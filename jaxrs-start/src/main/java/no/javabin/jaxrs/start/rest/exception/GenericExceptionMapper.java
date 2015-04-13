package no.javabin.jaxrs.start.rest.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

// TODO: Aktiver @Provider
//@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UriInfo uriInfo;

    public GenericExceptionMapper(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public Response toResponse(Throwable t) {

        int responseCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        if(t instanceof WebApplicationException) {
            responseCode = ((WebApplicationException) t).getResponse().getStatus();
        }


        ErrorMessage errorMessage = new ErrorMessage(Integer.toString(responseCode), "Enellerannentittel", t.getMessage());

        //logger.debug(errorMessage.toString());

        return Response
                .status(responseCode)
                .entity(errorMessage)
                .location(uriInfo.getRequestUri())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}


