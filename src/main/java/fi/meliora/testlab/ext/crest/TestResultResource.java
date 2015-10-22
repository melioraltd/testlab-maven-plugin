package fi.meliora.testlab.ext.crest;

import fi.meliora.testlab.ext.crest.exception.TestlabAPIException;
import fi.meliora.testlab.ext.rest.model.AddTestResultResponse;
import fi.meliora.testlab.ext.rest.model.TestResult;
import org.codegist.crest.annotate.*;
import org.codegist.crest.serializer.jackson.JsonEntityWriter;

/**
 * Crest client-side descriptor for Testlab's TestResultResource.
 *
 * @author Marko Kanala
 */
@Path("testresult")
@org.codegist.crest.annotate.ErrorHandler(fi.meliora.testlab.ext.crest.ErrorHandler.class)
public interface TestResultResource {

    @PUT
    @ConnectionTimeout(30000)
    @SocketTimeout(30000)
    @EntityWriter(JsonEntityWriter.class)
    public AddTestResultResponse addTestResult(@FormParam("param") TestResult result)
            throws TestlabAPIException;

}
