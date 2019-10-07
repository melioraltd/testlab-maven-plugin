package fi.meliora.testlab.ext.crest;

import fi.meliora.testlab.ext.crest.exception.*;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Our Testlab API call error handler.
 *
 * @author Marko Kanala
 */
public class ErrorHandler implements org.codegist.crest.handler.ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public <T> T handle(Request request, Exception e) throws Exception {
        if(e instanceof RequestException) {
            RequestException re = (RequestException)e;
            Response testlabResponse = re.getResponse();

            if(testlabResponse != null) {
                // read response, if any
                String responseData = getResponseIfAny(testlabResponse);

                if(log.isErrorEnabled())
                    log.error(
                            "Testlab REST call failed with response '{}', status code {}, exception: {}",
                            new Object[] {
                                    responseData,
                                    testlabResponse.getStatusCode(),
                                    e
                            }
                    );

                // map status code to checked exceptions
                int statusCode = testlabResponse.getStatusCode();
                if(statusCode == javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode()) {
                    // call returned not found status
                    throw new NotFoundException(responseData);
                } else if(statusCode == javax.ws.rs.core.Response.Status.CONFLICT.getStatusCode()) {
                    throw new ConflictException(responseData);
                } else if(statusCode == javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE.getStatusCode()) {
                    throw new ServiceUnavailableException(responseData);
                } else if(statusCode == javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode()) {
                    throw new UnauthorizedException(responseData);
                } else if(statusCode == javax.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode()) {
                    throw new ValidationException(responseData);
                }

                if(responseData != null && responseData.length() > 0) {
                    throw new TestlabAPIException(responseData);
                }
            }
        }

        if(log.isErrorEnabled())
            log.error("Testlab REST call exception: " + e.getMessage(), e);

        if(e.getCause() != null && e.getCause() instanceof Exception) {
            throw (Exception)e.getCause();
        }
        throw e;
    }

    protected String getResponseIfAny(Response response) {
        // read response
        String responseData = null;
        InputStream is = null;
        try {
            is = response.asStream();
            if(is != null) {
                String charset = response.getCharset() != null ? response.getCharset().name() : "UTF-8";
                Scanner s = new Scanner(is, charset).useDelimiter("\\A");
                if(s.hasNext())
                    responseData = s.next();
            }
            if(responseData != null && responseData.length() > 400)
                responseData = responseData.substring(0, 400) + "...";
        } catch (Exception e) {
            // could not read response, just ignore
        } finally {
            if(is != null)
                try { is.close(); } catch (Exception ee) {}
        }
        return responseData;
    }

}
