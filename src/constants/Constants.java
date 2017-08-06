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
    /**Name of the media division file read in and wrote to by system.*/
    public static final String MEDIA_DIVISION_FILE = "mediaDivision.txt";

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
    /**Settings header to determine if the user would like the copied output
     * to be plex divided by media type. User input required for each unique set.*/
    public static final String MEDIA_DIVISION = "MediaDivision";
    /**Settings header to determine if the user would like to use the
     * error handler.*/
    public static final String ERROR_HANDLER = "ErrorHandler";
    /**Settings header to determine how the file structure should be defaulted to.*/
    public static final String COPY_FILE_STRUCTURE = "CopyFileStructure";
    /**Settings header to determine what file types to exclude.*/
    public static final String EXCLUDE_FILE_TYPES = "ExcludeFileTypes";
    /**Settings header to determine if backup is desired.*/
    public static final String BACKUP = "Backup";

    /**String representation of "true" for setting flags in settings files.*/
    public static final String TRUE = "TRUE";
    /**String representation of "false" for setting flags in settings files.*/
    public static final String FALSE = "FALSE";
    /**String representation of "Season" to be at end of folder name.*/
    public static final String SEASON = "Season";
    /**String replacement of season to be used in the settings file.*/
    public static final String SEASON_REPLACEMENT = "{season}";
    /**String replacement of title to be used in the settings file.*/
    public static final String TITLE_REPLACEMENT = "{title}";
    /**Default copy file structure to use.*/
    public static final String DEFAULT_COPY_FILE_STRUCTURE =
            TITLE_REPLACEMENT+"\\"+TITLE_REPLACEMENT+" "+SEASON_REPLACEMENT;
    /**Used to divide media files in output to screen.*/
    public static final String LINE_BREAK = "-----------------------------";
    /**Default byte buffer size for copying data.*/
    public static final int DEFAULT_BYTE_BUFFER_SIZE = 4096;
    /**Media type of a movie, pulled from settings files.*/
    public static final String MOVIES = "Movies";

    /*Runner constants*/
    public static final String DIRECTORY_NULL = "Directory is null.";
    public static final String NO_FILES_TO_RENAME = "No files to Rename.";
    public static final String MEDIA_RENAME_COMPLETE = "Media Renamed.";
    public static final String MEDIA_COPY_COMPLETE = "Media Copied.";
}
