import constants.Constants;
import junit.framework.TestCase;
import rename.MediaFile;
import rename.Rename;
import utilities.Utilities;

import java.util.HashMap;

/**
 * Set of tests to test the rename module.
 */
public class MediaRenameTest extends TestCase{
    /*Read in settings values.*/
    private HashMap<String, String> settings = Utilities.loadSettingsFile(Constants.SETTINGS_FILE);
    private HashMap<String, String> specialRenameCases = Utilities.loadSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
    private HashMap<String, String> specialEpisodeCases = Utilities.loadSettingsFile(Constants.SPECIAL_EP_CASES_FILE);

    /**
     * Simple test case where only episode is given in the original name.
     * Episode is three digits long.
     * Since no season number given, should result in season 1.
     */
    public void testMediaRenameWithSimpleCase3DigitE(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "001";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S01E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Simple test case where only episode is given in the original name.
     * Season number should default to "01" since its not given.
     */
    public void testMediaRenameWithSimpleCase2DigitE(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String seasonNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Simple test case where episode & season are given in the original name.
     * Season number is first 3 digits and episode is last 1 digit.
     */
    public void testMediaRenameWithSimpleCase3DigitE_1DigitS(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "001";
        String seasonNumber = "1";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S0"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }


    /**
     * Simple test case where episode & season are given in the original name.
     * Season number is first 2 digits and episode is last 2 digits.
     * Season is denoted with S## in the original filename.
     */
    public void testMediaRenameWithSimpleCase2DigitE_2DigitS(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String seasonNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }


    /**
     * Simple test case where episode & season are given in the original name.
     * Season number is first 1 digit and episode is last 3 digit.
     * Season and Episode is given all together.
     */
    public void testMediaRenameWithSimpleCase4DigitE_S(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "001";
        String seasonNumber = "1";
        String combinedSeasonEpisode = seasonNumber+episodeNumber;
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, combinedSeasonEpisode);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S0"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Simple test case where episode & season are given in the original name.
     * Season number is first 1 digit and episode is last 2 digits.
     * Season is denoted with S# in the original filename.
     */
    public void testMediaRenameWithSimpleCase3DigitE_1S(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String seasonNumber = "1";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = mediaName+" S0"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }


    /**
     * Media rename test for testing the replacement
     * of a given title.
     */
    public void testMediaRenameWithRenameCase(){
        String mediaName = "The Big Bang Theory";
        String expectedMediaName = "BBT";
        specialRenameCases.put(mediaName, expectedMediaName);
        String episodeNumber = "01";
        String seasonNumber = "01";
        String originalFileName = TestHelperMethods.buildShAaiGOriginalName(mediaName, seasonNumber, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }
}
