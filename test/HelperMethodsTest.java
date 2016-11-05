import constants.Constants;
import errorHandle.ErrorHandler;
import launch.Setup;
import utilities.Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 */
class HelperMethodsTest {

    /**TestDirectory that all test that need path use.*/
    static final String TESTDIR = "TestDir";

    /**
     * Enum to represent possible formats that can be generated.
     */
    enum FORMATS{
        HORRIBLESUBS,
        ETTV,
        SHAAIG,
        ANIMERG
    }

    /**
     * Construct a properly formatted horribleSubs original Filename.
     * @param mediaName of the original name.
     * @param episodeNumber of the original name.
     * @return horribleSubs formatted originalName.
     */
    static String buildHorribleSubsOriginalName(String mediaName, String episodeNumber){
        return String.format("[HorribleSubs] %s - %s [720p].mkv", mediaName, episodeNumber);
    }

    /**
     * Construct a properly formatted horribleSubs original Filename.
     * @param mediaName of the original name.
     * @param seasonNumber of the original name.
     * @param episodeNumber of the original name.
     * @return horribleSubs formatted originalName.
     */
    static String buildHorribleSubsOriginalName(String mediaName, String seasonNumber, String episodeNumber){
        return String.format("[HorribleSubs] %s - S%s %s [720p].mkv", mediaName, seasonNumber ,episodeNumber);
    }

    /**
     * Construct a properly formatted animeRG original Filename.
     * @param mediaName of the original name.
     * @param episodeNumber of the original name.
     * @return animeRG formatted originalName.
     */
    static String buildAnimeRGOriginalName(String mediaName, String episodeNumber){
        return String.format("[AnimeRG] %s - %s [1080p][HEVC][TheBiscuitMan].mkv", mediaName, episodeNumber);
    }

    /**
     * Construct a properly formatted ShAaNiG original Filename.
     * Ex: "The.Big.Bang.Theory.S10E03.720p.HDTV.x265.ShAaNiG.mkv"
     * @param mediaName of the original name.
     * @param seasonNumber of the original name.
     * @param episodeNumber of the original name.
     * @return ShAaNiG formatted originalName.
     */
    static String buildShAaiGOriginalName(String mediaName, String seasonNumber, String episodeNumber){
        /*Replace all spaces with "." for each field.*/
        mediaName = mediaName.replaceAll(" ",".");
        seasonNumber = seasonNumber.replaceAll(" ",".");
        episodeNumber = episodeNumber.replaceAll(" ",".");
        return String.format("%s.S%sE%s.720p.HDTV.x265.ShAaNiG.mkv", mediaName, seasonNumber ,episodeNumber);
    }

    /**
     * Build the test directory
     * @param testDirectory of root level.
     * @param maxEp of number of iteration or number of files to generate.
     * @param season of the show to generate, only used if nonAnime.
     * @param title of the show that we are generating.
     * @param format of the show that is to be generated, Must be from enum.
     */
    static void generateTestDirectory(String testDirectory, int maxEp, int season, String title, FORMATS format){
        Utilities.makeDirectory(testDirectory);
        Utilities.makeDirectory(testDirectory+"\\copy");
        testDirectory += "\\test";
        Utilities.makeDirectory(testDirectory);
        String seasonNumber = String.format("%2d", season).replace(" ","0");
        for(int i = 1; i <= maxEp; i++){
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            String originalFilename = null;
            switch(format){
                case HORRIBLESUBS:
                    originalFilename = HelperMethodsTest.buildHorribleSubsOriginalName(title, episodeNumber);
                    break;
                case SHAAIG:
                    originalFilename = HelperMethodsTest.buildShAaiGOriginalName(title, seasonNumber, episodeNumber);
                    break;
                case ETTV:
                    break;
                case ANIMERG:
                    originalFilename = HelperMethodsTest.buildAnimeRGOriginalName(title, episodeNumber);
                    break;
            }
            if(originalFilename == null){
                return;
            }
            try {
                if(!Utilities.fileExists(testDirectory+"\\"+originalFilename)) {
                    Files.createFile(Paths.get(testDirectory + "\\" + originalFilename));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove the test directory
     * @param testDirectory of root level.
     */
    static void destroyTestDirectory(String testDirectory){
        Utilities.deleteFolder(testDirectory);
        Utilities.makeDirectory(testDirectory);
    }

    /**
     * Generate settings files to be used in testing.
     * Recreate if deleted.
     * @param testDirectory root level of where we are working from.
     */
    static void generateTestSettingsFiles(String testDirectory){
        if(!Utilities.fileExists(testDirectory+"\\"+Constants.SETTINGS_FILE)) {
            Setup.setupSettingsFile(testDirectory + "\\" + Constants.SETTINGS_FILE);
            Map<String, String> settingsMap = new HashMap<>();
            settingsMap.put(Constants.DEFAULT_RENAME_DIRECTORY, testDirectory+"\\test");
            settingsMap.put(Constants.DEFAULT_COPY_DIRECTORY, testDirectory+"\\copy");
            settingsMap.put(Constants.USER_INTERACTION, Constants.FALSE);
            settingsMap.put(Constants.COPY_FILES_FLAG, Constants.TRUE);
            settingsMap.put(Constants.MEDIA_DIVISION, Constants.TRUE);
            settingsMap.put(Constants.ERROR_HANDLER, Constants.TRUE);
            generateSettingsFileFromMap(testDirectory, settingsMap);
        }
        Setup.setupSettingsFile(testDirectory+"\\"+Constants.SPECIAL_EP_CASES_FILE);
        Setup.setupSettingsFile(testDirectory+"\\"+Constants.SPECIAL_RENAME_CASES_FILE);
        Setup.setupSettingsFile(testDirectory+"\\"+Constants.MEDIA_DIVISION_FILE);
    }

    /**
     * Generation of settings file to a testDirectory from a given map.
     * @param testDirectory of where settings file is located.
     * @param settingsMap of system settings.
     */
    static void generateSettingsFileFromMap(String testDirectory, Map<String, String> settingsMap){
        for(String key: settingsMap.keySet()){
            String value = settingsMap.get(key);
            ErrorHandler.printOutToFile(testDirectory+"\\"+Constants.SETTINGS_FILE,key+": "+value);
        }
    }
}
