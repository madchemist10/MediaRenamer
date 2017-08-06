package backup;

import copy.Copy;
import utilities.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to iterate over a directory of data,
 * store a mapping of what is available, and replicate updated files onto
 * a second directory of data. Overwriting any files that share a different
 * creation date.
 */
public class Backup {

    /**Reference to the static list of files. Container for
     * the list files method.*/
    private static List<File> files;
    /**Source location filepath and filename*/
    private String source;
    /**Destination location filepath*/
    private String destination;
    /**Last date to compare for files.*/
    private static long last = 0;
    private long dateGiven = 0;


    /**
     * Create a new backup object.
     * @param source directory of where new files are.
     * @param destination directory of where new files should backup to.
     */
    public Backup(String source, String destination){
        this.source = source;
        this.destination = destination;
    }

    public Backup(String source, String destination, long date){
        this(source, destination);
        this.dateGiven = date;
    }

    /**
     * Perform the back up of files that were created after
     * the set date. Loop through all files in the src and destination
     * directories, compare the name and date of modification.
     */
    public List<File> performBackup(){
        files = new ArrayList<>();
        listFiles(new File(this.source).listFiles());
        List<File> sourceFiles = files;
        findNewestDate(new File(this.destination).listFiles());
        String srcDrive = this.source.split("\\\\")[0];
        String destDrive = this.destination.split("\\\\")[0];
        if(!srcDrive.contains(":") && !destDrive.contains(":")){
            return null;
        }
        List<File> safeToBackup = new ArrayList<>();
        for(File src : sourceFiles){
            /*By default do not backup unless no destination files*/
            boolean backup = false;
            boolean dateMatching = false;
            if(dateGiven != 0 && src.lastModified() > dateGiven){
                dateMatching = true;
            }
            else if(src.lastModified() > last) {
                dateMatching = true;
            }
            String newPath = swapDriveLetters(src.getAbsolutePath(), srcDrive, destDrive);
            File destFile = new File(newPath);
            if(!Utilities.fileExists(destFile.getAbsolutePath())){
                backup = true;
            }
            else if(src.length() != destFile.length()){
                backup = true;
            }
            if(backup && dateMatching){
                safeToBackup.add(src);
            }
        }
        return safeToBackup;
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
                if(file.lastModified() > last)
                    files.add(file);
        }
    }

    /**
     * Recursive listFiles structure
     * @param myFiles array of files to add to list.
     */
    private static void findNewestDate(File[] myFiles){
        for(File file : myFiles){
            if (file.isDirectory())
                findNewestDate(file.listFiles());
            else if(file.lastModified() > last) {
                last = file.lastModified();
            }
        }
    }

    public static boolean compareFilePaths(String path_1, String path_2){
        String[] splitPath_1 = path_1.split("\\\\");
        String[] splitPath_2 = path_2.split("\\\\");
        for(int i = 1; i < splitPath_1.length-1; i++){
            if(!splitPath_1[i].equals(splitPath_2[i])){
                return false;
            }
        }
        return true;
    }

    public static String swapDriveLetters(String path_1, String srcDrive, String destDrive){
        return path_1.replaceFirst(srcDrive, destDrive);
    }

}
