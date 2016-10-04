package launch;

import errorHandle.ErrorHandler;
import utilities.Utilities;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Responsible for setting up working directory with supporting
 * settings files.
 */
class Setup {

    /**
     * Generate a new copy of the specified settings file.
     * Only build new if doesn't already exist.
     * @param filePath file to generate.
     */
    static void setupSettingsFile(String filePath){
        try {
            if(Utilities.fileExists(filePath))
                return;
            PrintWriter settings = new PrintWriter(new FileWriter(filePath));
            generateDefaultFileText(settings);
            settings.close();
        }catch(Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Helper method to generate default header for file.
     * @param writer to write data out to.
     */
    private static void generateDefaultFileText(PrintWriter writer){
        writer.println("###DO NOT REMOVE");
        writer.println("###To restore default, delete this file and launch application.");
    }
}
