package rest;

import com.google.gson.Gson;
import facades.RandomNumber;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("demouser")
@RolesAllowed("User")
public class User {
    Gson gson;
    public User(){
        gson = new Gson();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSomething() {
        return "{\"message\" : \"Hello User from Server (Accesible by only authenticated USERS)\"}";
    }

    @GET
    @Path("random")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNumber() {
        int number;
        RandomNumber Rnd = new RandomNumber();
        number = Rnd.getRndNumber();

        return gson.toJson(number);
    }
}
