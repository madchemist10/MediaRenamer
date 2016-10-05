package utilities;

import errorHandle.ErrorHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Utility class to contain helper methods needed throughout
 * code. All methods defined here should be static and self contained.
 */
public class Utilities {

    /**
     * Private constructor to defeat instantiation.
     */
    private Utilities(){
    }

    /**
     * Generate hashMap that has all the loaded settings for
     * this application.
     * @param filepath of the file to load settings from.
     * @return Map of key to value pairs for settings denoted by the user.
     */
    public static HashMap<String, String> loadSettingsFile(String filepath){
        HashMap<String, String> settingsMap = new HashMap<>();
        String line;
        String delimiter = ": ";
        try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null){
                if(!line.contains("###")) {
                    String[] new_data = line.split(delimiter);
                    settingsMap.put(new_data[0], new_data[1]);
                }
            }
            br.close();
        }catch(Exception e) {
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        return settingsMap;
    }

    /**
     * Check if File Exists
     * @param filePath to be checked for existence.
     * @return true if exists, false if not exists.
     */
    public static boolean fileExists(String filePath){
        return new File(filePath).exists();
    }

    /**
     * Retrieve user input for a given task.
     * @return String representation of the user's input.
     */
    public static String userInput(){
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    /**
     * Perform the renaming of a given file to the .toString() of the
     * media file. Output to log file for logging.
     * @param file of the original file to be renamed.
     * @param newFilename of the newly found rename.
     * @return true if rename successful, false if failure
     */
    public static boolean rename(File file, String newFilename){
        return file.renameTo(new File(newFilename));
    }

    /**
     * Retrieve the filename portion of the path that is given.
     * @param path to retrieve filename from.
     * @return filename parsed from path.
     */
    public static String parseFilenameFromPath(String path){
        String forwardSlash = "\\\\";
        String[] splitPath = path.split(forwardSlash);
        return splitPath[splitPath.length-1];
    }

    /**
     * Parse the filename from the path.
     * @param path to remove filename from.
     * @return path without filename at end.
     */
    public static String removeFilenameFromPath(String path){
        String forwardSlash = "\\\\";
        String[] splitPath = path.split(forwardSlash);
        String temp = "";
        for(int i = 0; i < splitPath.length-1; i++){
            temp+=splitPath[i]+"\\";
        }
        return temp;
    }

    /**
     * Copy File from one location to another
     * @param src of where the current file lives.
     * @param dest of where the file should live.
     */
    public static void copyFile(String src, String dest){
        try {
            Files.copy(Paths.get(src), Paths.get(dest));
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Make directory
     * @param directoryPath to make
     * @return true if directory was made successfully, false on error.
     */
    public static boolean makeDirectory(String directoryPath){
        return new File(directoryPath).mkdir();
    }
}
