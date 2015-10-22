package fi.meliora.testlab.ext.crest.exception;

/**
 * Exception thrown when api responds with 404 for some reason.
 *
 * @author Marko Kanala
 */
public class NotFoundException extends TestlabAPIException {

    public NotFoundException(Object responseData) {
        super(responseData);
    }

}
