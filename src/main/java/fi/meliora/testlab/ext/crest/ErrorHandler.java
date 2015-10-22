package fi.meliora.testlab.ext.crest;

import fi.meliora.testlab.ext.crest.exception.NotFoundException;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Our Testlab api call error handler.
 *
 * @author Marko Kanala
 */
public class ErrorHandler implements org.codegist.crest.handler.ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public <T> T handle(Request request, Exception e) throws Exception {
        if(e instanceof RequestException) {
            // if we have a http response from testlab just log the error
            RequestException re = (RequestException)e;
            Response testlabResponse = re.getResponse();

            if(testlabResponse != null) {
                // read response

                String responseData = null;
                // try to read error
                InputStream is = null;
                try {
                    is = testlabResponse.asStream();
                    if(is != null) {
                        String charset = testlabResponse.getCharset() != null ? testlabResponse.getCharset().name() : "UTF-8";
                        Scanner s = new Scanner(is, charset).useDelimiter("\\A");
                        if(s.hasNext())
                            responseData = s.next();
                    }
                    if(responseData != null && responseData.length() > 400)
                        responseData = responseData.substring(0, 400) + "...";
                } catch (Exception ee) {
                    // ...
                } finally {
                    if(is != null)
                        try { is.close(); } catch (Exception eee) {}
                }

                log.error(
                        "Rest call failed with status code {}, response: {}",
                        testlabResponse.getStatusCode(),
                        responseData
                );

                // map status code to checked exceptions
                int statusCode = testlabResponse.getStatusCode();
                if(statusCode == javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode()) {
                    // call returned not found status
                    throw new NotFoundException(responseData);
                }
            }
        }
        if(e.getCause() != null && e.getCause() instanceof Exception) {
            throw (Exception)e.getCause();
        }
        throw e;
    }

}
