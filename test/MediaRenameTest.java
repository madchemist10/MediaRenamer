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
    public void testMediaRenameShAaiGWithRenameCase(){
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

    /**
     * Media rename with season number being 10.
     */
    public void testMediaRenameShAaiGWithSeasonNum10(){
        String mediaName = "The Big Bang Theory";
        String expectedMediaName = "BBT";
        specialRenameCases.put(mediaName, expectedMediaName);
        String episodeNumber = "01";
        String seasonNumber = "10";
        String originalFileName = TestHelperMethods.buildShAaiGOriginalName(mediaName, seasonNumber, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Sample Big Bang Theory ettv case to rename.
     */
    public void testETTV_BBT_Case(){
        String mediaName = "The Big Bang Theory";
        String expectedMediaName = "BBT";
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "The.Big.Bang.Theory.S10E03.HDTV.XviD-FUM[ettv].avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "BBT S10E03.avi";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Rename media file with offset for episode.
     */
    public void testHorribleSubsWithEpisodeOffset(){
        String mediaName = "Mobile Suit Gundam - Iron-Blooded Orphans";
        String episodeNumber = "26";
        String seasonNumber = "02";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        mediaName = "Mobile Suit Gundam Iron-Blooded Orphans";
        specialEpisodeCases.put(mediaName,"S01E25");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        episodeNumber = "01";
        String expectedFormattedMediaFile = mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Sample Law and Order SVU ettv case to rename.
     */
    public void testETTV_SVU_Case(){
        String mediaName = "Law and Order SVU";
        String expectedMediaName = "SVU";
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "Law.and.Order.SVU.S18E02.WEB-DL.XviD-FUM[ettv].avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "SVU S18E02.avi";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Sample Chicago PD ettv case to rename.
     */
    public void testETTV_CPD_Case(){
        String mediaName = "Chicago PD";
        String expectedMediaName = "CPD";
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "Chicago.PD.S04E02.HDTV.x264-KILLERS[ettv].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "CPD S04E02.mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Sample NCIS LA ettv case to rename.
     */
    public void testETTV_NCISLA_Case(){
        String mediaName = "NCIS Los Angeles";
        String expectedMediaName = "NCIS:LA";
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "NCIS.Los.Angeles.S08E03.HDTV.XviD-FUM[ettv].avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "NCIS:LA S08E03.avi";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Sample NCIS Nola ettv case to rename.
     */
    public void testETTV_NCISNola_Case(){
        String mediaName = "NCIS New Orleans";
        String expectedMediaName = "NCIS:Nola";
        settings.put(Constants.DEFAULT_MAX_EPISODE_COUNT,"100");
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "ncis.new.orleans.302.hdtv-lol[ettv].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "NCIS:Nola S03E02.mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }
}
