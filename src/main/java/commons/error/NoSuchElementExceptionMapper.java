package commons.error;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import java.util.NoSuchElementException;

@Provider
@Priority(10)
public class NoSuchElementExceptionMapper implements ExceptionMapper<NoSuchElementException> {

    private static final Logger logger = Logger.getLogger(NoSuchElementExceptionMapper.class);
    @Override
    public Response toResponse(NoSuchElementException e) {
        CommonError commonError = new CommonError("Null Pointer Exception");
        return Response.status(404).entity(commonError).build();
    }
}