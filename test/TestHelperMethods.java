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
}
