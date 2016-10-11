import constants.Constants;
import errorHandle.ErrorHandler;
import junit.framework.TestCase;
import launch.Runner;
import launch.Setup;
import org.awaitility.Awaitility;
import utilities.Utilities;

import javax.rmi.CORBA.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * Wait for directoryNull flag to be set to true.
     * @param runnerHelperThread thread reference for thread running.
     * @return true or timeout.
     */
    private Callable<Boolean> waitForDirectoryNull(RunnerHelperThread runnerHelperThread){
        return () -> runnerHelperThread.directoryNull;
    }

    /**
     * Wait for copyComplete flag to be set to true.
     * @param runnerHelperThread thread reference for thread running.
     * @return true or timeout.
     */
    private Callable<Boolean> waitForCopyComplete(RunnerHelperThread runnerHelperThread){
        return () -> runnerHelperThread.copyComplete;
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
     * Test case where the directory should be reported null.
     * Early exit from runner.main.
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

    /**
     * Test case where copy is completed correctly.
     */
    public void testRunnerRenameCopyComplete(){
        String testDirectory = testDir;
        Setup.setupSettingsFile(testDirectory + "\\" + Constants.SETTINGS_FILE);
        Map<String, String> settingsMap = new HashMap<>();
        settingsMap.put(Constants.DEFAULT_RENAME_DIRECTORY, testDirectory+"\\test");
        settingsMap.put(Constants.DEFAULT_COPY_DIRECTORY, testDirectory+"\\copy");
        settingsMap.put(Constants.USER_INTERACTION, Constants.FALSE);
        settingsMap.put(Constants.COPY_FILES_FLAG, Constants.TRUE);
        settingsMap.put(Constants.MEDIA_DIVISION, Constants.TRUE);
        settingsMap.put(Constants.ERROR_HANDLER, Constants.TRUE);
        HelperMethodsTest.generateSettingsFileFromMap(testDirectory, settingsMap);
        HelperMethodsTest.generateTestSettingsFiles(testDirectory);
        ErrorHandler.printOutToFile(testDirectory+"\\mediaDivision.txt","Tokyo Ghoul: Anime");
        byte[] buff = new byte[2048];
        ByteArrayInputStream myIn = new ByteArrayInputStream(buff);
        System.setIn(myIn);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream myOut = new PrintStream(outputBuffer);
        System.setOut(myOut);
        RunnerHelperThread myRunner = new RunnerHelperThread(myIn,outputBuffer);
        Thread runnerHelper = new Thread(myRunner);
        runnerHelper.start();
        String title = "Tokyo Ghoul";
        HelperMethodsTest.generateTestDirectory(testDir, 1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{testDir});
        Awaitility.await().until(waitForCopyComplete(myRunner));
        assertTrue(myRunner.copyComplete);
        assertTrue(Utilities.fileExists(testDir+"\\copy\\Anime\\Tokyo Ghoul\\Tokyo Ghoul S01E01.mkv"));
    }
}