package fi.meliora.testlab.ext.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Encapsulates a single inbound result of a test case step.
 *
 * @author Marko Kanala
 */
@Deprecated
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseResultStep extends ModelObject {
    public final static int RESULT_NOTRUN = 0;
    public final static int RESULT_PASS = 1;
    public final static int RESULT_FAIL = 2;
    public final static int RESULT_SKIP = 3;
    public final static int RESULT_BLOCK = 4;

    /**
     * result of the step
     */
    private int result;

    /**
     * description of the execution step, text
     */
    private String description;

    /**
     * expected end result of the execution step, text
     */
    private String expected;

    /**
     * Execution comment of the execution step, text
     */
    private String comment;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
