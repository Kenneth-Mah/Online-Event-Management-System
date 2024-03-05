/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import entity.Member;
import error.NoResultException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public void createEvent(Long memberId, Event newEvent) throws NoResultException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);

        newEvent.getOrganisingMembers().add(member);
        member.getOrganisingEvents().add(newEvent);

        em.persist(newEvent);
    }

    @Override
    public List<Event> retrieveOrganisingEventsByMemberId(Long memberId) throws NoResultException {
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
        Query query = em.createQuery(
                "SELECT e FROM Event e "
                + "WHERE :member MEMBER OF e.organisingMembers"
        );
        query.setParameter("member", member);

        return query.getResultList();
    }

    @Override
    public Event retrieveEventByEventId(Long eventId) throws NoResultException {
        Event event = em.find(Event.class, eventId);

        if (event != null) {
            return event;
        } else {
            throw new NoResultException("Event ID " + eventId + " does not exist");
        }
    }

    @Override
    public Boolean isEventInUse(Long eventId) throws NoResultException {
        Event event = retrieveEventByEventId(eventId);

        if (event.getRegistrations().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteEvent(Long eventId) throws NoResultException {
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

}
