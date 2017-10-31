package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import facades.UserFacade;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import security.PasswordStorage;

@Path("register")
public class Register {

    Gson gson;

    public Register() {
        gson = new Gson();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSomething() {
        return "{\"message\" : \"Please register)\"}";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String jsonString) throws PasswordStorage.CannotPerformOperationException {
        JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CA3");
        UserFacade uf = new UserFacade(emf);
        uf.setUser(username, password);
        return gson.toJson(username);
        

    }
}

