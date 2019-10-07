package fi.meliora.testlab.ext.crest.exception;

/**
 * Thrown if rest endpoint sends 400 BAD_REQUEST response.
 *
 * @author Marko Kanala
 */
public class ValidationException extends TestlabAPIException {

    public ValidationException(Object responseData) {
        super(responseData);
    }

}

