/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Event;
import entity.Member;
import entity.Registration;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.EventSessionLocal;

/**
 *
 * @author User
 */
@Path("events")
public class EventsResource {

    @EJB
    private EventSessionLocal eventSessionLocal;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private void eventRemoveCyclicReference(Event event) {
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

    private void eventsRemoveCyclicReference(List<Event> events) {
        for (Event event : events) {
            eventRemoveCyclicReference(event);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getALLEvents() {
        List<Event> events = eventSessionLocal.searchEventsByTitle(null);
        eventsRemoveCyclicReference(events);
        return events;
    }

    @GET
    @Path("/query")
    public Response searchEvents(@QueryParam("title") String title,
            @QueryParam("location") String location,
            @QueryParam("description") String description,
            @QueryParam("date") String dateString,
            @QueryParam("deadline") String deadlineString) {
        if (title != null) {
            List<Event> results = eventSessionLocal.searchEventsByTitle(title);
            eventsRemoveCyclicReference(results);

            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
            };

            return Response.status(200).entity(
                    entity
            ).build();
        } else if (location != null) {
            List<Event> results = eventSessionLocal.searchEventsByLocation(location);
            eventsRemoveCyclicReference(results);

            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
            };

            return Response.status(200).entity(
                    entity
            ).build();
        } else if (description != null) {
            List<Event> results = eventSessionLocal.searchEventsByDescription(description);
            eventsRemoveCyclicReference(results);

            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
            };

            return Response.status(200).entity(
                    entity
            ).build();
        } else if (dateString != null) {
            try {
                Date date = dateFormat.parse(dateString);

                List<Event> results = eventSessionLocal.searchEventsByDate(date);
                eventsRemoveCyclicReference(results);

                GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
                };

                return Response.status(200).entity(
                        entity
                ).build();
            } catch (ParseException ex) {
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "Date must be in dd-mm-yyyy format")
                        .build();

                return Response.status(400).entity(exception)
                        .type(MediaType.APPLICATION_JSON).build();
            }
        } else if (deadlineString != null) {
            try {
                Date deadline = dateFormat.parse(deadlineString);

                List<Event> results = eventSessionLocal.searchEventsByDeadline(deadline);
                eventsRemoveCyclicReference(results);

                GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
                };

                return Response.status(200).entity(
                        entity
                ).build();
            } catch (ParseException ex) {
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "Deadline must be in dd-mm-yyyy format")
                        .build();

                return Response.status(400).entity(exception)
                        .type(MediaType.APPLICATION_JSON).build();
            }
        } else {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "No query conditions")
                    .build();

            return Response.status(400).entity(exception).build();
        }
    }
}
