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

    public void setUp() throws Exception{
        settings.put(Constants.DEFAULT_MAX_EPISODE_COUNT, "100");
        super.setUp();
    }

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
        specialRenameCases.put(mediaName, expectedMediaName);
        String originalFileName = "ncis.new.orleans.302.hdtv-lol[ettv].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = "NCIS:Nola S03E02.mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test case with number that is not episode or season
     * contained within title.
     * Using rename case and episode case, exchange can be done.
     * Attempt to have algorithm process based on maximum episode count permitted.
     */
    public void testMediaRenameWithSimpleCaseNumberInTitle_MAX_EP_COUNT(){
        String mediaName = "Danganronpa 3 - Despair Arc";
        String expectedMediaName = "Danganronpa 3 The End of Kibougamine Gakuen - Zetsubou-hen";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        mediaName = "Danganronpa  Despair Arc";
        specialRenameCases.put(mediaName, expectedMediaName);
        specialEpisodeCases.put(expectedMediaName, "S01");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S01E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test case with number that is not episode or season
     * contained within title.
     * Using rename case and episode case, exchange can be done.
     */
    public void testMediaRenameWithSimpleCaseNumberInTitle(){
        String mediaName = "Danganronpa 3 - Despair Arc";
        String expectedMediaName = "Danganronpa 3 The End of Kibougamine Gakuen - Zetsubou-hen";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        mediaName = "Danganronpa  Despair Arc";
        specialRenameCases.put(mediaName, expectedMediaName);
        specialEpisodeCases.put(expectedMediaName, "S01");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S01E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test case with number that is not episode or season
     * contained within title.
     * Using rename case and episode case, exchange can be done.
     * Season number is 00 to account for special episode.
     */
    public void testMediaRenameWithSimpleCaseNumberInTitleExchangeWithE00(){
        String mediaName = "Danganronpa 3 - Hope Arc";
        String expectedMediaName = "Danganronpa 3 The End of Kibougamine Gakuen - Kibou-hen";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        mediaName = "Danganronpa  Hope Arc";
        specialRenameCases.put(mediaName, expectedMediaName);
        specialEpisodeCases.put(expectedMediaName, "S00");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S00E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test to attempt rename with no season or episode numbers.
     */
    public void testMediaRenameNoNumbers(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertNull(testMediaFile.toString());
    }

    /**
     * Test to attempt rename with no season or episode numbers.
     * Settings file are configured.
     */
    public void testMediaRenameNoNumbersWithSettingsFile(){
        String mediaName = "Tokyo Ghoul";
        specialEpisodeCases.put(mediaName, "S01");
        String episodeNumber = "";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertNull(testMediaFile.toString());
    }

    /**
     * Test to attempt rename with no file ext.
     */
    public void testMediaRenameNoFileExt(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        originalFileName = originalFileName.replaceAll("[.mkv]",""); //remove file ext
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertNull(testMediaFile.toString());
    }

    /**
     * Test to attempt rename with no file ext.
     * Filename has all spaces replaced with "."
     */
    public void testMediaRenameNoFileExt_MultipleDot(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        originalFileName = originalFileName.replaceAll("[.mkv]",""); //remove file ext
        originalFileName = originalFileName.replaceAll(" ",".");    //replace all space with "."
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertNull(testMediaFile.toString());
    }

    /**
     * Test to test renaming when there is a filepath involved.
     */
    public void testRenameWithFilePath(){
        String filepath = "C:\\Test\\";
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        String completePath = filepath+originalFileName;
        MediaFile testMediaFile = new MediaFile(completePath);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = filepath+mediaName+" S01E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Rename media file with offset for episode.
     * Season is season 2.
     */
    public void testHorribleSubsWithEpisodeOffset_Season2(){
        String mediaName = "Bungou Stray Dogs";
        String seasonNumber = "02";
        String originalFileName = "[HorribleSubs] Bungou Stray Dogs S2 - 13 [720p].mkv";
        specialEpisodeCases.put(mediaName,"S01E12");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String episodeNumber = "01";
        String expectedFormattedMediaFile = mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Rename media file with offset for episode.
     * Season is season 2.
     * Also prepend file path to beginning.
     */
    public void testHorribleSubsWithEpOffset_S2_FilePath(){
        String mediaName = "Bungou Stray Dogs";
        String seasonNumber = "02";
        String originalFileName = "C:\\Test\\[HorribleSubs] Bungou Stray Dogs S2 - 13 [720p].mkv";
        specialEpisodeCases.put(mediaName,"S01E12");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String episodeNumber = "01";
        String expectedFormattedMediaFile = "C:\\Test\\"+mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Simple test case where episode & season are given in the original name.
     * Season number is first 1 digit and episode is last 2 digits.
     * Season is denoted with S# in the original filename.
     */
    public void testMediaRenameWithSimpleCaseAnimeRG(){
        String mediaName = "Alderamin on the Sky";
        String episodeNumber = "12";
        String seasonNumber = "1";
        String originalFileName = TestHelperMethods.buildAnimeRGOriginalName(mediaName, episodeNumber);
        String expectedMediaName = "Nejimaki Seirei Senki Tenkyou no Alderamin";
        specialRenameCases.put(mediaName,expectedMediaName);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = expectedMediaName+" S0"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test with path prepended on filename, path contains numbers.
     * Special case replacement.
     */
    public void testMediaRenameWithPath_Replace_AllLower(){
        String filepath = "Z:\\Need to Watch\\Rename\\Blue.Bloods.S06E19.HDTV.x264-LOL[ettv]\\";
        String originalFileName = "blue bloods  hdtv-lol S06E19.mp4";
        String completePath = filepath+originalFileName;
        specialRenameCases.put("blue bloods", "Blue Bloods");
        MediaFile testMediaFile = new MediaFile(completePath);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String expectedFormattedMediaFile = filepath+"Blue Bloods S06E19.mp4";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test a media entry that is already renamed.
     * Path precedes the media file.
     */
    public void testMediaRenameFileAlreadyRenamed_WithPath(){
        String filepath = "C:\\test\\Tokyo Ghoul\\";
        String originalFileName = "Tokyo Ghoul S01E01.mkv";
        String completePath = filepath+originalFileName;
        MediaFile testMediaFile = new MediaFile(completePath);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(completePath, testMediaFile.toString());
    }

    /**
     * Test a media entry that is already renamed.
     */
    public void testMediaRenameFileAlreadyRenamed(){
        String originalFileName = "Tokyo Ghoul S01E01.mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(originalFileName, testMediaFile.toString());
    }

    /**
     * Test for parsing the year from a movie.
     */
    public void testMediaRenameMovieFileCorrectOutput(){
        String originalFileName = "Movie 1999.mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(originalFileName, testMediaFile.toString());
    }

    /**
     * Test media rename with media file where first character is number.
     */
    public void testMediaRename_2BrokeGirls(){
        String path = "Z:\\Need to Watch\\Rename\\2.Broke.Girls.Season.3.Complete.1080p.WebRip.x265.HEVC-zsewdc\\S03\\";
        String originalFileName = "2.Broke.Girls.S03E01.1080p.WebRip.x265.HEVC-zsewdc.mkv";
        MediaFile testMediaFile = new MediaFile(path+originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(path+"2 Broke Girls S03E01.mkv", testMediaFile.toString());
    }

    /**
     * Test media rename with media file where first character is number.
     * Episode is same as character from filename.
     */
    public void testMediaRename_2BrokeGirls_EP_22(){
        String path = "Z:\\Need to Watch\\Rename\\2.Broke.Girls.Season.3.Complete.1080p.WebRip.x265.HEVC-zsewdc\\S03\\";
        String originalFileName = "2.Broke.Girls.S03E22.1080p.WebRip.x265.HEVC-zsewdc.mkv";
        MediaFile testMediaFile = new MediaFile(path+originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(path+"2 Broke Girls S03E22.mkv", testMediaFile.toString());
    }
}
