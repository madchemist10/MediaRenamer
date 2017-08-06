import backup.Backup;
import junit.framework.TestCase;
import utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class TestBackupTest extends TestCase{
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

    public void FAILINGtestBackupSingleFile() throws IOException {
        String srcDirectory = "Z:\\TestDir";
        String destDirectory = "Z:\\TestDir2";
        String fileName = "sample.txt";
        String filePath = srcDirectory + "\\" + fileName;
        File testFile = new File(filePath);
        if(!Utilities.fileExists(filePath)) {
            Files.createFile(Paths.get(filePath));
        }
        assertTrue(Utilities.fileExists(filePath));
        boolean success = testFile.setLastModified(System.currentTimeMillis());
        assertTrue(success);
        Backup backup = new Backup(srcDirectory,destDirectory);
        backup.performBackup();
        assertTrue(Utilities.fileExists(destDirectory+"\\"+fileName));
        assertTrue(new File(destDirectory+"\\"+fileName).delete());
    }

    public void testCompareFilePaths(){
        String path_1 = "X:\\Anime\\BTOOOM!\\BTOOOM! S01E06.mkv";
        String path_2 = "Y:\\Anime\\BTOOOM!\\BTOOOM! S01E06.mkv";
        boolean compare = Backup.compareFilePaths(path_1, path_2);
        assertTrue(compare);
    }

    public void testSwapDriveLetters(){
        String path_1 = "X:\\Anime\\BTOOOM!\\BTOOOM! S01E06.mkv";
        String path_2 = "Y:\\Anime\\BTOOOM!\\BTOOOM! S01E06.mkv";
        String newPath_1 = Backup.swapDriveLetters(path_1, "X:", "Y:");
        assertEquals(newPath_1, path_2);
    }

}
