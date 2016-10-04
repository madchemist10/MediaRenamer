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

    public void testMediaRenameWithSimpleCase(){
        String mediaName = "Tokyo Ghoul";
        String episodeNumber = "01";
        String originalFileName = TestHelperMethods.buildHorribleSubsOriginalName(mediaName, episodeNumber);
        MediaFile testMediaFile = new MediaFile(originalFileName);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename(testMediaFile);
    }

}
