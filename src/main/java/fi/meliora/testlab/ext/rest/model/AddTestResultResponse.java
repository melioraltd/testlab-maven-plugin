package fi.meliora.testlab.ext.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TestResultResource.addTestResult response.
 *
 * @author Marko Kanala
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddTestResultResponse extends ResponseObject {
    @XmlElement
    private Long testRunId;

    /**
     * @return id of the test run which was created or updated, null if nothing was done
     */
    public Long getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }
}
