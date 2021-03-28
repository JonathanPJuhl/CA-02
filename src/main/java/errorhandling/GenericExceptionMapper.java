
package errorhandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Context
    ServletContext context;
    
    @Override
    public Response toResponse(Throwable exception){
    Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, exception);
        Response.StatusType type = getStatusType(exception);
        ExceptionDTO erDTO;
        if (exception instanceof WebApplicationException) {
            erDTO = new ExceptionDTO(type.getStatusCode(), ((WebApplicationException) exception).getMessage());
        } else {

            erDTO = new ExceptionDTO(type.getStatusCode(), type.getReasonPhrase());
        }
        return Response.status(type.getStatusCode())
                .entity(gson.toJson(erDTO))
                .type(MediaType.APPLICATION_JSON).
                build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse().getStatusInfo();
        }
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

    //To make json-errors response in filter
    public Response makeErrRes(String msg, int status) {
        ExceptionDTO error = new ExceptionDTO(status, msg);
        String errJson = gson.toJson(error);
        return Response.status(error.getHttpCode())
                .entity(errJson)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
    
}
