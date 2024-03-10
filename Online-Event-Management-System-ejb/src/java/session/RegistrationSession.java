/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Registration;
import entity.Event;
import entity.Member;
import error.NoResultException;
import error.RegistrationDeletionNotAllowedException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kenne
 */
@Stateless
public class RegistrationSession implements RegistrationSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private MemberSessionLocal memberSessionLocal;
    @EJB
    private EventSessionLocal eventSessionLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createRegistration(Long memberId, Long eventId) throws NoResultException {
        if (!doesRegistrationExist(memberId, eventId)) {
            Registration registration = new Registration();

            Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
            Event event = eventSessionLocal.retrieveEventByEventId(eventId);

            registration.setMember(member);
            registration.setEvent(event);
            member.getRegistrations().add(registration);
            event.getRegistrations().add(registration);

            em.persist(registration);
        }
    }

    private Boolean doesRegistrationExist(Long memberId, Long eventId) throws NoResultException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
        Event event = eventSessionLocal.retrieveEventByEventId(eventId);

        Query query = em.createQuery("SELECT r FROM Registration r WHERE r.member = :member AND r.event = :event");
        query.setParameter("member", member);
        query.setParameter("event", event);
        try {
            Registration registration = (Registration) query.getSingleResult();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<Event> retrieveRegisteredEventsByMemberId(Long memberId) throws NoResultException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);

        Query query = em.createQuery("SELECT e FROM Event e, "
                + "IN (e.registrations) r "
                + "WHERE r.member = :member");
        query.setParameter("member", member);

        return query.getResultList();
    }

    @Override
    public void deleteRegistration(Long memberId, Long eventId) throws NoResultException, RegistrationDeletionNotAllowedException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
        Event event = eventSessionLocal.retrieveEventByEventId(eventId);

        //can only unregister before the start of the event
        if (event.getDate().after(new Date())) {
            Query query = em.createQuery("SELECT r FROM Registration r WHERE r.member = :member AND r.event = :event");
            query.setParameter("member", member);
            query.setParameter("event", event);

            Registration registration = (Registration) query.getSingleResult();

            registration.setMember(null);
            registration.setEvent(null);
            member.getRegistrations().remove(registration);
            event.getRegistrations().remove(registration);

            em.remove(registration);
        } else {
            throw new RegistrationDeletionNotAllowedException("Cannot unregister after start of event");
        }
    }

    @Override
    public Registration retrieveRegistrationByRegistrationId(Long registrationId) throws NoResultException {
        Registration registration = em.find(Registration.class, registrationId);
        
        if (registration != null) {
            return registration;
        } else {
            throw new NoResultException("Registration ID " + registrationId + " does not exist");
        }
    }

    @Override
    public void toggleAttendence(Long registrationId) throws NoResultException {
        Registration registration = retrieveRegistrationByRegistrationId(registrationId);
        
        if (registration.getHasAttended() == true) {
            registration.setHasAttended(false);
        } else {
            registration.setHasAttended(true);
        }
    }
}
