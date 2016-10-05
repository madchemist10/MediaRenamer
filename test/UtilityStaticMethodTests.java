import junit.framework.TestCase;
import utilities.Utilities;

/**
 * Set of tests to test the correctness of static methods
 * from the Utility Class.
 */
public class UtilityStaticMethodTests extends TestCase {

    /**
     * Simple test to test that the filename is parsed
     * from the given path.
     */
    public void testParseFilenameFromPath(){
        String pathWithFilename = "C:\\User\\Test\\myFile.txt";
        String filename = Utilities.parseFilenameFromPath(pathWithFilename);
        assertEquals("myFile.txt",filename);
    }

    /**
     * Simple test to test that the filename is removed
     * from the given path.
     */
    public void testRemoveFilenameFromPath(){
        String pathWithFilename = "C:\\User\\Test\\myFile.txt";
        String path = Utilities.removeFilenameFromPath(pathWithFilename);
        assertEquals("C:\\User\\Test\\",path);
    }
}
