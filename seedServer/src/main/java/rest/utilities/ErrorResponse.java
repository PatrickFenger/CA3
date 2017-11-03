package rest.utilities;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by adam on 31/10/2017.
 */
@XmlRootElement
public class ErrorResponse {
    @XmlElement(name = "status")
    int status;
    @XmlElement(name = "message")
    String message;

    public ErrorResponse(EntityNotFoundException e) {
        this.status = 404;
        this.message = e.getMessage();
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response build() {
        return Response.status(status).entity(this).build();
    }
}
