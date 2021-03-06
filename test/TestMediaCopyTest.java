import constants.Constants;
import errorHandle.ErrorHandler;
import junit.framework.TestCase;
import launch.Runner;
import launch.Setup;
import utilities.Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
    /**Text string representation of Anime*/
    private static String ANIME = "Anime";
    /**Text string representation of TV Shows*/
    private static String TV_SHOWS = "TVShows";
    /**Text string representation of Movies*/
    private static String MOVIES = "Movies";

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

    /**
     * Test the copying of a single file using the predictive copy algorithm.
     */
    public void testCopyWithPreExistingFileStructure(){
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt","Tokyo Ghoul: Anime");
        String title = "Tokyo Ghoul";
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\"+title+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\"+title+"\\"+title+" Season 1");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\"+title+"\\"+title+" Season 2");
        Runner.main(new String[]{defaultTestDir});
        //new file exists
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\Anime\\"+title+"\\"+title+" Season 1\\"+title+" S01E01.mkv"));
        //old file does not exist
        assertTrue(!Utilities.fileExists(defaultTestDir+"\\test\\"+title+" S01E01.mkv"));
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * Two different shows with 5 files each.
     * This test places each show in {title}\{title} Season {SNum}\.
     */
    public void testCopyWithMultipleFilesWithPreExistingFileStructure(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        String theBlacklistTitle = "The Blacklist";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",theBlacklistTitle+": "+ TV_SHOWS);
        HelperMethodsTest.generateTestDirectory(defaultTestDir,episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+tokyoGhoulTitle+" Season 1");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+tokyoGhoulTitle+" Season 2");

        HelperMethodsTest.generateTestDirectory(defaultTestDir,episodeMax, 1, theBlacklistTitle, HelperMethodsTest.FORMATS.SHAAIG);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+TV_SHOWS+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+TV_SHOWS+"\\"+theBlacklistTitle+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+TV_SHOWS+"\\"+theBlacklistTitle+"\\"+theBlacklistTitle+" Season 1");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+TV_SHOWS+"\\"+theBlacklistTitle+"\\"+theBlacklistTitle+" Season 2");
        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+tokyoGhoulTitle+" Season 1\\"+ tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+TV_SHOWS+"\\"+theBlacklistTitle+"\\"+theBlacklistTitle+" Season 1\\"+ theBlacklistTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * This test places each show in {title}\.
     */
    public void testCopyWithMultipleFilesWithPreExistingFileStructure_2(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\");

        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+ tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * This test places each show in {title} Season {SNum}\.
     */
    public void testCopyWithMultipleFilesWithPreExistingFileStructure_3(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+" Season 1\\");

        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+" Season 1\\"+ tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * This test places each show in {title}\{title} Season {SNum}\.
     */
    public void testCopyWithMultipleFilesWithNonFileStructure(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        ErrorHandler.printOutToFile(defaultTestDir+"\\"+ Constants.SETTINGS_FILE,Constants.COPY_FILE_STRUCTURE+": "+Constants.DEFAULT_COPY_FILE_STRUCTURE);
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);

        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+tokyoGhoulTitle+" Season 1\\"+ tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * This test places each show in {title}\.
     */
    public void testCopyWithMultipleFilesWithNonFileStructure_2(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        ErrorHandler.printOutToFile(defaultTestDir+"\\"+ Constants.SETTINGS_FILE,Constants.COPY_FILE_STRUCTURE+": {title}");
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);

        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+"\\"+tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a set of 5 files using the predictive copy algorithm.
     * This test places each show in {title} {season}.
     */
    public void testCopyWithMultipleFilesWithNonFileStructure_3(){
        String tokyoGhoulTitle = "Tokyo Ghoul";
        int episodeMax = 5;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",tokyoGhoulTitle+": "+ ANIME);
        ErrorHandler.printOutToFile(defaultTestDir+"\\"+ Constants.SETTINGS_FILE,Constants.COPY_FILE_STRUCTURE+": {title} {season}");
        HelperMethodsTest.generateTestDirectory(defaultTestDir, episodeMax, 1, tokyoGhoulTitle, HelperMethodsTest.FORMATS.HORRIBLESUBS);

        Runner.main(new String[]{defaultTestDir});

        for(int i = 1; i <= episodeMax; i++) {
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+tokyoGhoulTitle+" Season 1\\"+tokyoGhoulTitle+" S01E"+episodeNumber+".mkv"));
        }
    }

    /**
     * Test the copying of a single file.
     * Test case has filename of :
     *      Trickster Edogawa Ranpo
     * but belongs in directory with title:
     *      Trickster Edogawa Ranpo Shounen Tanteidan yori
     */
    public void testCopySingleFile_WithReplacement(){
        String title = "Trickster";
        String newTitle = "Trickster Edogawa Ranpo";
        String folderTitle = "Trickster Edogawa Ranpo Shounen Tanteidan yori";
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",newTitle+": "+ ANIME);
        ErrorHandler.printOutToFile(defaultTestDir+"\\"+ Constants.SPECIAL_RENAME_CASES_FILE,title+": "+newTitle);
        ErrorHandler.printOutToFile(defaultTestDir+"\\"+ Constants.SPECIAL_RENAME_CASES_FILE,"$$"+newTitle+": "+folderTitle);
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, 1, title, HelperMethodsTest.FORMATS.HORRIBLESUBS);
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\");
        Utilities.makeDirectory(defaultTestDir+"\\copy\\"+ANIME+"\\"+folderTitle+"\\");
        Runner.main(new String[]{defaultTestDir});
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+ANIME+"\\"+folderTitle+"\\"+newTitle+" S01E01.mkv"));
    }

    /**
     * Test the copying of a single movie file.
     */
    public void testCopySingleMovieFile(){
        String title = "MyTitle";
        int year = 2016;
        ErrorHandler.printOutToFile(defaultTestDir+"\\mediaDivision.txt",title+": "+ MOVIES);
        HelperMethodsTest.generateTestDirectory(defaultTestDir,1, year, title, HelperMethodsTest.FORMATS.MOVIE);
        Runner.main(new String[]{defaultTestDir});
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+MOVIES+"\\"+title+" "+year+".mkv"));
    }

    /**
     * Test the copying and renaming of a file
     * with episode number handling as follows:
     * Boku no Hero Academia: S02##E13
     */
    public void testCopyEpisodePassLastSeason(){
        ErrorHandler.printOutToFile(defaultTestDir+"\\specialEpisodes.txt","Boku no Hero Academia: S02##E13");
        String title = "Boku no Hero Academia";
        String episodeNumber = "31";
        String originalFilename = HelperMethodsTest.buildHorribleSubsOriginalName(title, episodeNumber);
        Utilities.makeDirectory(defaultTestDir);
        Utilities.makeDirectory(defaultTestDir+"\\copy");
        Utilities.makeDirectory(defaultTestDir+"\\test");
        try {
            if(!Utilities.fileExists(defaultTestDir+"\\test\\"+originalFilename)) {
                Files.createFile(Paths.get(defaultTestDir + "\\test\\" + originalFilename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utilities.makeDirectory(defaultTestDir+"\\copy\\Anime\\");
        Runner.main(new String[]{defaultTestDir});
        //new file exists
        assertTrue(Utilities.fileExists(defaultTestDir+"\\copy\\"+title+"\\"+title+" S02E18.mkv"));
        //old file does not exist
        assertTrue(!Utilities.fileExists(defaultTestDir+"\\test\\"+title+" S01E18.mkv"));
    }
}
