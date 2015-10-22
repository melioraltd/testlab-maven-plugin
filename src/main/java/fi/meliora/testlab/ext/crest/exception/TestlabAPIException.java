package fi.meliora.testlab.ext.crest.exception;

import org.codegist.crest.CRestException;

/**
 * A non-fatal informative status code mapped exception thrown from testlab endpoint.
 *
 * @author Marko Kanala
 */
public class TestlabAPIException extends CRestException {

    private Object responseData;

    public TestlabAPIException(Object responseData) {
        super();
        this.responseData = responseData;
    }

    public Object getResponseData() {
        return responseData;
    }

}
