import constants.Constants;
import junit.framework.TestCase;
import rename.MediaFile;
import rename.Rename;
import utilities.Utilities;

import java.util.HashMap;

/**
 * Set of tests to test the rename module.
 */
public class TestMediaRenameTest extends TestCase{
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, combinedSeasonEpisode);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, seasonNumber, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildShAaiGOriginalName(mediaName, seasonNumber, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildShAaiGOriginalName(mediaName, seasonNumber, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildHorribleSubsOriginalName(mediaName, episodeNumber);
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
        String originalFileName = HelperMethodsTest.buildAnimeRGOriginalName(mediaName, episodeNumber);
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
        String originalFileName = "Sample 1999.mkv";
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

    /**
     * Test media rename when replacement and episode are
     * given. If Season is 1 from algorithm and and
     * season 1 is what we want, but episodes need offset,
     * we given settings S00E{offset}
     */
    public void testMediaRename_EpisodeReplacement(){
        String originalFileName = "Bubuki Buranki - 14.mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        specialRenameCases.put("Bubuki Buranki","Bubuki Buranki Hoshi no Kyojin");
        specialEpisodeCases.put("Bubuki Buranki Hoshi no Kyojin","S00E12");
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Bubuki Buranki Hoshi no Kyojin S01E02.mkv", testMediaFile.toString());
    }

    /**
     * Test media rename with a movie that has audio channels in
     * filename.
     */
    public void testMediaRenameMovie_ShAaNiG_6CH(){
        String originalFileName = "Pans.Labyrinth.2006.REMASTERED.1080p.BluRay.6CH.ShAaNiG.mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Pans Labyrinth 2006.mkv", testMediaFile.toString());
    }

    /**
     * Test case where the filename contains the keyword Episode.
     */
    public void testMediaRenameEpisodeKeyWord(){
        String originalFileName = "[Kōritsu].Clannad.Episode.01.On.a.Slope.with.Falling.Cherry.Blossoms.1080p.Dual.Audio.Bluray.(8C127EE1).mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Clannad S01E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where the filename contains the keyword Special.
     * Should result in season number being 0.
     */
    public void testMediaRenameSpecialKeyWord(){
        String originalFileName = "[Kōritsu].Clannad.Special.1.It.Happened.During.Summer.Vacation.1080p.Dual.Audio.Bluray.(A25142DF).mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Clannad S00E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where the filename contains the keyword episode.
     */
    public void testMediaRenameLowerEpisodeKeyWord(){
        String originalFileName = "[Kōritsu].Clannad.episode.01.On.a.Slope.with.Falling.Cherry.Blossoms.1080p.Dual.Audio.Bluray.(8C127EE1).mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Clannad S01E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where the filename contains the keyword special.
     * Should result in season number being 0.
     */
    public void testMediaRenameLowerSpecialKeyWord(){
        String originalFileName = "[Kōritsu].Clannad.special.1.It.Happened.During.Summer.Vacation.1080p.Dual.Audio.Bluray.(A25142DF).mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Clannad S00E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where the filename contains the keyword episode.
     * Apostrophes are in the filename.
     */
    public void testMediaRenameEpisodeKeyWord_Apostrophe(){
        String originalFileName = "[Kōritsu].Clannad.Episode.06.The.Sister`s.Founder`s.Festival.1080p.Dual.Audio.Bluray.(95F009A9).mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Clannad S01E06.mkv", testMediaFile.toString());
    }

    //[Kōritsu].Clannad.Episode.16.3.on.3.1080p.Dual.Audio.Bluray.(845198A7)
    //special case, need to add test case.

    /**
     * Test case where filename contains underscore as well as special episode keyword.
     */
    public void test_1_MediaRename_Steins_Gate(){
        String originalFileName = "[DeadFish] Steins;Gate_ Soumei Eichi no Cognitive Computing - 01 - ONA [720p][AAC].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Steins Gate Soumei Eichi no Cognitive Computing S00E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where filename contains year within parenthesis.
     */
    public void test_2_MediaRename_Steins_Gate(){
        String originalFileName = "[AnimeRG] Gekijouban Steins;Gate - Fuka Ryouiki no Deja vu (2013) [1080p] [FLAC5.1][x265] [10bit] [FK99].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Steins Gate Fuka Ryouiki no Deja vu 2013.mkv", testMediaFile.toString());
    }

    /**
     * Test case where filename is Mob Psycho 100.
     * Title has number within. Uses special rename cases
     * settings file to account for this.
     */
    public void test_1_MediaRename_Mob_Psycho(){
        String originalFileName = "[AnimeRG]  Mob Psycho 100 - 01 [1080p] [HEVC] [TheBiscuitMan].mkv";
        specialRenameCases.put("Mob Psycho 100","Mob Psycho 100");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Mob Psycho 100 S01E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where filename is Mob Psycho 100.
     * Title has number within. Uses special rename cases
     * settings file to account for this.
     * Path included in media name.
     */
    public void test_2_MediaRename_Mob_Psycho(){
        String path = "C:\\test\\Media\\";
        String originalFileName = path+"[AnimeRG]  Mob Psycho 100 - 01 [1080p] [HEVC] [TheBiscuitMan].mkv";
        specialRenameCases.put("Mob Psycho 100","Mob Psycho 100");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals(path+"Mob Psycho 100 S01E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where filename has unique season/episode scheme of
     * S#xE##; followed by episode title.
     */
    public void test_1_MediaRename_Claymore(){
        String originalFileName = "Claymore - 1x01 - Great Sword [1080p] [Dual Audio5.1][HEVC][10bit] {bk}.mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Claymore S01E01.mkv", testMediaFile.toString());
    }

    /**
     * Test case where filename has season number and episode number bound within [].
     */
    public void test_MediaRename_FamilyGuy(){
        String originalFileName = "Family Guy [2.01] Peter, Peter, Caviar Eater.avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Family Guy S02E01.avi", testMediaFile.toString());
    }

    /**
     * Test case where filename is separated via underscores..
     */
    public void test_MediaRename_GilliganIsland(){
        String originalFileName = "Gilligans_Island_-_s01e02_Home_Sweet_Hut.avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Gilligans Island S01E02.avi", testMediaFile.toString());
    }

    /**
     * Test case where filename is movie with plain text Movie but not a year.
     */
    public void test_MediaRename_MovieNoYear(){
        String originalFileName = "Sample - Movie.avi";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Sample.avi", testMediaFile.toString());
    }

    /**
     * Test case where filename is movie with plain text Movie
     * and Gekijouban.
     */
    public void test_MediaRename_MovieGekijouban(){
        String originalFileName = "[DeadFish] Ao no Exorcist Gekijouban - Movie [BD][720p][AAC].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Ao no Exorcist.mkv", testMediaFile.toString());
    }

    /**
     * Rename media file with offset for episode.
     * Season is season 1.
     * Specify specific season number and episode offset.
     */
    public void testHorribleSubsWithEpisodeOffset_Season1(){
        String mediaName = "Bungou Stray Dogs";
        String seasonNumber = "01";
        String originalFileName = "[HorribleSubs] Bungou Stray Dogs - 13 [720p].mkv";
        specialEpisodeCases.put(mediaName,"S01##E12");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        String episodeNumber = "01";
        String expectedFormattedMediaFile = mediaName+" S"+seasonNumber+"E"+episodeNumber+".mkv";
        assertEquals(expectedFormattedMediaFile, testMediaFile.toString());
    }

    /**
     * Test case where season number and episode number are joined and
     * the title contains a number.
     */
    public void test2BrokeGirlsSeasonNumEpisodeNumJoined(){
        String originalFileName = "2.broke.girls.501.hdtv-lol[ettv].mp4";
        specialRenameCases.put("2 broke girls","2 broke girls");
        specialRenameCases.put("broke girls","2 Broke Girls");
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("2 Broke Girls S05E01.mp4", testMediaFile.toString());
    }

    /**
     * Test case where filename has comma in filename
     */
    public void test_MediaRename_CommaInFilename(){
        String originalFileName = "Family Guy - S02E08 - I Am Peter, Hear Me Roar [480p DVD x265][AAC 2.0][Sub][Ch].mkv";
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
        assertEquals("Family Guy S02E08.mkv", testMediaFile.toString());
    }
}
