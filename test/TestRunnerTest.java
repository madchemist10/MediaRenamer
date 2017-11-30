import constants.Constants;
import errorHandle.ErrorHandler;
import junit.framework.TestCase;
import launch.Runner;
import launch.Setup;
import org.awaitility.Awaitility;
import utilities.Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class TestRunnerTest extends TestCase {
    /**Default test directory for testing*/
    private String testDir = HelperMethodsTest.TESTDIR;
    /**Lock for ensuring one unit test executes at a time.*/
    private Lock sequential = new ReentrantLock();


    /**
     * Acquire the lock to begin test execution.
     * @throws Exception if setup failed.
     */
    public void setUp() throws Exception{
        sequential.lock();
        super.setUp();
    }

    /**
     * Ensure the test directory is clean for next test.
     * Release the lock to stop test execution.
     * @throws Exception if tear down fails.
     */
    public void tearDown() throws Exception{
//        HelperMethodsTest.destroyTestDirectory(testDir);
        super.tearDown();
        sequential.unlock();
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
     * Wait for copyComplete flag to be set to true.
     * @param runnerHelperThread thread reference for thread running.
     * @return true or timeout.
     */
    private Callable<Boolean> waitForNoFilesToRename(RunnerHelperThread runnerHelperThread){
        return () -> runnerHelperThread.noFilesToRename;
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
        myRunner.setWaitCondition(RunnerHelperThread.Wait.DIRECTORY_NULL);
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
        ErrorHandler.printOutToFile(testDirectory+"\\mediaDivision.txt","One Punch Man: Anime");
        byte[] buff = new byte[2048];
        ByteArrayInputStream myIn = new ByteArrayInputStream(buff);
        System.setIn(myIn);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream myOut = new PrintStream(outputBuffer);
        System.setOut(myOut);
        RunnerHelperThread myRunner = new RunnerHelperThread(myIn,outputBuffer);
        myRunner.setWaitCondition(RunnerHelperThread.Wait.COPY_COMPLETE);
        Thread runnerHelper = new Thread(myRunner);
        runnerHelper.start();
        String title = "One Punch Man";
        HelperMethodsTest.generateTestDirectory(testDir, 1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{testDir});
        Awaitility.await().until(waitForCopyComplete(myRunner));
        assertTrue(myRunner.copyComplete);
        assertTrue(Utilities.fileExists(testDir+"\\copy\\Anime\\One Punch Man\\One Punch Man S01E01.mkv"));
    }

    /**
     * Test the program execution with no files to be copied.
     */
    public void testCopyWithNoFiles(){
        HelperMethodsTest.generateTestSettingsFiles(testDir);
        Utilities.makeDirectory(testDir+"\\copy");
        Utilities.makeDirectory(testDir+"\\test");
        byte[] buff = new byte[2048];
        ByteArrayInputStream myIn = new ByteArrayInputStream(buff);
        System.setIn(myIn);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream myOut = new PrintStream(outputBuffer);
        System.setOut(myOut);
        RunnerHelperThread myRunner = new RunnerHelperThread(myIn,outputBuffer);
        myRunner.setWaitCondition(RunnerHelperThread.Wait.NO_FILES_TO_RENAME);
        Thread runnerHelper = new Thread(myRunner);
        runnerHelper.start();
        Runner.main(new String[]{testDir});
        Awaitility.await().until(waitForNoFilesToRename(myRunner));
        assertTrue(myRunner.noFilesToRename);
    }


    /**
     * Test to validate that tv shows and anime may be separated
     * via the default copy directory tag.
     */
    public void testCopyDifferentOutputs(){
        String testDirectory = testDir;
        Setup.setupSettingsFile(testDirectory + "\\" + Constants.SETTINGS_FILE);
        Map<String, String> settingsMap = new HashMap<>();
        String copyDir = "Anime-"+testDirectory+"\\a;TVShows-"+testDirectory+"\\b";
        settingsMap.put(Constants.DEFAULT_RENAME_DIRECTORY, testDirectory+"\\test");
        settingsMap.put(Constants.DEFAULT_COPY_DIRECTORY, copyDir);
        settingsMap.put(Constants.USER_INTERACTION, Constants.FALSE);
        settingsMap.put(Constants.COPY_FILES_FLAG, Constants.TRUE);
        settingsMap.put(Constants.MEDIA_DIVISION, Constants.TRUE);
        settingsMap.put(Constants.ERROR_HANDLER, Constants.TRUE);
        HelperMethodsTest.generateSettingsFileFromMap(testDirectory, settingsMap);
        HelperMethodsTest.generateTestSettingsFiles(testDirectory);
        Utilities.makeDirectory(testDirectory+"\\a");
        Utilities.makeDirectory(testDirectory+"\\b");
        Utilities.makeDirectory(testDirectory+"\\a\\Anime");
        Utilities.makeDirectory(testDirectory+"\\b\\TVShows");
        ErrorHandler.printOutToFile(testDirectory+"\\mediaDivision.txt","One Punch Man: Anime");
        ErrorHandler.printOutToFile(testDirectory+"\\mediaDivision.txt","Gotham: TVShows");
        byte[] buff = new byte[2048];
        ByteArrayInputStream myIn = new ByteArrayInputStream(buff);
        System.setIn(myIn);
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
        PrintStream myOut = new PrintStream(outputBuffer);
        System.setOut(myOut);
        RunnerHelperThread myRunner = new RunnerHelperThread(myIn,outputBuffer);
        myRunner.setWaitCondition(RunnerHelperThread.Wait.COPY_COMPLETE);
        Thread runnerHelper = new Thread(myRunner);
        runnerHelper.start();
        String title = "One Punch Man";
        HelperMethodsTest.generateTestDirectory(testDir, 1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        title = "Gotham";
        HelperMethodsTest.generateTestDirectory(testDir, 1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{testDir});
        Awaitility.await().until(waitForCopyComplete(myRunner));
        assertTrue(myRunner.copyComplete);
        assertTrue(Utilities.fileExists(testDir+"\\a\\Anime\\One Punch Man\\One Punch Man S01E01.mkv"));
        Awaitility.await().until(waitForCopyComplete(myRunner));
        assertTrue(myRunner.copyComplete);
        assertTrue(Utilities.fileExists(testDir+"\\b\\TVShows\\Gotham\\Gotham S01E01.mkv"));
    }
}
