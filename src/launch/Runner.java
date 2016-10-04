package launch;

import constants.Constants;
import errorHandle.ErrorHandler;
import rename.Rename;
import utilities.Utilities;

import java.util.HashMap;
import java.util.logging.ErrorManager;

/**
 * Top level class that kicks off the media rename application.
 */
public class Runner {

    public static void main(String[] args) {
        /*Setup working directory.*/
        Setup.setupSettingsFile(Constants.SETTINGS_FILE);
        ErrorHandler.printOutToFile(Constants.SETTINGS_FILE, "#"+Constants.DEFAULT_RENAME_DIRECTORY+":");

        Setup.setupSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
        ErrorHandler.printOutToFile(Constants.SPECIAL_RENAME_CASES_FILE, "###OriginalName: NewName");

        Setup.setupSettingsFile(Constants.SPECIAL_EP_CASES_FILE);
        ErrorHandler.printOutToFile(Constants.SPECIAL_EP_CASES_FILE, "### S##E## where S## is last season, E## is last ep of S##");
        ErrorHandler.printOutToFile(Constants.SPECIAL_EP_CASES_FILE, "###OriginalName: S##E##");

        /*Read in settings values.*/
        HashMap<String, String> settings = Utilities.loadSettingsFile(Constants.SETTINGS_FILE);
        HashMap<String, String> specialRenameCases = Utilities.loadSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
        HashMap<String, String> specialEpisodeCases = Utilities.loadSettingsFile(Constants.SPECIAL_EP_CASES_FILE);
        /*Instantiate rename module and execute rename.*/
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
    }
}
