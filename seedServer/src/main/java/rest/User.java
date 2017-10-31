package rest;

import com.google.gson.Gson;
import facades.UserFacade;
import security.IUserFacade;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class User {
    Gson gson;
    IUserFacade facade;

    public User() {
        this.gson = new Gson();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        facade = new UserFacade(emf);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editUser(String json) {
        entity.User user = gson.fromJson(json, entity.User.class);
        facade.editUser(user);
        return gson.toJson(user);
    }
}
