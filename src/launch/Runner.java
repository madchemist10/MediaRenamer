package launch;

import constants.Constants;
import errorHandle.ErrorHandler;
import rename.MediaFile;
import rename.Rename;
import utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ErrorManager;

/**
 * Top level class that kicks off the media rename application.
 */
public class Runner {
    /**Static list of files to rename.*/
    private static ArrayList<File> files = new ArrayList<>();

    public static void main(String[] args) {
        /*Setup working directory.*/
        Setup.setupSettingsFile(Constants.SETTINGS_FILE);
        if(!Utilities.fileExists(Constants.SETTINGS_FILE)) {
            ErrorHandler.printOutToFile(Constants.SETTINGS_FILE, "#" + Constants.DEFAULT_RENAME_DIRECTORY + ":");
            ErrorHandler.printOutToFile(Constants.SETTINGS_FILE, "#" + Constants.DEFAULT_MAX_EPISODE_COUNT + ":");
        }

        Setup.setupSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
        if(!Utilities.fileExists(Constants.SPECIAL_RENAME_CASES_FILE)) {
            ErrorHandler.printOutToFile(Constants.SPECIAL_RENAME_CASES_FILE, "###OriginalName: NewName");
        }
        Setup.setupSettingsFile(Constants.SPECIAL_EP_CASES_FILE);
        if(!Utilities.fileExists(Constants.SPECIAL_EP_CASES_FILE)) {
            ErrorHandler.printOutToFile(Constants.SPECIAL_EP_CASES_FILE, "### S##E## where S## is last season, E## is last ep of S##");
            ErrorHandler.printOutToFile(Constants.SPECIAL_EP_CASES_FILE, "###OriginalName: S##E##");
        }

        /*Read in settings values.*/
        HashMap<String, String> settings = Utilities.loadSettingsFile(Constants.SETTINGS_FILE);
        HashMap<String, String> specialRenameCases = Utilities.loadSettingsFile(Constants.SPECIAL_RENAME_CASES_FILE);
        HashMap<String, String> specialEpisodeCases = Utilities.loadSettingsFile(Constants.SPECIAL_EP_CASES_FILE);
        /*Instantiate rename module and execute rename.*/
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        String directory = settings.get(Constants.DEFAULT_RENAME_DIRECTORY);
        if(directory == null){
            System.out.println("Directory is null.");
            return;
        }
        listFiles(new File(directory).listFiles());
        for(File file : files){
            MediaFile mediaFile = new MediaFile(file.toString());
            renameModule.rename(mediaFile);
            boolean renamed = file.renameTo(new File(mediaFile.toString()));
            String debugMessage = "Debug: "+mediaFile.getOriginalFileName()+
                    " >> "+mediaFile.toString();
            if(renamed){
                ErrorHandler.printOutToFile(Constants.LOG_FILE,debugMessage);
            }
        }
    }

    /**
     * Recursive listFiles structure
     * @param myFiles array of files to add to list.
     */
    private static void listFiles(File[] myFiles){
        for(File file : myFiles){
            if (file.isDirectory())
                listFiles(file.listFiles());
            else
                files.add(file);
        }
    }
}
