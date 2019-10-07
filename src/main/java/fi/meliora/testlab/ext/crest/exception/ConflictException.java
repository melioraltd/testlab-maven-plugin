package fi.meliora.testlab.ext.crest.exception;

/**
 * Thrown if rest endpoint sends 409 CONFLICT response.
 *
 * @author Marko Kanala
 */
public class ConflictException extends TestlabAPIException {

    public ConflictException(Object responseData) {
        super(responseData);
    }

}
