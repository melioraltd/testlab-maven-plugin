package fi.meliora.testlab.ext.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Encapsulates a single inbound result of a test case.
 *
 * @author Marko Kanala
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestCaseResult extends ModelObject {
    public final static int RESULT_NOTRUN = 0;
    public final static int RESULT_PASS = 1;
    public final static int RESULT_FAIL = 2;
    public final static int RESULT_SKIP = 3;
    public final static int RESULT_BLOCK = 4;

    private Long testCaseId;
    private String mappingId;

    private int result;

    private Long started;
    private Long run;

    private Long runById;
    private String runBy;

    private String comment;

    private List<TestCaseResultStep> steps;

    public Long getTestCaseId() {
        return testCaseId;
    }

    /**
     * Test case id for this result. Optional if mappingId is set.
     *
     * @param testCaseId
     */
    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getMappingId() {
        return mappingId;
    }

    /**
     * Id value to use to lookup the test case from Testlab. This value is compared
     * against project's custom field value from custom field set in TestResult.testCaseMappingField.
     *
     * @param mappingId
     */
    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public int getResult() {
        return result;
    }

    /**
     * Result status value for the run test case.
     *
     * @param result
     */
    public void setResult(int result) {
        this.result = result;
    }

    public long getStarted() {
        return started;
    }

    /**
     * Timestamp of when this test case was started.
     *
     * @param started
     */
    public void setStarted(Long started) {
        this.started = started;
    }

    public Long getRun() {
        return run;
    }

    /**
     * Timestamp of when this test case was completed.
     *
     * @param run
     */
    public void setRun(Long run) {
        this.run = run;
    }

    public Long getRunById() {
        return runById;
    }

    /**
     * Id of the user who ran this test case. Optional.
     *
     * @param runById
     */
    public void setRunById(Long runById) {
        this.runById = runById;
    }

    public String getRunBy() {
        return runBy;
    }

    /**
     * Name of the user who ran this test case. Optional.
     *
     * @param runBy
     */
    public void setRunBy(String runBy) {
        this.runBy = runBy;
    }

    /**
     * Comment (i.e. error description) for this result. This value is used
     * when adding issues for this result.
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Optional execution steps and their results for this result.
     *
     * @return
     */
    public List<TestCaseResultStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TestCaseResultStep> steps) {
        this.steps = steps;
    }
}
