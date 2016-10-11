import constants.Constants;
import junit.framework.TestCase;
import launch.Runner;
import org.awaitility.Awaitility;
import utilities.Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

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

    private Callable<Boolean> waitForDirectoryNull(RunnerHelperThread runnerHelperThread){
        return () -> runnerHelperThread.directoryNull;
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

    /**
     * Test case where user is prompted to verify what should be used for the
     * renaming of a file. This test assumes the algorithm is correct.
     */
    public void testRunnerRenameDirectoryNull(){
        byte[] buff = new byte[2048];
        ByteArrayInputStream myIn = new ByteArrayInputStream(buff);
        System.setIn(myIn);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream myOut = new PrintStream(outputBuffer);
        System.setOut(myOut);
        RunnerHelperThread myRunner = new RunnerHelperThread(myIn,outputBuffer);
        Thread runnerHelper = new Thread(myRunner);
        runnerHelper.start();
        Runner.main(new String[]{testDir});
        Awaitility.await().until(waitForDirectoryNull(myRunner));
        assertTrue(myRunner.directoryNull);
    }
}
