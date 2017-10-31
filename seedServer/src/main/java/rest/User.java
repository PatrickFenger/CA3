package rest;

import com.google.gson.Gson;
import facades.UserFacade;
import rest.utilities.EnhancedGSONBuilder;
import rest.utilities.ErrorMessage;
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
        this.gson = new EnhancedGSONBuilder().excludeFiledNames("users","passwordHash").buildGSON();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_development");
        facade = new UserFacade(emf);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(String json) {
        entity.User user = gson.fromJson(json, entity.User.class);
        try {
            String responseJson = gson.toJson(facade.editUser(user));
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } catch (EntityNotFoundException e) {
            return getErrorResponse(new ErrorMessage(e));
        }
    }

    private Response getErrorResponse(ErrorMessage errorMessage) {
        return Response.status(404)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("username") String username) {
        try {
            facade.deleteUser(username);
            return Response.status(200).build();
        } catch (EntityNotFoundException e) {
            return getErrorResponse(new ErrorMessage(e));
        }
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        entity.User user = facade.getUserByUserId(username);
        if (user == null) {
            return getErrorResponse(new ErrorMessage(404, "User " + username + " not found!"));
        }
        String json = gson.toJson(user);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        return gson.toJson(facade.getUsers());
    }
}
