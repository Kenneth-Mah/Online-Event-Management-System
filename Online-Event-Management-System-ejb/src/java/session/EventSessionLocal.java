/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import error.NoResultException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface EventSessionLocal {
    
    public void createEvent(Long memberId, Event newEvent) throws NoResultException;
    
    public List<Event> retrieveOrganisingEventsByMemberId(Long memberId) throws NoResultException;
    
    public Event retrieveEventByEventId(Long eventId) throws NoResultException;
    
    public Boolean isEventInUse(Long eventId) throws NoResultException;
    
    public void deleteEvent(Long eventId) throws NoResultException;
    
    public List<Event> searchEventsByTitle(String title);
    
}
