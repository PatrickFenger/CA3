package rest;

import com.google.gson.Gson;
import entity.Role;
import entity.User;
import rest.utilities.ErrorResponse;
import rest.utilities.ExclusionGsonBuilder;
import security.IUserFacade;
import security.UserFacadeFactory;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("users")
public class UserResource {
    Gson gson;
    IUserFacade facade;

    public UserResource() {
        this.gson = new ExclusionGsonBuilder().excludeFieldNames("users","passwordHash").buildGson();
        facade = UserFacadeFactory.getInstance();
    }

    @Path("roles/{username}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoles(@PathParam("username") String username,String json) {
        try {
            List<Role> roles = Arrays.asList(gson.fromJson(json,Role[].class));
            User user = facade.editUserRoles(roles, username);
            String responseJson = this.gson.toJson(user);
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } catch (EntityNotFoundException e) {
            return new ErrorResponse(e).build();
        }
    }

    @DELETE
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("username") String username) {
        try {
            String responseJson = gson.toJson(facade.deleteUser(username));
            return Response.ok(responseJson, MediaType.APPLICATION_JSON).build();
        } catch (EntityNotFoundException e) {
            return new ErrorResponse(e).build();
        }
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        User user = facade.getUserByUserId(username);
        if (user == null) {
            return new ErrorResponse(404, "UserResource " + username + " not found!").build();
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
