import errorHandle.ErrorHandler;
import junit.framework.TestCase;
import launch.Runner;
import utilities.Utilities;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Set of test cases for testing the copy functionality.
 */
public class TestMediaCopyTest extends TestCase {
    /**Lock for ensuring one unit test executes at a time.*/
    private Lock sequential = new ReentrantLock();
    /**Default test directory for these tests.*/
    private static String defaultTestDir = HelperMethodsTest.TESTDIR;

    /**
     * Acquire the lock to begin test execution.
     * @throws Exception if setup failed.
     */
    public void setUp() throws Exception{
        sequential.lock();
        HelperMethodsTest.generateTestSettingsFiles(defaultTestDir);
        super.setUp();
    }

    /**
     * Release the lock to stop test execution.
     * @throws Exception if teardown failed.
     */
    public void tearDown() throws Exception{
        HelperMethodsTest.destroyTestDirectory(defaultTestDir);
        sequential.unlock();
        super.tearDown();
    }

    /**
     * Test the copying of a single file.
     */
    public void testCopySingleFile(){
        String title = "Bloodivores";
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{defaultTestDir});
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+title+"\\"+title+" S01E01.mkv"));
    }

    /**
     * Test the copying of a 3 files of the same season.
     */
    public void testCopy3Files_SameSeason(){
        String title = "Bloodivores";
        int episodeMax = 3;
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{defaultTestDir});
        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir + "\\copy\\" + title + "\\" + title + " S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a single file.
     * Test deletion of original file and existence of new.
     */
    public void testCopySingleFile_DeleteOriginal(){
        String title = "Bloodivores";
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Runner.main(new String[]{defaultTestDir});
        //new file exists
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+title+"\\"+title+" S01E01.mkv"));
        //old file does not exist
        assertTrue(!Utilities.fileExists(defaultTestDir+"\\test\\"+title+" S01E01.mkv"));
    }

    /**
     * Test the copying of a multiple file.
     */
    public void testCopySingleFileToMediaTypeDirectory(){
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt","Tokyo Ghoul: Anime");
        String title = "Tokyo Ghoul";
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\");
        Runner.main(new String[]{defaultTestDir});
        //new file exists
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\Anime\\"+title+"\\"+title+" S01E01.mkv"));
        //old file does not exist
        assertTrue(!Utilities.fileExists(defaultTestDir+"\\test\\"+title+" S01E01.mkv"));
    }
}
