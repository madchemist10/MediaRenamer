package utilities;

import errorHandle.ErrorHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

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
                String[] new_data = line.split(delimiter);
                settingsMap.put(new_data[0],new_data[1]);
            }
            br.close();
        }catch(Exception e) {
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        return settingsMap;
    }
}
