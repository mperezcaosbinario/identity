package commons.error;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
@Priority(10)
public class NullPointerExceptionMapper implements ExceptionMapper<NullPointerException> {

    private static final Logger logger = Logger.getLogger(NullPointerExceptionMapper.class);
    @Override
    public Response toResponse(NullPointerException e) {
        logger.error("Null Pointer Exception: " + e.getMessage());
        CommonError commonError = new CommonError("Null Pointer Exception");
        return Response.status(Response.Status.NOT_FOUND).entity(commonError).build();
    }
}