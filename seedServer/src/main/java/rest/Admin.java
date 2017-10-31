package rest;

import com.google.gson.Gson;
import facades.RandomNumber;
import facades.UserFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import mappers.UsersMapper;

@Path("demoadmin")
@RolesAllowed("Admin")
public class Admin {
    Gson gson;
    
    
    public Admin(){
       gson = new Gson();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSomething() {
        String now = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
        return "{\"message\" : \"Hello Admin from server (call accesible by only authenticated ADMINS)\",\n" + "\"serverTime\": \"" + now + "\"}";
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        UserFacade uf = new UserFacade(emf);
        
        List<entity.User> users = uf.getUsers();
        return gson.toJson(new UsersMapper(users));
    }

}
