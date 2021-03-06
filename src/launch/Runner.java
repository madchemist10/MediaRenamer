package launch;

import backup.Backup;
import constants.Constants;
import copy.Copy;
import errorHandle.ErrorHandler;
import rename.MediaFile;
import rename.Rename;
import utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Top level class that kicks off the media rename application.
 */
public class Runner {
    /**Static list of files to rename.*/
    private static ArrayList<File> files = new ArrayList<>();

    public static void main(String[] args) {

        /*Command line argument #1 is an offset directory to look for settings files.*/
        String offset = "";
        if(args.length != 0){
            offset = args[0]+"\\";
        }

        /*Setup working directory.*/
        if(!Utilities.fileExists(offset+Constants.SETTINGS_FILE)) {
            Setup.setupSettingsFile(offset+Constants.SETTINGS_FILE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, "#" + Constants.DEFAULT_RENAME_DIRECTORY + ":");
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, "#" + Constants.DEFAULT_COPY_DIRECTORY + ":");
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, "#" + Constants.DEFAULT_MAX_EPISODE_COUNT + ":");
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, Constants.USER_INTERACTION+": "+Constants.TRUE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, Constants.COPY_FILES_FLAG+": "+Constants.FALSE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, Constants.MEDIA_DIVISION+": "+Constants.FALSE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, Constants.ERROR_HANDLER+": "+Constants.TRUE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE,
                    Constants.COPY_FILE_STRUCTURE+": "+Constants.DEFAULT_COPY_FILE_STRUCTURE);
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, "#" + Constants.EXCLUDE_FILE_TYPES + ":");
            ErrorHandler.printOutToFile(offset+Constants.SETTINGS_FILE, "#" + Constants.BACKUP + ":");
        }

        if(!Utilities.fileExists(offset+Constants.SPECIAL_RENAME_CASES_FILE)) {
            Setup.setupSettingsFile(offset+Constants.SPECIAL_RENAME_CASES_FILE);
            ErrorHandler.printOutToFile(offset+Constants.SPECIAL_RENAME_CASES_FILE, "###OriginalName: NewName");
        }

        if(!Utilities.fileExists(offset+Constants.SPECIAL_EP_CASES_FILE)) {
            Setup.setupSettingsFile(offset+Constants.SPECIAL_EP_CASES_FILE);
            ErrorHandler.printOutToFile(offset+Constants.SPECIAL_EP_CASES_FILE, "### S##E## where S## is last season, E## is last ep of S##");
            ErrorHandler.printOutToFile(offset+Constants.SPECIAL_EP_CASES_FILE, "###OriginalName: S##E##");
        }

        if(!Utilities.fileExists(offset+Constants.MEDIA_DIVISION_FILE)){
            Setup.setupSettingsFile(offset+Constants.MEDIA_DIVISION_FILE);
            ErrorHandler.printOutToFile(offset+Constants.MEDIA_DIVISION_FILE, "### Media Division format = {Title}: {Type}");
        }

        /*Read in settings values.*/
        HashMap<String, String> settings = Utilities.loadSettingsFile(offset+Constants.SETTINGS_FILE);
        HashMap<String, String> specialRenameCases = Utilities.loadSettingsFile(offset+Constants.SPECIAL_RENAME_CASES_FILE);
        HashMap<String, String> specialEpisodeCases = Utilities.loadSettingsFile(offset+Constants.SPECIAL_EP_CASES_FILE);
        HashMap<String, String> mediaDivisionCases = Utilities.loadSettingsFile(offset+Constants.MEDIA_DIVISION_FILE);
        String directory = settings.get(Constants.DEFAULT_RENAME_DIRECTORY);
        if(directory == null){
            Utilities.getPrintStream().println(Constants.DIRECTORY_NULL);
            return;
        }
        /*Set if the user has chosen to user error handler.*/
        String errorHandle = settings.get(Constants.ERROR_HANDLER);
        if(Constants.TRUE.equals(errorHandle)){
            ErrorHandler.errorHandle = true;
        } else if(Constants.FALSE.equals(errorHandle)){
            ErrorHandler.errorHandle = false;
        }
        String userInteraction = settings.get(Constants.USER_INTERACTION);
        /*
        *
        * Integration of the Backup module
        *
        */
        String backup = settings.get(Constants.BACKUP);
        if(backup != null) {
            String[] backupSplit = backup.split(",");
            if (Constants.TRUE.equals(backupSplit[0])) {
                Backup myBackup = new Backup(backupSplit[1], backupSplit[2], Long.parseLong(backupSplit[3]));
                List<File> safeToBackup = myBackup.performBackup();
                if(safeToBackup == null){
                    return;
                }
                for(File file : safeToBackup){
                    if(Utilities.fileExists(file.getAbsolutePath())){
                        String srcDrive = file.getAbsolutePath().split("\\\\")[0];
                        String destDrive = backupSplit[2].split("\\\\")[0];
                        String destination = file.getAbsolutePath().replaceFirst(srcDrive, destDrive);
                        boolean copy = false;
                        Utilities.getPrintStream().println("SRC: "+file.getAbsoluteFile());
                        Utilities.getPrintStream().println("DEST: "+destination);
                        Utilities.getPrintStream().println("Y/N");
                        //default to "Y" unless user input is different
                        String userInput = "Y";
                        if(Constants.TRUE.equals(userInteraction)) {
                            userInput = Utilities.userInput();
                        }
                        switch(userInput){
                            case "Y":
                            case "y":
                                copy = true;
                                break;
                            case "N":
                            case "n":
                                break;
                        }
                        if(copy) {
                            Copy.executeCopy(file.getAbsolutePath(), destination, false);
                        }
                    }
                }

            }
            return;
        }

        /*Instantiate rename module and execute rename.*/
        Rename renameModule = new Rename(settings, specialRenameCases, specialEpisodeCases);
        listFiles(new File(directory).listFiles());
        if(files.size() == 0){
            Utilities.getPrintStream().println(Constants.NO_FILES_TO_RENAME);
            return;
        }
        for(File file : files){
            MediaFile mediaFile = new MediaFile(file.toString());
            renameModule.rename(mediaFile);
            if(mediaFile.toString() == null){
                continue;
            }
            /*If the settings file has determined that the user wants user interaction.*/
            if(Constants.TRUE.equals(userInteraction)){
                userDecisionOnRename(mediaFile, file);
                continue;   //continue to next item
            }
            Utilities.rename(file, mediaFile.toString());
            Utilities.getPrintStream().println(Constants.MEDIA_RENAME_COMPLETE);
            logRename(mediaFile);
        }

        String copyFlag = settings.get(Constants.COPY_FILES_FLAG);
        if(Constants.TRUE.equals(copyFlag)) {
            //Begin move
            /*Instantiate copy module and execute copy*/
            Copy copyModule = new Copy(settings, specialRenameCases);
            /*Remove all files in the list*/
            files.clear();
            listFiles(new File(directory).listFiles());
            String mediaDivision = settings.get(Constants.MEDIA_DIVISION);
            boolean division = false;
            if(Constants.TRUE.equals(mediaDivision)){
                division = true;
            }
            for (File file : files) {
                MediaFile mediaFile = new MediaFile(file.toString());
                mediaFile.renamed();
                renameModule.rename(mediaFile);
                if(mediaFile.toString() == null){
                    continue;
                }
                //get media name from complete filename
                String mediaName = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
                //if user settings is to divide media based on type
                if(division){
                    String mediaType = mediaDivisionCases.get(mediaName);
                    if(mediaType == null){
                        if(Constants.TRUE.equals(userInteraction)) {
                            Utilities.getPrintStream().println(mediaName);
                            mediaType = getMediaTypeFromUser();
                            ErrorHandler.printOutToFile(Constants.MEDIA_DIVISION_FILE, mediaName + ": " + mediaType);
                            //allows for multiple of same file.
                            mediaDivisionCases.put(mediaName,mediaType);
                        }
                    }
                    //assign media type if determined.
                    if(mediaType != null) {
                        mediaFile.setMediaType(mediaType);
                    }
                }
                //generate the copy directory path
                copyModule.copy(mediaFile);

                Utilities.getPrintStream().println(mediaFile.getCopyLocation());
                //default to true since the user specified the copy module to be enabled.
                boolean copy = true;
                if(Constants.TRUE.equals(userInteraction)){
                    copy = userDecisionOnCopy(mediaFile);
                }
                if(copy && ensureFileStructureExists(mediaFile.getCopyLocation())) {
                    //if the user selected to copy, the execute the copy.
                    Copy.executeCopy(mediaFile.getOriginalFileName(), mediaFile.getCopyLocation());
                    /*Let the user know where the file was stored.*/
                    String filename = Utilities.parseFilenameFromPath(mediaFile.toString());
                    String path = Utilities.removeFilenameFromPath(mediaFile.toString());
                    String folder = Utilities.parseFilenameFromPath(path);
                    Utilities.getPrintStream().println("File copied: " + filename + " to dir: {CopyDir}\\" + folder);
                    Utilities.getPrintStream().println(Constants.MEDIA_COPY_COMPLETE);
                }
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

    /**
     * Helper method to determine rename based on user input.
     * Allow the user to modify the filename dynamically.
     * @param mediaFile of the media file in question to be renamed.
     */
    private static void userDecisionOnRename(MediaFile mediaFile, File file){
        Utilities.getPrintStream().println(Constants.LINE_BREAK);
        Utilities.getPrintStream().println("The rename algorithm has determined the following new name:");
        Utilities.getPrintStream().println("Original >> "+Utilities.parseFilenameFromPath(mediaFile.getOriginalFileName()));
        Utilities.getPrintStream().println("New      >> "+Utilities.parseFilenameFromPath(mediaFile.toString()));
        Utilities.getPrintStream().println("Is this correct? Y/N");
        String userInput = Utilities.userInput();
        switch(userInput){
            case "Y":
            case "y":
                Utilities.rename(file, mediaFile.toString());
                break;
            case "N":
            case "n":
                Utilities.getPrintStream().println("What is the suggested rename?");
                Utilities.getPrintStream().println("Give full name with extension?");
                userInput = Utilities.userInput();
                String path = Utilities.removeFilenameFromPath(mediaFile.toString());
                Utilities.rename(file, path+userInput);
                break;
        }
    }

    /**
     * Helper method to determine copy based on user input.
     * @param mediaFile of the media file in question to be copied.
     */
    private static boolean userDecisionOnCopy(MediaFile mediaFile){
        Utilities.getPrintStream().println(Constants.LINE_BREAK);
        Utilities.getPrintStream().println("The copy algorithm has determined the following path:");
        Utilities.getPrintStream().println(mediaFile.getCopyLocation());
        Utilities.getPrintStream().println("Is this correct? Y/N");
        String userInput = Utilities.userInput();
        switch(userInput){
            case "Y":
            case "y":
                return true;
            case "N":
            case "n":
            default:
                return false;
        }
    }

    /**
     * Log the contents of the file that was renamed.
     * This method should only be called when the renaming was purely from
     * the algorithm.
     * @param mediaFile of the file renamed.
     */
    private static void logRename(MediaFile mediaFile){
        String debugMessage = "Debug: "+mediaFile.getOriginalFileName()+
                " >> "+mediaFile.toString();
        ErrorHandler.printOutToFile(Constants.LOG_FILE,debugMessage);
    }

    /**
     * Retrieve media type from user.
     * @return user specified media type.
     */
    private static String getMediaTypeFromUser(){
        Utilities.getPrintStream().println("What is the Media type?");
        return Utilities.userInput();
    }

    /**
     * Determine if a file structure exists, if it does not, create it.
     * @param filepath of where the end of the file structure should be.
     * @return true if exists, false otherwise.
     */
    private static boolean ensureFileStructureExists(String filepath){
        filepath = Utilities.removeFilenameFromPath(filepath);
        if(Utilities.fileExists(filepath)){
            return true;
        }
        String[] subFolders = filepath.split("\\\\");
        String folderStructure = "";
        for(String folder: subFolders){
            folderStructure += folder;
            if(!Utilities.fileExists(folderStructure)){
                Utilities.makeDirectory(folderStructure);
            }
            folderStructure+="\\";
        }
        return Utilities.fileExists(filepath);
    }

}
