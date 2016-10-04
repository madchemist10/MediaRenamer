package launch;

import constants.Constants;
import rename.Rename;
import utilities.Utilities;

import java.util.HashMap;

/**
 * Top level class that kicks off the media rename application.
 */
public class Runner {

    public static void main(String[] args) {
        HashMap<String, String> settings = Utilities.loadSettingsFile(Constants.SETTINGS_FILE);
        HashMap<String, String> specialRenameCases = Utilities.loadSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
        HashMap<String, String> specialEpisodeCases = Utilities.loadSettingsFile(Constants.SPECIAL_EP_CASES_FILE);
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        renameModule.rename();
    }
}
