package errorHandle;

import constants.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Error Handler to Output certain issues to logFile.
 */
public class ErrorHandler {

    /**
     * Default error recording method.
     * @param error string to output to default log file.
     */
    public static void printError(String error) {
        printOutToFile(Constants.LOG_FILE,error);
    }

    /**
     * Output new data to end of existing file.
     * If file does not exist, file is created.
     * @param filePath of file to add data to.
     * @param data to add to the given filePath.
     */
    public static void printOutToFile(String filePath, String data){
        try{
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath,true)));
            writer.println(data);
            writer.close();
        } catch (Exception e){
            printError((e.getClass().getName()+": "+e.getMessage()));
        }
    }
}
