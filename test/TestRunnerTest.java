import constants.Constants;
import junit.framework.TestCase;
import launch.Runner;
import utilities.Utilities;

/**
 */
public class TestRunnerTest extends TestCase {
    /**Default test directory for testing*/
    private String testDir = HelperMethodsTest.TESTDIR;

    /**
     * Ensure the test directory is clean for next test.
     * @throws Exception if tear down fails.
     */
    public void tearDown() throws Exception{
        HelperMethodsTest.destroyTestDirectory(testDir);
        super.tearDown();
    }

    /**
     * Test case where the runner must create a set of default
     * settings files.
     */
    public void testRunnerCreatesSettingFiles(){
        Runner.main(new String[]{testDir});
        assertTrue(Utilities.fileExists(testDir+"\\"+ Constants.SETTINGS_FILE));
        assertTrue(Utilities.fileExists(testDir+"\\"+ Constants.SPECIAL_RENAME_CASES_FILE));
        assertTrue(Utilities.fileExists(testDir+"\\"+ Constants.SPECIAL_EP_CASES_FILE));
        assertTrue(Utilities.fileExists(testDir+"\\"+ Constants.MEDIA_DIVISION_FILE));
    }
}
