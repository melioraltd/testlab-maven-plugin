package fi.meliora.testlab.ext.crest.exception;

/**
 * Thrown if rest endpoint sends 401 UNAUTHORIZED response.
 *
 * @author Marko Kanala
 */
public class UnauthorizedException extends TestlabAPIException {

    public UnauthorizedException(Object responseData) {
        super(responseData);
    }

}

