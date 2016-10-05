import constants.Constants;
import copy.Copy;
import junit.framework.Test;
import junit.framework.TestCase;
import launch.Runner;
import rename.Rename;
import utilities.Utilities;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Set of test cases for testing the copy functionality.
 */
public class MediaCopyTest extends TestCase {
    /**Lock for ensuring one unit test executes at a time.*/
    private Lock sequential = new ReentrantLock();
    /**Default test directory for these tests.*/
    private static String defaultTestDir = "TestDir";

    /**
     * Acquire the lock to begin test execution.
     * @throws Exception if setup failed.
     */
    public void setUp() throws Exception{
        sequential.lock();
        super.setUp();
    }

    /**
     * Release the lock to stop test execution.
     * @throws Exception if teardown failed.
     */
    public void tearDown() throws Exception{
        sequential.unlock();
        super.tearDown();
    }

    /**
     * Test the copying of a single file.
     */
    public void testCopySingleFile(){
        String testDir = defaultTestDir;
        TestHelperMethods.generateTestSettingsFiles(testDir);
        String title = "Tokyo Ghoul";
        TestHelperMethods.generateTestDirectory(testDir,1, title);
        Runner.main(new String[]{defaultTestDir});
        boolean tokyoGhoulS01E01Exists = Utilities.fileExists(testDir+"\\copy\\"+title+"\\"+title+" S01E01.mkv");
        assertTrue(tokyoGhoulS01E01Exists);
        TestHelperMethods.destroyTestDirectory(testDir);
    }
}
