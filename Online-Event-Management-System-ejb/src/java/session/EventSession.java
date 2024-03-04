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
        em.persist(newEvent);
        
        Member member = memberSessionLocal.retrieveMemberByMemberId(memberId);
        member.getOrganisingEvents().add(newEvent);
        
        newEvent.getOrganisingMembers().add(member);
    }

    @Override
    public List<Event> searchEventsByTitle(String title) {
        Query q;
        if (title != null) {
            q = em.createQuery("SELECT e FROM Event e WHERE "
                    + "LOWER(e.title) LIKE :title");
            q.setParameter("name", "%" + title.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT e FROM Event e");
        }

        return q.getResultList();
    }
}
