package rest;

import com.google.gson.Gson;
import rest.utilities.ErrorMessage;
import rest.utilities.ExclusionGsonBuilder;
import security.IUserFacade;
import security.UserFacadeFactory;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class User {
    Gson gson;
    IUserFacade facade;

    public User() {
        this.gson = new ExclusionGsonBuilder().excludeFieldNames("users","passwordHash").buildGson();
        facade = UserFacadeFactory.getInstance();
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
        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("username") String username) {
        try {
            String responseJson = gson.toJson(facade.deleteUser(username));
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
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
