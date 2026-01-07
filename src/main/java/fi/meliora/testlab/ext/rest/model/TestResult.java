package fi.meliora.testlab.ext.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Encapsulates inbound results of a single test run.
 *
 * @author Marko Kanala
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResult extends ModelObject {

    public static final String FORMAT_JUNIT = "junit";
    public static final String FORMAT_ROBOTFRAMEWORK = "robot";

    public enum AddIssueStrategy {
        DONOTADD,
        ADDPERTESTRUN,
        ADDPERTESTCASE,
        ADDPERRESULT,
        RULESET_DEFAULT //handle as null
    }

    @XmlElement
    private Long projectId;
    @XmlElement
    private String projectKey;

    @XmlElement
    private String ruleset;

    @XmlElement
    private Long automationSourceId;

    @XmlElement
    private String automationSourceTitle;

    @XmlElement
    private Long testRunId;
    @XmlElement
    private String testRunTitle;

    @XmlElement
    private String description;

    @XmlElement
    private String user;

    @XmlElement
    private Long milestoneId;
    @XmlElement
    private String milestoneIdentifier;
    @XmlElement
    private String milestoneTitle;

    @XmlElement
    private Long testTargetId;
    @XmlElement
    private String testTargetTitle;

    @XmlElement
    private Long testEnvironmentId;
    @XmlElement
    private String testEnvironmentTitle;

    @XmlElement
    private String tags;

    @XmlElement(type = KeyValuePair.class)
    private List<KeyValuePair> parameters;

    @XmlElement(type = TestCaseResult.class)
    private List<TestCaseResult> results;

    @XmlElement
    private String xml;

    @XmlElement
    private String xmlFormat;       // junit | robot, defaults to junit

    // control fields

    @Deprecated
    @XmlElement
    private String testCaseMappingField;

    @XmlElement
    private AddIssueStrategy addIssueStrategy;
    @XmlElement
    private Boolean reopenExistingIssues;
    @XmlElement
    private String assignIssuesToUser;

    @Deprecated
    @XmlElement
    private Boolean importTestCases;
    @Deprecated
    @XmlElement
    private String importTestCasesRootCategory;

    @XmlElement
    private Boolean robotCatenateParentKeywords = true;

    /**
     * Optional. If set, this will be used as a name for the results (file) added to Testlab.
     * For example, this can be set as an URL (for example Jenkins job URL) or the name of the
     * result file you are pushing the results from.
     */
    @XmlElement
    private String resultName;

    /**
     * Culprits for the possible failure, if any.
     */
    @XmlElement
    private List<String> culprits;

    /**
     * Changesets the test result relates to. For Testlab to react to these values, the
     * VCS integration to be set up for the project for the Changeset to exist in the Testlab's
     * database. If corresponding Changeset does not exist in Testlab, these values are ignored.
     */
    @XmlElement(type = Changeset.class)
    private List<Changeset> changesets;

    public Long getProjectId() {
        return projectId;
    }

    /**
     * Id of the project to add the test results to. If null, projectKey is used.
     *
     * @param projectId projectId
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Key of the project to add the test results to. Optional if projectId is set.
     *
     * @param projectKey project prefix / key
     */
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getRuleset() {
        return ruleset;
    }

    /**
     * Name of the ruleset to apply to these results. Test result rulesets are configured in the
     * "Test automation" UI in Testlab. If not set, a default ruleset for the project is used.
     *
     * @param ruleset name of the ruleset
     */
    public void setRuleset(String ruleset) {
        this.ruleset = ruleset;
    }

    public Long getTestRunId() {
        return testRunId;
    }

    /**
     * Id of the test run to update the test results to. If null, testRunTitle is used.
     *
     * @param testRunId test run id
     */
    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }

    public String getTestRunTitle() {
        return testRunTitle;
    }

    /**
     * Title of the test run to update the test results to. Optional if testRunId is set.
     *
     * @param testRunTitle test run title
     */
    public void setTestRunTitle(String testRunTitle) {
        this.testRunTitle = testRunTitle;
    }

    public void setAutomationSourceId(Long automationSourceId) { this.automationSourceId = automationSourceId; }
    public Long getAutomationSourceId() { return automationSourceId; }

    /**
     * @param automationSourceTitle Source of test results
     */
    public void setAutomationSourceTitle(String automationSourceTitle) { this.automationSourceTitle = automationSourceTitle; }
    public String getAutomationSourceTitle() { return automationSourceTitle; }

    public String getDescription() {
        return description;
    }

    /**
     * Description for the test run. If test run exists and description is left as null the description
     * of TestRun at Testlab is left as it is.
     *
     * @param description comment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    /**
     * Name of the user to execute Testlab operations with.
     *
     * @param user user name
     */
    public void setUser(String user) {
        this.user = user;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    /**
     * Id of the milestone for the test run and issues. If null, milestoneIdentifier and milestoneTitle are used.
     *
     * @param milestoneId milestone id
     */
    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getMilestoneIdentifier() {
        return milestoneIdentifier;
    }

    /**
     * Identifier of the milestone for the test run and issues. If null, milestoneTitle is used.
     *
     * @param milestoneIdentifier milestone identifier
     */
    public void setMilestoneIdentifier(String milestoneIdentifier) {
        this.milestoneIdentifier = milestoneIdentifier;
    }

    public String getMilestoneTitle() {
        return milestoneTitle;
    }

    /**
     * Title of the milestone for the test run and issues.
     *
     * @param milestoneTitle milestone title
     */
    public void setMilestoneTitle(String milestoneTitle) {
        this.milestoneTitle = milestoneTitle;
    }

    public Long getTestTargetId() {
        return testTargetId;
    }

    /**
     * Id of the test target (version) for the test run. If null, testTargetTitle is used.
     *
     * @param testTargetId test target id
     */
    public void setTestTargetId(Long testTargetId) {
        this.testTargetId = testTargetId;
    }

    public String getTestTargetTitle() {
        return testTargetTitle;
    }

    /**
     * Title of the test target (version) for the test run. Optional if testTargetId is set.
     *
     * @param testTargetTitle test target title
     */
    public void setTestTargetTitle(String testTargetTitle) {
        this.testTargetTitle = testTargetTitle;
    }

    public Long getTestEnvironmentId() {
        return testEnvironmentId;
    }

    /**
     * Id of the test environment for the test run. If null, testEnvironmentTitle is used.
     *
     * @param testEnvironmentId test environment id
     */
    public void setTestEnvironmentId(Long testEnvironmentId) {
        this.testEnvironmentId = testEnvironmentId;
    }

    public String getTestEnvironmentTitle() {
        return testEnvironmentTitle;
    }

    /**
     * Title of the test environment for the test run. Optional if testEnvironmentId is set.
     *
     * @param testEnvironmentTitle test environment title
     */
    public void setTestEnvironmentTitle(String testEnvironmentTitle) {
        this.testEnvironmentTitle = testEnvironmentTitle;
    }

    public String getTags() {
        return tags;
    }

    /**
     * Tags for the test run. Optional. Separate multiple tags with spaces ("tag1 tag2 tag3 ...").
     *
     * @param tags a list of tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Deprecated
    public String getTestCaseMappingField() {
        return testCaseMappingField;
    }

    /**
     * Title of the custom field to use to map the test cases from the project. Optional
     * if results are sent with actual testCaseId values set.
     *
     * This value is case-insensitive.
     *
     * @param testCaseMappingField test case mapping field
     */
    @Deprecated
    public void setTestCaseMappingField(String testCaseMappingField) {
        this.testCaseMappingField = testCaseMappingField;
    }

    public List<TestCaseResult> getResults() {
        return results;
    }

    /**
     * Values to set to test case parameters if any.
     *
     * @return parameters
     */
    public List<KeyValuePair> getParameters() {
        return parameters;
    }

    public void setParameters(List<KeyValuePair> parameters) {
        this.parameters = parameters;
    }

    /**
     * Results of individual test cases.
     *
     * @param results results
     */
    public void setResults(List<TestCaseResult> results) {
        this.results = results;
    }

    public AddIssueStrategy getAddIssueStrategy() {
        return addIssueStrategy;
    }

    /**
     * When to add issues for failed results. Never, one issue per test run, one issue per Testlab test case, or one issue per test result
     * @param addIssueStrategy TestResult.AddIssueStrategy
     */
    public void setAddIssueStrategy(AddIssueStrategy addIssueStrategy) {
        this.addIssueStrategy = addIssueStrategy;
    }

    public Boolean getReopenExistingIssues() {
        return reopenExistingIssues;
    }

    /**
     * Set to true to reopen existing issues in Testlab if found.
     *
     * @param reopenExistingIssues Boolean
     */
    public void setReopenExistingIssues(Boolean reopenExistingIssues) {
        this.reopenExistingIssues = reopenExistingIssues;
    }

    public String getAssignIssuesToUser() {
        return assignIssuesToUser;
    }

    /**
     * Assign added issues to this user if the user is found in the project.
     *
     * @param assignIssuesToUser boolean
     */
    public void setAssignIssuesToUser(String assignIssuesToUser) {
        this.assignIssuesToUser = assignIssuesToUser;
    }

    public String getXml() {
        return xml;
    }

    /**
     * JUnit compatible xml content for results. If results are delivered in results
     * field this field is ignored.
     *
     * @param xml xml
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    @Deprecated
    public Boolean isImportTestCases() {
        return importTestCases;
    }

    /**
     * If set implies that test cases which are not found via the mapping identifier
     * should be automatically created during the push.
     *
     * @param importTestCases boolean
     */
    @Deprecated
    public void setImportTestCases(Boolean importTestCases) {
        this.importTestCases = importTestCases;
    }

    @Deprecated
    public String getImportTestCasesRootCategory() {
        return importTestCasesRootCategory;
    }

    /**
     * If set, sets the root category path where the test cases are created. By default, "Import".
     *
     * @param importTestCasesRootCategory boolean
     */
    @Deprecated
    public void setImportTestCasesRootCategory(String importTestCasesRootCategory) {
        this.importTestCasesRootCategory = importTestCasesRootCategory;
    }

    public String getXmlFormat() {
        return xmlFormat;
    }

    /**
     * Format for provided xml file. "junit" | "robot" - defaults to "junit".
     *
     * @param xmlFormat format
     */
    public void setXmlFormat(String xmlFormat) {
        this.xmlFormat = xmlFormat;
    }

    public Boolean isRobotCatenateParentKeywords() {
        return robotCatenateParentKeywords;
    }

    /**
     * If true, when the xml provided is in Robot Framework format and in the
     * xml keyword has sub keywords, the sub keywords are catenated
     * to a single step in the result. For example, if the robot result has
     * <br>
     * &lt;pre&gt;
     *     &lt;kw name="Open site"&gt;
     *         &lt;kw name="Open URL"&gt;
     *             &lt;kw name="Navigate browser"&gt;
     *                 ...
     *             &lt;/kw&gt;
     *         &lt;/kw&gt;
     *     &lt;/kw&gt;
     *     ...
     * &lt;/pre&gt;
     * <br>
     * .. the test case is added with a single step described as "Open site - Open URL - Navigate browser".
     * When concatenating, if a step fails it is always included as a step.
     * <br>
     * If false, each sub keyword will generate a separate step to the result.
     * <br>
     * This value defaults to true.
     *
     * @param robotCatenateParentKeywords boolean
     */
    public void setRobotCatenateParentKeywords(Boolean robotCatenateParentKeywords) {
        this.robotCatenateParentKeywords = robotCatenateParentKeywords;
    }

    public String getResultName() { return resultName; }

    /**
     * Optional. If set, this will be used as a name for the results (file) added to Testlab.
     * For example, this can be set as an URL (for example Jenkins job URL) or the name of the
     * result file you are pushing the results from.
     *
     * @param resultName result name
     */
    public void setResultName(String resultName) { this.resultName = resultName; }

    public List<String> getCulprits() {
        return culprits;
    }

    /**
     * Culprits for the possible failure, if any.
     *
     * @param culprits culprits
     */
    public void setCulprits(List<String> culprits) {
        this.culprits = culprits;
    }

    public List<Changeset> getChangesets() {
        return changesets;
    }

    /**
     * Changesets the test result relates to.
     *
     * For Testlab to react to these values, the
     * VCS integration to be set up for the project for the Changeset to exist in the Testlab's
     * database. If corresponding Changeset does not exist in Testlab, these values are ignored.
     *
     * @param changesets changesets
     */
    public void setChangesets(List<Changeset> changesets) {
        this.changesets = changesets;
    }
}
