import utilities.Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 */
class TestHelperMethods {

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
     */
    static void generateTestDirectory(int max){
        String testDirectory = "TestDir";
        Utilities.makeDirectory(testDirectory);
        String title = "Tokyo Ghoul";
        for(int i = 1; i <= max; i++){
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(title,episodeNumber);
            try {
                Files.createFile(Paths.get(testDirectory+"\\"+originalFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        title = "Mobile Suit Gundam - Iron-Blooded Orphans";
        for(int i = 1; i <= max; i++){
            String episodeNumber = String.format("%2d", i).replace(" ","0");
            String originalFilename = TestHelperMethods.buildHorribleSubsOriginalName(title,episodeNumber);
            try {
                Files.createFile(Paths.get(testDirectory+"\\"+originalFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove the test directory
     */
    static void destroyTestDirectory(){
        String testDirectory = "TestDir";
        Utilities.deleteFile(testDirectory);
    }
}
