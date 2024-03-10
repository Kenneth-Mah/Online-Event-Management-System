/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Member;
import error.InputDataValidationException;
import error.InvalidLoginCredentialException;
import error.NoResultException;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author kenne
 */
@Stateless
public class MemberSession implements MemberSessionLocal {

    @PersistenceContext
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MemberSession() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createMember(Member newMember) throws InputDataValidationException {
        Set<ConstraintViolation<Member>> constraintViolations = validator.validate(newMember);

        if (constraintViolations.isEmpty()) {
            em.persist(newMember);
            em.flush();

            return newMember.getId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Member>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public Member retrieveMemberByUsername(String username) throws NoResultException {
        Query query = em.createQuery(
                "SELECT m FROM Member m "
                + "WHERE m.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Member) query.getSingleResult();
        } catch (Exception ex) {
            throw new NoResultException("Member Username " + username + " does not exist");
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
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }

    @Override
    public Member retrieveMemberByMemberId(Long memberId) throws NoResultException {
        Member member = em.find(Member.class, memberId);

        if (member != null) {
            return member;
        } else {
            throw new NoResultException("Member ID " + memberId + " does not exist");
        }
    }

    @Override
    public void updateMember(Member updatedMember) throws NoResultException {
        Member oldMember = retrieveMemberByMemberId(updatedMember.getId());
        
        // Do not allow change of username
        oldMember.setPassword(updatedMember.getPassword());
        oldMember.setName(updatedMember.getName());
        oldMember.setPhone(updatedMember.getPhone());
        oldMember.setEmail(updatedMember.getEmail());
    }

}
