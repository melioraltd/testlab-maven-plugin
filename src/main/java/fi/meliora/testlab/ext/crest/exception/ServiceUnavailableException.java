package fi.meliora.testlab.ext.crest.exception;

/**
 * Thrown if rest endpoint sends 503 SERVICE UNAVAILABLE response.
 *
 * @author Marko Kanala
 */
public class ServiceUnavailableException extends TestlabAPIException {

    public ServiceUnavailableException(Object responseData) {
        super(responseData);
    }

}

