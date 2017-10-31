package rest;

import com.google.gson.Gson;
import facades.UserFacade;
import security.IUserFacade;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class User {
    Gson gson;
    IUserFacade facade;

    public User() {
        this.gson = new EnhancedGSONBuilder().excludeFiledNames("users").buildGSON();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        facade = new UserFacade(emf);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editUser(String json) {
        entity.User user = gson.fromJson(json, entity.User.class);
        return gson.toJson(facade.editUser(user));
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("username") String username) {
        try {
            facade.deleteUser(username);
            return Response.status(200).build();
        } catch (EntityNotFoundException e) {
            return Response.status(404).build();
        }
    }
}
