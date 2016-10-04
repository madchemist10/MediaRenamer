import junit.framework.TestCase;
import rename.MediaFile;

/**
 * Unit tests to conduct for media file class..
 */
public class MediaFileTest extends TestCase{

    /**
     * Sample test that gives valid original filename and
     * supporting pre-parsed components.
     * Testing two digit episode number given.
     * Assert that the formatted mediaFile name is formatted correctly
     * based on the given input.
     */
    public void testMediaFileParseHorribleSubsTwoDigitEpisodeNum(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(mediaName);
        testFile.setSeasonNumber("1");
        testFile.setEpisodeNumber(episodeNumber);
        testFile.setFileExt("mkv");
        String formattedMediaFile = testFile.toString();
        String expectedMediaFileFormat = "Tokyo Ghoul S01E01.mkv";
        assertEquals(expectedMediaFileFormat, formattedMediaFile);
    }


    /**
     * Sample test that gives valid original filename and
     * supporting pre-parsed components.
     * Testing three digit episode number given.
     * Assert that the formatted mediaFile name is formatted correctly
     * based on the given input.
     */
    public void testMediaFileParseHorribleSubsThreeDigitEpisodeNum(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "001";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(mediaName);
        testFile.setSeasonNumber("1");
        testFile.setEpisodeNumber(episodeNumber);
        testFile.setFileExt("mkv");
        String formattedMediaFile = testFile.toString();
        String expectedMediaFileFormat = "Tokyo Ghoul S01E001.mkv";
        assertEquals(expectedMediaFileFormat, formattedMediaFile);
    }

    /**
     * Test formatted testMediaFile with null mediaName.
     */
    public void testNullMediaName(){
        String episodeNumber = "01";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(null, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(null);
        testFile.setSeasonNumber("1");
        testFile.setEpisodeNumber(episodeNumber);
        testFile.setFileExt("mkv");
        String formattedMediaFile = testFile.toString();
        assertNull(formattedMediaFile);
    }

    /**
     * Test formatted testMediaFile with null seasonNumber.
     */
    public void testNullSeasonNumber(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(mediaName);
        testFile.setSeasonNumber(null);
        testFile.setEpisodeNumber(episodeNumber);
        testFile.setFileExt("mkv");
        String formattedMediaFile = testFile.toString();
        assertNull(formattedMediaFile);
    }

    /**
     * Test formatted testMediaFile with null episodeNumber.
     */
    public void testNullEpisodeNumber(){
        String mediaName = "Tokyo Ghoul";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, null);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(mediaName);
        testFile.setSeasonNumber("1");
        testFile.setEpisodeNumber(null);
        testFile.setFileExt("mkv");
        String formattedMediaFile = testFile.toString();
        assertNull(formattedMediaFile);
    }

    /**
     * Test formatted testMediaFile with null episodeNumber.
     */
    public void testNullFileExt(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        testFile.setMediaName(mediaName);
        testFile.setSeasonNumber("1");
        testFile.setEpisodeNumber(episodeNumber);
        testFile.setFileExt(null);
        String formattedMediaFile = testFile.toString();
        assertNull(formattedMediaFile);
    }

    /**
     * Test to test the formatted original filename
     * is not corrupted.
     */
    public void testOriginalFileNameExists(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testFile = new MediaFile(originalFilename);
        String expectedOriginalName = "[HorribleSubs] "+mediaName+" - "+episodeNumber+" [720p].mkv";
        assertEquals(expectedOriginalName, testFile.getOriginalFileName());
    }

}
