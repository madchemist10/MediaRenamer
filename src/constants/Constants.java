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

    /**Default rename directory key for settings file.*/
    public static final String DEFAULT_RENAME_DIRECTORY = "DefaultRenameDir";
    /**Default copy directory key for settings file. This is where
     * files will be moved to when they are renamed.*/
    public static final String DEFAULT_COPY_DIRECTORY = "DefaultCopyDir";
    /**Default max number of episodes to account for.*/
    public static final String DEFAULT_MAX_EPISODE_COUNT = "MaxEpisodeCount";
    /**Settings header to determine if user interaction is desired.*/
    public static final String USER_INTERACTION = "UserInteraction";
    /**Settings header to determine if the user would like their files copied.*/
    public static final String COPY_FILES_FLAG = "CopyFiles";

    /**String representation of "true" for setting flags in settings files.*/
    public static final String TRUE = "TRUE";
    /**String representation of "false" for setting flags in settings files.*/
    public static final String FALSE = "FALSE";
    /**Used to divide media files in output to screen.*/
    public static final String LINE_BREAK = "-----------------------------";
}
