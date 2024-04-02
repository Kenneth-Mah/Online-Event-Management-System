/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Event;
import entity.Member;
import entity.Registration;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.EventSessionLocal;

/**
 *
 * @author User
 */
@Path("events")
public class EventsResource {
    
    @EJB
    private EventSessionLocal eventSessionLocal;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getALLEvents() {
        List<Event> events = eventSessionLocal.searchEventsByTitle(null);
        for (Event event : events) {
            List<Member> organisingMembers = event.getOrganisingMembers();
            List<Registration> registrations = event.getRegistrations();
            
            for (Member organisingMember : organisingMembers) {
                organisingMember.setOrganisingEvents(null);
                organisingMember.setRegistrations(null);
                organisingMember.setPassword(null);
            }
            for (Registration registration : registrations) {
                Member registeredMember = registration.getMember();
                registeredMember.setOrganisingEvents(null);
                registeredMember.setRegistrations(null);
                registeredMember.setPassword(null);
                
                registration.setEvent(null);
            }
        }
        return events;
    }
}
