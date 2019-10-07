package fi.meliora.testlab.ext.maven;

import fi.meliora.testlab.ext.crest.CrestEndpointFactory;
import fi.meliora.testlab.ext.crest.TestResultResource;
import fi.meliora.testlab.ext.rest.model.AddTestResultResponse;
import fi.meliora.testlab.ext.rest.model.KeyValuePair;
import fi.meliora.testlab.ext.rest.model.TestResult;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.XmlStreamReader;

import java.io.File;
import java.util.*;

/**
 * Maven plugin Mojo to publish testing results to Meliora Testlab.
 *
 * For documentation of this plugin please see https://www.melioratestlab.com/maven-plugin/
 *
 * @author Meliora Ltd
 */
@Mojo(name = "publish", defaultPhase = LifecyclePhase.VERIFY)
public class TestResultMojo extends AbstractMojo {
    public static final String FORMAT_JUNIT = "junit";
    public static final String FORMAT_ROBOT = "robot";

    /**
     * Company ID of the hosted Testlab you want to publish the results to. If your testlab is accessed from
     * mytestlab.melioratestlab.com your Company ID most likely is "mytestlab".
     */
    @Parameter
    private String companyId;
    /**
     * Api key to use in communicating with the Testlab you want to publish the results to.
     * An api key must be set for security on your Company settings at Testlab side.
     */
    @Parameter(required = true)
    private String apiKey;
    /**
     * For on-premises installation, a full URL address (such as 'https://testlab.mycompany.com')
     * of your Testlab must be set.
     * <br/>
     * <ul>
     * <li>For on-premise installations on your own server: set onpremiseUrl</li>
     * <li>For hosted Testlab: set Company ID</li>
     * </ul>
     */
    @Parameter
    private String onpremiseUrl;

    /**
     * Title of the Testlab's custom field to use to map the test cases to unit test methods.
     * <br/>
     * To make it possible to push unit test results to Testlab the
     * unit test class.method name must be mapped to a test case in your Testlab project:
     * <br/>
     * <ol>
     * <li>
     * Configure a new "text" custom field to your project at Testlab side. Give
     * the field a human readable title such as "JUnit test".
     * </li>
     * <li>
     * Update your test cases at Testlab side and enter proper JUnit test
     * class+method name pairs for your test cases.
     * </li>
     * <li>
     * Enter "JUnit test" to this parameter in plugin configuration. This way when
     * unit tests in the build fail they will be mapped to test cases with
     * matching "JUnit test" names at Testlab side. When publishing Testlab
     * will peek the set custom field of a test case and if it matches the class.method name
     * of a failed unit test the test case will be marked as failed.
     * </li>
     * </ol>
     */
    @Parameter(required = true)
    private String testCaseMappingField;

    /**
     * The key of the Testlab project you want to publish the results to.
     */
    @Parameter(required = true)
    private String projectKey;

    /**
     * The name of the Testlab test run to create or update.
     * <br/>
     * If a test run with same name already exists in Testlab it will be always replaced with
     * test results when this plugin is run. If it does not it will be created.
     */
    @Parameter(required = true)
    private String testRun;
    /**
     * The "created by" user name of the test run created or updated. Defaults to "Maven".
     */
    @Parameter
    private String testRunUser;

    /**
     * A comment to be set for the Testlab test run to create or update.
     * <br/>
     * Hint: You can use Maven's properties in all the configuration variables
     * and in the testRunComment field this is especially handy. Use the
     * <code>project.*</code>, <code>settings.*</code> and <code>env.*</code>
     * variables to decorate your configuration as needed. The variables
     * are referenced in typical format such as ${project.version}.
     */
    @Parameter
    private String testRunComment;
    /**
     * Milestone in Testlab the results and issues are bound to. Milestone <b>must exist</b>
     * in the project. If milestone does not exist the results and issues are added without
     * the milestone binding.
     * <br/>
     * You can enter an identifier of a milestone or a title of a milestone. Identifier
     * is matched first.
     */
    @Parameter
    private String milestone;
    /**
     * Version in Testlab the results and issues are bound to. If version does not exist
     * in Testlab it is added to the project.
     */
    @Parameter
    private String version;
    /**
     * Test environment in Testlab the results and issues are bound to. If environment does not exist
     * in Testlab it is added to the project.
     */
    @Parameter
    private String testEnvironment;
    /**
     * Tags to be set for the Testlab test run.
     * <br/>
     * When updating runs, if the plugin sends even one tag all existing tags in the run will be replaced. Leaving
     * this parameter unset leaves the tags at Testlab side untouched.
     * <br/>
     * <pre>
     * &lt;tags&gt;
     *     &lt;tag&gt;maven&lt;/tag&gt;
     *     &lt;tag&gt;automated&lt;/tag&gt;
     * &lt;/tags&gt;
     * </pre>
     */
    @Parameter
    private List tags;
    /**
     * Test cases in Testlab can include parameters in the content as ${PARAM1} formatted tags. Normally,
     * these tags are set and replaced with values set during the test case execution or execution planning
     * phase. Set this configuration map to send the parameter values for test case parameters.
     * <br/>
     * <pre>
     * &lt;parameters&gt;
     *     &lt;UI&gt;Desktop&lt;/UI&gt;
     *     &lt;BROWSER&gt;Firefox&lt;/BROWSER&gt;
     * &lt;/parameters&gt;
     * </pre>
     */
    @Parameter
    private Map parameters;

    /**
     * If true, issues will be added to the Testlab project for failing tests.
     */
    @Parameter
    private boolean addIssues;
    /**
     * When set to Testlab's user id value for an user who has a role in the set project
     * the added issue(s) are automatically assigned to this user.
     */
    @Parameter
    private String assignIssuesToUser;
    /**
     * The plugin will create the issues with the title set to the name of the failing
     * test class. When this setting is set the plugin tries to
     * reopen existing issues in the project by matching the issue's title.
     * <br/>
     * When finding existing issues if
     * <br/>
     * <ul>
     * <li>a single resolved or closed issue is found it is reopened and updated,</li>
     * <li>a single still non-resolved issue is found it is updated,</li>
     * <li>multiple issues are found and the latest issue is resolved or closed it is reopened and updated,</li>
     * <li>multiple issues are found and the latest issue is still non-resolved it is updated.</li>
     * </ul>
     * <br/>
     * Otherwise a new issue is added.
     * <br/>
     * When reopening an issue version, environment, description and assigned to -fields are
     * updated to match the plugin sent values. When updating a found non-resolved issue
     * only description is updated to plugin sent value.
     */
    @Parameter
    private boolean reopenExistingIssues;
    /**
     * If set, added issues are merged to a single issue. Checking this option
     * results always in a maximum of a single issue added per plugin execution.
     */
    @Parameter
    private boolean mergeAsSingleIssue;

    /**
     * If set, automatically creates test cases when publishing.
     */
    @Parameter
    private boolean importTestCases;

    /**
     * If set, sets the root category path where the test cases are created. By default, "Import".
     */
    @Parameter
    private String importTestCasesRootCategory;

    /**
     * File resource patterns to find the xUnit's .xml files or Robot Framework's output.xml file to push the
     * actual results from.
     * <br/>
     * <pre>
     * &lt;resources&gt;
     *     &lt;resource&gt;
     *         &lt;directory&gt;target/surefire-reports&lt;/directory&gt;
     *         &lt;includes&gt;
     *             &lt;include&gt;**&#47;*.xml&lt;/include&gt;
     *         &lt;/includes&gt;
     *         &lt;excludes&gt;
     *             &lt;exclude&gt;**&#47;junk&#47;**&lt;/exclude&gt;
     *         &lt;/excludes&gt;
     *     &lt;/resource&gt;
     * &lt;/resources&gt;
     * </pre>
     */
    @Parameter(required = true)
    private List<Resource> resources;

    /**
     * If set, the build will not be marked as FAILURE if any failures are detected.
     *
     * <strong>
     * Note that this plugin marks the build as failed only by inspecting the results
     * of the resources configured.
     * </strong>
     */
    @Parameter
    private boolean ignoreFailedTests;

    /**
     * If set, the results will be always published ignoring the possible -Dtest and -Dit.test
     * parameters.
     * <br/>
     * By default this option is "false". This means, that if a test or subset of tests
     * are specified by -Dtest or -Dit.test parameters on command line, the (probably partial) results
     * will <strong>not</strong> be published to Testlab. Set this option to "true" to ignore
     * these properties and always publish the results to Testlab.
     */
    @Parameter
    private boolean forcePublish;

    /**
     * Accessor for Surefire's test parameter.
     */
    @Parameter(property = "test")
    private String surefireTest;

    /**
     * Accessor for Failsafe's test parameter.
     */
    @Parameter(property = "it.test")
    private String failsafeTest;

    /**
     * Format of the result resources to publish. "junit" for standard xUnit formatted files and
     * "robot" for Robot Framework's output files. Defaults to "junit".
     */
    @Parameter
    private String format = FORMAT_JUNIT;

    /**
     * If true, when the xml file containing the results is in Robot Framework format, and in the
     * xml keyword has sub keywords, the sub keywords are catenated
     * to a single step in the result. For example, if the robot result has
     * <br>
     * <pre>
     *     &lt;kw name="Open site"&gt;
     *         &lt;kw name="Open URL"&gt;
     *             &lt;kw name="Navigate browser"&gt;
     *                 ...
     *             &lt;/kw&gt;
     *         &lt;/kw&gt;
     *     &lt;/kw&gt;
     *     ...
     * </pre>
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
    @Parameter
    private boolean robotCatenateParentKeywords = true;

    /**
     * Executes this Mojo.
     *
     * @throws MojoExecutionException on exception
     * @throws MojoFailureException on failure
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Publishing test results to Testlab ...");

        getLog().debug("- Running with configuration: " + this);

        //// validate configuration

        if(StringUtils.isBlank(apiKey)) {
            fail("configuration error: apiKey must be set.");
        }

        if(StringUtils.isBlank(companyId) && StringUtils.isBlank(onpremiseUrl)) {
            fail("configuration error: companyId or onpremiseUrl must be set.");
        }

        if(StringUtils.isBlank(testCaseMappingField)) {
            fail("configuration error: testCaseMappingField must be set.");
        }

        if(StringUtils.isBlank(projectKey)) {
            fail("configuration error: projectKey must be set.");
        }

        if(StringUtils.isBlank(testRun)) {
            fail("configuration error: testRun must be set.");
        }

        if(resources == null || resources.size() == 0) {
            fail("configuration error: resources must be set.");
        }

        if(!StringUtils.isBlank(format) && !Arrays.asList(FORMAT_JUNIT, FORMAT_ROBOT).contains(format)) {
            fail("configuration error: invalid format " + format + " - must be one of: " + FORMAT_JUNIT + " | " + FORMAT_ROBOT);
        }

        //// check if the publish should continue or not
        boolean doPublish = true;
        if(!forcePublish) {
            String skipPublishMessage = null;

            if(!StringUtils.isBlank(surefireTest)) {
                skipPublishMessage = "Surefire plugin's 'test' option set - not publishing results. " +
                        "Set forcePublish to true in plugin configuration to always publish results.";
            } else if(!StringUtils.isBlank(failsafeTest)) {
                skipPublishMessage = "Failsafe plugin's 'it.test' option set - not publishing results. " +
                        "Set forcePublish to true in plugin configuration to always publish results.";
            }

            if(skipPublishMessage != null) {
                getLog().warn(skipPublishMessage);
                doPublish = false;
            }
        }

        if(doPublish) {
            //// find resources to publish

            Set<File> publishFiles = new LinkedHashSet<File>();
            if(resources != null) {
                for(Resource r : resources) {
                    getLog().debug("- Scanning directory: " + r);
                    ResourceDirectoryScanner scanner = new ResourceDirectoryScanner(
                            r.getDirectory(), r.getIncludes(), r.getExcludes()
                    );
                    publishFiles.addAll(scanner.getIncludedFiles());
                }
            }

            getLog().info("Files to publish: " + publishFiles);

            if(publishFiles.size() == 0) {
                getLog().warn("No resource files resolved. Not publishing results.");
            } else {

                //// let's publish

                TestResult data = new TestResult();
                data.setStatus(fi.meliora.testlab.ext.rest.model.TestResult.STATUS_FINISHED);
                data.setProjectKey(projectKey);
                data.setTestRunTitle(testRun);
                // note: we send the set milestone in both fields as backend logic tries first with identifier and fallbacks to title
                data.setMilestoneIdentifier(milestone);
                data.setMilestoneTitle(milestone);
                data.setAddIssues(addIssues);
                data.setMergeAsSingleIssue(mergeAsSingleIssue);
                data.setReopenExistingIssues(reopenExistingIssues);
                data.setAssignIssuesToUser(assignIssuesToUser);
                data.setTestCaseMappingField(testCaseMappingField);
                data.setUser(StringUtils.isBlank(testRunUser) ? "Maven" : testRunUser);
                data.setComment(testRunComment);
                data.setTestTargetTitle(version);
                data.setTestEnvironmentTitle(testEnvironment);
                data.setImportTestCases(importTestCases);
                if(StringUtils.isBlank(importTestCasesRootCategory))
                    importTestCasesRootCategory = "Import";
                data.setImportTestCasesRootCategory(importTestCasesRootCategory);
                data.setXmlFormat(format);
                data.setRobotCatenateParentKeywords(robotCatenateParentKeywords);

                if(tags != null && tags.size() > 0) {
                    data.setTags(StringUtils.join(tags.toArray(), " "));
                }

                if(parameters != null && parameters.size() > 0) {
                    List<KeyValuePair> parameterValues = new ArrayList<KeyValuePair>();
                    for(Object name : parameters.keySet()) {
                        KeyValuePair kvp = new KeyValuePair();
                        kvp.setKey(String.valueOf(name));
                        kvp.setValue(String.valueOf(parameters.get(name)));
                        parameterValues.add(kvp);
                    }
                    data.setParameters(parameterValues);
                }

                boolean hasFailures = false, hasErrors = false;

                StringBuilder sb = new StringBuilder();
                if(FORMAT_JUNIT.equals(format)) {
                    //// combine all test suites to a single one if multiple xml result files are present

                    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                    sb.append("<testsuites>\n");

                    for(File f : publishFiles) {
                        XmlStreamReader r = null;
                        try {
                            r = ReaderFactory.newXmlReader(f);
                            String fileXml = IOUtils.toString(r);

                            int s = fileXml.indexOf("<");
                            int e = fileXml.length();
                            if(s > -1) {
                                String l = fileXml.toLowerCase();
                                if(s == l.indexOf("<?xml")) {
                                    // skip prolog
                                    s = l.indexOf("<", s + 1);
                                }
                                if(s == l.indexOf("<testsuites")) {
                                    // drop "testsuites"
                                    s = l.indexOf("<testsuite", s + 1);
                                    e = l.indexOf("</testsuites>");
                                }
                                fileXml = fileXml.substring(s, e);

                                getLog().debug("Test suites from file " + f + ":\n " + fileXml);

                                if(fileXml.trim().length() > 0) {
                                    sb.append(fileXml);

                                    // try to detect failures or errors
                                    l = fileXml.toLowerCase();
                                    s = l.indexOf("<testsuite ");
                                    if(s > -1) {
                                        e = l.indexOf(">", s);
                                        if(e > -1) {
                                            String testsuiteLine = l.substring(s, e);
                                            if(testsuiteLine.contains(" failures=") && !testsuiteLine.contains("failures=\"0\"")) {
                                                hasFailures = true;
                                            }
                                            if(testsuiteLine.contains(" errors=") && !testsuiteLine.contains("errors=\"0\"")) {
                                                hasErrors = true;
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception fe) {
                            getLog().error(fe);
                        } finally {
                            if(r != null)
                                try { r.close(); } catch (Exception ee) {}
                        }
                    }

                    sb.append("\n</testsuites>");
                } else if(FORMAT_ROBOT.equals(format)) {
                    if(publishFiles.size() > 1) {
                        getLog().error("Resource pattern matches to multiple files. When publishing Robot Framework results, only a single output.xml file is supported.");
                    }
                    XmlStreamReader r = null;
                    try {
                        r = ReaderFactory.newXmlReader(publishFiles.iterator().next());
                        String fileXml = IOUtils.toString(r);

                        if(fileXml.contains("status=\"FAIL\"")) {
                            hasFailures = true;
                        }

                        sb.append(fileXml);
                    } catch (Exception fe) {
                        getLog().error(fe);
                    } finally {
                        if(r != null)
                            try { r.close(); } catch (Exception ee) {}
                    }
                }

                data.setXml(sb.toString());

                getLog().info("Sending results ...");
                getLog().debug("Data:\n" + data);

                AddTestResultResponse response = CrestEndpointFactory.getInstance().getTestlabEndpoint(
                        companyId, onpremiseUrl, apiKey, TestResultResource.class
                ).addTestResult(data);

                getLog().debug("Response from Testlab: " + response);
                getLog().info("Results sent.");

                if(hasFailures || hasErrors) {
                    getLog().warn("Results report " + (hasFailures && hasErrors ? "failures and errors." : hasFailures ? "failures." : "errors."));
                    if(!ignoreFailedTests) {
                        throw new MojoFailureException(
                                "Results were published but there are test failures. Please refer to your " +
                                        "test runner plugin's reports for results."
                        );
                    }
                }
            }
        }
    }

    // fails the build with error msg
    protected void fail(String msg) throws MojoFailureException {
        msg += " Hint: Run with -X to get more debug output.";
        getLog().error(msg);
        throw new MojoFailureException(msg);
    }

    @Override
    public String toString() {
        return "fi.meliora.testlab.ext.maven.TestResultMojo{" +
                "addIssues=" + addIssues +
                ", companyId='" + companyId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", onpremiseUrl='" + onpremiseUrl + '\'' +
                ", testCaseMappingField='" + testCaseMappingField + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", testRun='" + testRun + '\'' +
                ", testRunUser='" + testRunUser + '\'' +
                ", testRunComment='" + testRunComment + '\'' +
                ", milestone='" + milestone + '\'' +
                ", version='" + version + '\'' +
                ", testEnvironment='" + testEnvironment + '\'' +
                ", tags=" + tags +
                ", parameters=" + parameters +
                ", assignIssuesToUser='" + assignIssuesToUser + '\'' +
                ", reopenExistingIssues=" + reopenExistingIssues +
                ", mergeAsSingleIssue=" + mergeAsSingleIssue +
                ", importTestCases=" + importTestCases +
                ", importTestCasesRootCategory='" + importTestCasesRootCategory + '\'' +
                ", resources=" + resources +
                ", ignoreFailedTests=" + ignoreFailedTests +
                ", forcePublish=" + forcePublish +
                ", format=" + format +
                ", robotCatenateParentKeywords=" + robotCatenateParentKeywords +
                "}";
    }
}
