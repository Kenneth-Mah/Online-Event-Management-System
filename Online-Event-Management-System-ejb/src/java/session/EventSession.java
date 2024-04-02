/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import entity.Member;
import error.ResourceNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author User
 */
@Stateless
public class EventSession implements EventSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private MemberSessionLocal memberSessionLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void createEvent(Long memberId, Event newEvent) throws ResourceNotFoundException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);

        newEvent.getOrganisingMembers().add(member);
        member.getOrganisingEvents().add(newEvent);

        em.persist(newEvent);
    }

    @Override
    public List<Event> retrieveOrganisingEventsByMemberId(Long memberId) throws ResourceNotFoundException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
        Query query = em.createQuery(
                "SELECT e FROM Event e "
                + "WHERE :member MEMBER OF e.organisingMembers"
        );
        query.setParameter("member", member);

        return query.getResultList();
    }

    @Override
    public Event retrieveEventByEventId(Long eventId) throws ResourceNotFoundException {
        Event event = em.find(Event.class, eventId);

        if (event != null) {
            return event;
        } else {
            throw new ResourceNotFoundException("Event ID " + eventId + " does not exist");
        }
    }

    @Override
    public Boolean isEventInUse(Long eventId) throws ResourceNotFoundException {
        Event event = retrieveEventByEventId(eventId);

        if (event.getRegistrations().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteEvent(Long eventId) throws ResourceNotFoundException {
        if (!isEventInUse(eventId)) {
            Event eventToRemove = retrieveEventByEventId(eventId);

            em.remove(eventToRemove);
        } else {
            Event eventToCancel = retrieveEventByEventId(eventId);

            eventToCancel.setIsCancelled(true);
        }
    }

    @Override
    public List<Event> searchEventsByTitle(String title) {
        Query q;
        if (title != null) {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE LOWER(e.title) LIKE :title "
                    + "AND e.isCancelled = FALSE"
            );
            q.setParameter("title", "%" + title.toLowerCase() + "%");
        } else {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE e.isCancelled = FALSE"
            );
        }

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventsByLocation(String location) {
        Query q;
        if (location != null) {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE LOWER(e.location) LIKE :location "
                    + "AND e.isCancelled = FALSE"
            );
            q.setParameter("location", "%" + location.toLowerCase() + "%");
        } else {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE e.isCancelled = FALSE"
            );
        }

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventsByDescription(String description) {
        Query q;
        if (description != null) {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE LOWER(e.description) LIKE :description "
                    + "AND e.isCancelled = FALSE"
            );
            q.setParameter("description", "%" + description.toLowerCase() + "%");
        } else {
            q = em.createQuery(
                    "SELECT e FROM Event e "
                    + "WHERE e.isCancelled = FALSE"
            );
        }

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventsByDate(Date date) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.date = :date");
        q.setParameter("date", date, TemporalType.DATE);

        return q.getResultList();
    }

    @Override
    public List<Event> searchEventsByDeadline(Date deadline) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE e.deadline = :deadline");
        q.setParameter("deadline", deadline, TemporalType.DATE);

        return q.getResultList();
    }

}
