package webservices.restful;

import entity.Credentials;
import io.jsonwebtoken.Jwts;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authentication")
public class AuthenticationEndpoint {

    // 60 mins token expiry (can set this to be much larger value)
    final int TOKEN_EXPIRY = 60;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {
        JsonObject LOGIN_FAILED_ERROR = Json.createObjectBuilder()
                .add("error", "Login Failed")
                .build();

        try {
            //TODO: perform authentication
            //here we will do a fake authentication
            if (credentials.getUsername().equals("user1") && credentials.getPassword().equals("password")) {

                //assume that the userId of this person is 51
                int userId = 51;
                Date now = new Date();

                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.MINUTE, TOKEN_EXPIRY);

                String jwtToken = Jwts.builder()
                        .claim("userId", "" + userId) //store the userId as a String
                        .setId(UUID.randomUUID().toString())
                        .setIssuedAt(now)
                        .setExpiration(cal.getTime())
                        .signWith(JWTHelper.hmacKey)
                        .compact();

                System.out.println("#jwtToken: " + jwtToken);
                JsonObject token = Json.createObjectBuilder()
                        .add("token", jwtToken)
                        .build();
                return Response.status(Response.Status.OK).entity(token).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(LOGIN_FAILED_ERROR).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(LOGIN_FAILED_ERROR).build();
        }
    }
}
