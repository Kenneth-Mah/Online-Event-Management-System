/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import error.ResourceNotFoundException;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.RegistrationSessionLocal;

/**
 *
 * @author kenne
 */
@Path("registrations")
public class RegistrationsResource {
    
    @EJB
    private RegistrationSessionLocal registrationSessionLocal;
    
    @POST
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleAttendence(@PathParam("id") Long registrationId) {
        try {
            registrationSessionLocal.toggleAttendence(registrationId);
            return Response.status(Response.Status.OK).build();
        } catch (ResourceNotFoundException ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Registration not found")
                    .build();

            return Response.status(Response.Status.NOT_FOUND).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
}
