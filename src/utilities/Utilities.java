package utilities;

import constants.Constants;
import errorHandle.ErrorHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
     * Helper routine to generate a hash map of all
     * copy locations that are specified by the settings file
     * input from {@link Constants#DEFAULT_COPY_DIRECTORY}.
     * @param input String value that is pulled from the settings file,
     *              this value should be in the following format:
     *              #MediaType#-#Location#;...
     * @return map of all locations that may have data copied.
     */
    public static Map<String, String> determineCopyLoc(String input){
        Map<String, String> map = new HashMap<>();
        String[] locations = input.split(";");
        if(locations.length == 0){
            return null;
        }
        for(String loc: locations){
            String[] l = loc.split("-");
            if(l.length == 2){
                map.put(l[0], l[1]);
            }
        }
        return map;
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
                if(!line.startsWith("###")) {
                    String[] new_data = line.split(delimiter);
                    String key = new_data[0];
                    String value = "";
                    //we have valid key and value
                    if(new_data.length == 2){
                        value = new_data[1];
                        settingsMap.put(key,value);
                    }
                    //we only have a key
                    if(new_data.length == 1){
                        settingsMap.put(key,value);
                    }
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
     * Allow the providing of system.in as InputStream.
     * Allows for full testing of runner.
     * @return System.in
     */
    private static InputStream getInputStream(){
        return System.in;
    }

    /**
     * Allow the providing of system.out as OutputStream.
     * Allows for full testing of runner.
     * @return getPrintStream()
     */
    public static PrintStream getPrintStream(){
        return System.out;
    }

    /**
     * Retrieve user input for a given task.
     * @return String representation of the user's input.
     */
    public static String userInput(){
        Scanner input = new Scanner(getInputStream());
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
     * Get Input Stream
     * @param filePath of where to retrieve input stream from.
     * @return FileInputStream of the filePath
     */
    public static FileInputStream getInputStream(String filePath){
        FileInputStream inputStream;
        try{
            inputStream = new FileInputStream(filePath);
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
        return inputStream;
    }

    /**
     * Get Output Stream
     * @param filePath of where to retrieve output stream from.
     * @return FileOutputStream of the filePath
     */
    public static FileOutputStream getOutputStream(String filePath){
        FileOutputStream outputStream;
        try{
            outputStream = new FileOutputStream(filePath);
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
        return outputStream;
    }

    /**
     * Copy File from one location to another with progress.
     * @param src of where the current file lives.
     * @param dest of where the file should live.
     */
    public static void copyWithProgress(String src, String dest){
        File srcFile = new File(src);
        long fileSize = srcFile.length();
        FileInputStream srcStream = getInputStream(src);
        FileOutputStream destStream = getOutputStream(dest);
        if (srcStream == null || destStream == null)
            return;
        byte[] byteBuffer = new byte[Constants.DEFAULT_BYTE_BUFFER_SIZE];
        try {
            int bytesRead;
            long totalBytesRead = 0;
            while ((bytesRead = srcStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                totalBytesRead+=bytesRead;
                destStream.write(byteBuffer,0,bytesRead);
                double percentComplete = ((double)totalBytesRead/(double)fileSize)*100;
                String output = String.format("%.0f",percentComplete);
                writeToCMD(""+output+"%\r");
                byteBuffer = new byte[Constants.DEFAULT_BYTE_BUFFER_SIZE];
            }
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        try{
            srcStream.close();
            destStream.close();
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        System.out.println("Transfer complete.");
    }

    /**
     * Output data to standard out in command prompt.
     * @param output of string to send to cmd.
     */
    public static void writeToCMD(String output) throws Exception{
        System.out.print(output);
    }

    /**
     * Make directory
     * @param directoryPath to make
     * @return true if directory was made successfully, false on error.
     */
    public static boolean makeDirectory(String directoryPath){
        return new File(directoryPath).mkdir();
    }


    /**
     * Delete Folder from given location.
     * Remove folder and all containing files.
     * @param src of where the current folder lives.
     */
    public static void deleteFolder(String src){
        File file = new File(src);
        try {
            deleteDir(file);
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Recursive call to delete every element in a file structure.
     * @param file root level to delete at.
     */
    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    /**
     * Delete file from given location
     * @param src of where the current file lives
     */
    public static void deleteFile(String src){
        try {
            Files.delete(Paths.get(src));
        } catch (IOException e) {
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
