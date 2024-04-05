/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Member;
import java.security.Principal;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import session.MemberSessionLocal;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("members")
public class MembersResource {

    @EJB
    private MemberSessionLocal memberSessionLocal;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(Member newMember) {
        try {
            memberSessionLocal.createMember(newMember);
            return Response.status(200).entity(
                    newMember
            ).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("Error", "Username already exists")
                    .build();

            return Response.status(409).entity(exception).build();
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
        return Response.status(200).entity(
                null
        ).type(MediaType.APPLICATION_JSON).build();
    }
}