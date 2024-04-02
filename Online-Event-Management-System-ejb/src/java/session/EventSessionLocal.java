/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Event;
import error.ResourceNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author User
 */
@Local
public interface EventSessionLocal {
    
    public void createEvent(Long memberId, Event newEvent) throws ResourceNotFoundException;
    
    public List<Event> retrieveOrganisingEventsByMemberId(Long memberId) throws ResourceNotFoundException;
    
    public Event retrieveEventByEventId(Long eventId) throws ResourceNotFoundException;
    
    public Boolean isEventInUse(Long eventId) throws ResourceNotFoundException;
    
    public void deleteEvent(Long eventId) throws ResourceNotFoundException;
    
    public List<Event> searchEventsByTitle(String title);
    
    public List<Event> searchEventsByLocation(String location);
    
    public List<Event> searchEventsByDescription(String description);
    
    public List<Event> searchEventsByDate(Date date);
    
    public List<Event> searchEventsByDeadline(Date deadline);
    
}
