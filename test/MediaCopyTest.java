import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Set of test cases for testing the copy functionality.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MediaCopyTest extends TestCase {

    public void testaCopySingleFile(){
        TestHelperMethods.generateTestDirectory(1);
        TestHelperMethods.destroyTestDirectory();
    }
}
