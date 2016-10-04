package constants;

/**
 * Constants used throughout this application.
 */
public class Constants {
    /**Name of the log file for this application.*/
    public static final String LOG_FILE = "mediaRename.log";
    /**Name of the settings file read in by the system.*/
    public static final String SETTINGS_FILE = "settings.txt";
    /**Name of the special rename file read in by the system.*/
    public static final String SPECIAL_RENAME_CASES_FILE = "specialRename.txt";
    /**Name of the special episode cases file read in by the system.*/
    public static final String SPECIAL_EP_CASES_FILE = "specialEpisodes.txt";

    /**Find and replace var for DEFAULT_MEDIA_NAME for mediaName*/
    public static final String MEDIA_NAME = "MEDIA_NAME";
    /**Find and replace var for DEFAULT_MEDIA_NAME for season number*/
    public static final String XX = "xx";
    /**Find and replace var for DEFAULT_MEDIA_NAME for episode number*/
    public static final String YYY = "yyy";
    /**Find and replace var for DEFAULT_MEDIA_NAME for file extension*/
    public static final String FILE_EXT = "fileExt";
    /**Default media name format.
     * "MEDIA_NAME SxxEyyy.fileExt"
     * x = season number formatted for two digits.
     * y = episode number formatted for two or three digits.*/
    public static final String DEFAULT_MEDIA_NAME = MEDIA_NAME+" S"+XX+"E"+YYY+"."+FILE_EXT;
}
