/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Member;
import error.InputDataValidationException;
import error.InvalidLoginCredentialException;
import error.ResourceNotFoundException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kenne
 */
@Stateless
public class MemberSession implements MemberSessionLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createMember(Member newMember) throws InputDataValidationException {
        String newUsername = newMember.getUsername();

        if (!usernameInUse(newUsername)) {
            em.persist(newMember);
            em.flush();

            return newMember.getId();
        } else {
            throw new InputDataValidationException("Member Username " + newUsername + " is already in use");
        }
    }

    private Boolean usernameInUse(String newUsername) {
        try {
            retrieveMemberByUsername(newUsername);
            return true;
        } catch (ResourceNotFoundException ex) {
            return false;
        }
    }

    @Override
    public Member retrieveMemberByUsername(String username) throws ResourceNotFoundException {
        Query query = em.createQuery(
                "SELECT m FROM Member m "
                + "WHERE m.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Member) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new ResourceNotFoundException("Member Username " + username + " does not exist");
        }
    }

    @Override
    public Long login(String username, String password) throws InvalidLoginCredentialException {
        try {
            Member member = retrieveMemberByUsername(username);

            if (member.getPassword().equals(password)) {
                return member.getId();
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        } catch (ResourceNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }

    @Override
    public Member retrieveMemberByMemberId(Long memberId) throws ResourceNotFoundException {
        Member member = em.find(Member.class, memberId);

        if (member != null) {
            return member;
        } else {
            throw new ResourceNotFoundException("Member ID " + memberId + " does not exist");
        }
    }

    @Override
    public void updateMember(Member updatedMember) throws ResourceNotFoundException {
        Member oldMember = retrieveMemberByMemberId(updatedMember.getId());
        
        // Do not allow change of username
        oldMember.setPassword(updatedMember.getPassword());
        oldMember.setName(updatedMember.getName());
        oldMember.setPhoto(updatedMember.getPhoto());
        oldMember.setPhone(updatedMember.getPhone());
        oldMember.setEmail(updatedMember.getEmail());
    }

}
