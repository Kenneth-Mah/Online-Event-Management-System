/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Event;
import entity.Member;
import entity.Registration;
import error.ResourceNotFoundException;
import java.security.Principal;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import session.EventSessionLocal;
import session.MemberSessionLocal;
import session.RegistrationSessionLocal;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("members")
public class MembersResource {

    @EJB
    private MemberSessionLocal memberSessionLocal;
    @EJB
    private EventSessionLocal eventSessionLocal;
    @EJB
    private RegistrationSessionLocal registrationSessionLocal;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(Member newMember) {
        try {
            memberSessionLocal.createMember(newMember);
            return Response.status(Response.Status.OK).entity(
                    newMember
            ).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("Error", "Username already exists")
                    .build();

            return Response.status(Response.Status.CONFLICT).entity(exception).build();
        }
    }

    @GET
    @Secured
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyProfile(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String userId = principal.getName();

        //from this userId, we can then get the data from the session bean accordingly
        System.out.println("userId : " + userId);
        return Response.status(Response.Status.OK).entity(
                null
        ).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}/organising-events")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrganisingEvents(@PathParam("id") Long memberId) {
        try {
            List<Event> results = eventSessionLocal.retrieveOrganisingEventsByMemberId(memberId);
            eventsRemoveCyclicReference(results);

            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
            };

            return Response.status(Response.Status.OK).entity(
                    entity
            ).build();
        } catch (ResourceNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Member ID " + memberId + " does not exist")
                    .build();

            return Response.status(Response.Status.BAD_REQUEST).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @POST
    @Path("/{member_id}/organising-events/{event_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerForEvent(@PathParam("member_id") Long memberId, 
            @PathParam("event_id") Long eventId) {
        try {
            registrationSessionLocal.createRegistration(memberId, eventId);
            return Response.status(Response.Status.OK).build();
        } catch (ResourceNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Member or Event not found")
                    .build();

            return Response.status(Response.Status.NOT_FOUND).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{id}/registered-events")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRegisteredEvents(@PathParam("id") Long memberId) {
        try {
            List<Event> results = registrationSessionLocal.retrieveRegisteredEventsByMemberId(memberId);
            eventsRemoveCyclicReference(results);

            GenericEntity<List<Event>> entity = new GenericEntity<List<Event>>(results) {
            };

            return Response.status(Response.Status.OK).entity(
                    entity
            ).build();
        } catch (ResourceNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Member ID " + memberId + " does not exist")
                    .build();

            return Response.status(Response.Status.BAD_REQUEST).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
}
