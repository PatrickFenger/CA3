package rest;

import com.google.gson.Gson;
import entity.Place;
import facades.PlaceFacade;
import facades.PlaceFacadeFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * Created by adam on 01/11/2017.
 */
@Path("places")
public class PlaceResource {
    Gson gson;
    PlaceFacade facade;
    private static final String FILE_LOCATION = "/Users/adam/img/";

    public PlaceResource() {
        this.gson = new Gson();
        facade = PlaceFacadeFactory.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPlaces() {
        return gson.toJson(facade.getAllPlaces());
    }

    @Path("add")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@FormDataParam("city") String city,
                               @FormDataParam("address") String address,
                               @FormDataParam("zip") String zip,
                               @FormDataParam("description") String description,
                               @FormDataParam("file") InputStream file,
                               @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
        String fileName = fileDisposition.getFileName();
        File image = saveFile(file, fileName);
        Place place = facade.addPlace(address,city,zip,description,image);
        return Response.ok(gson.toJson(place)).build();
    }

    private File saveFile(InputStream is, String fileLocation) throws IOException {
        String location = FILE_LOCATION + fileLocation;
        File file = new File(location);
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[256];
        int bytes = 0;
        while ((bytes = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
        return file;
    }
}
