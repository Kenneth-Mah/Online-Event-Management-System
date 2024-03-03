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
    public Long createMember(Member m) throws InputDataValidationException {
        Set<ConstraintViolation<Member>> constraintViolations = validator.validate(m);
        
        if (constraintViolations.isEmpty()) {
            em.persist(m);
            em.flush();
            
            return m.getId();
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Member retrieveMemberByUsername(String username) throws NoResultException {
        Query query = em.createQuery("SELECT m FROM Member m WHERE m.username = :inUsername");
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
            Member m = retrieveMemberByUsername(username);

            if (m.getPassword().equals(password)) {
                return m.getId();
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Member>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
