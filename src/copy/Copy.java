package copy;

import constants.Constants;
import rename.MediaFile;
import utilities.Utilities;

import java.io.File;
import java.util.HashMap;

/**
 * This copy module is responsible for placing MediaFiles
 * in their respective directories.
 */
public class Copy {
    /**Map of settings to the actual setting values.*/
    private HashMap<String, String> settings;

    /**
     * Create a new copy module to copy the media file
     * to the user specified copy directory.
     * @param settings user specified settings for media copy.
     */
    public Copy(HashMap<String, String> settings) {
        this.settings = settings;
    }

    /**
     * Algorithm to determine where the media file should
     * live after the move.
     * @param mediaFile to by moved.
     */
    public void copy(MediaFile mediaFile){
        String destination = settings.get(Constants.DEFAULT_COPY_DIRECTORY);
        if(destination == null){
            return; //cannot continue if destination is null
        }
        //predict where the file belongs.
        predictiveCopyAlgorithm(mediaFile);

        String copyLocation = mediaFile.getCopyLocation();
        if(copyLocation != null){
            executeCopy(mediaFile.getOriginalFileName(),copyLocation);
            return;
        }

        /*determine what the new folder should be
        * Should be the same as the media files name*/
        String mediaName = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
        String newFolder = mediaName.trim();

        /*Determine the new destination path from the user specified destination
        * plus the new folder determination.*/
        String mediaType = "";
        String tempMediaType = mediaFile.getMediaType();
        if(tempMediaType != null){
            mediaType = tempMediaType+"\\";
        }
        String preNewPath = destination+"\\"+mediaType;
        if(!Utilities.fileExists(preNewPath)){
            Utilities.makeDirectory(preNewPath);
        }
        String newPath = preNewPath+newFolder;
        newPath = newPath.trim();

        /*Extract the filename of the file to be copied.*/
        String filename = Utilities.parseFilenameFromPath(mediaFile.toString());
        filename = filename.trim();

        /*Build the complete destination path.*/
        String dest = newPath+"\\"+filename;

        /*Need to create the directory if it does not exist.*/
        if(!Utilities.fileExists(newPath)) {
            Utilities.makeDirectory(newPath);
        }
        copyLocation = dest;
        String originalMediaLocation = mediaFile.toString().replace(mediaType,"");

        executeCopy(originalMediaLocation,copyLocation);
    }

    /**
     * Execute the copy and delete the old file if the new file exists
     * and the file size of the original and new are equal.
     * @param source of the file to copy.
     * @param destination of where the file belongs.
     */
    private static void executeCopy(String source, String destination){
        long originalFileSize = new File(source).length();
        /*Execute the copy command.*/
        Utilities.copyWithProgress(source, destination);
        long copiedFileSize = new File(destination).length();
        /*If the transferred file exists and the file size
        * is equal to the original, and the original
        * still exists, delete the original.*/
        if(Utilities.fileExists(destination) && originalFileSize == copiedFileSize){
            if(Utilities.fileExists(source)){
                Utilities.deleteFile(source);
            }
        }
    }

    /**
     * Locate where the media file should be copied to.
     * Determines where the file belongs based on the current file structure.
     * @param mediaFile that should be copied.
     */
    private void predictiveCopyAlgorithm(MediaFile mediaFile){
        String destination = settings.get(Constants.DEFAULT_COPY_DIRECTORY);
        //return if destination does not exist in the file structure.
        if(!Utilities.fileExists(destination)){
            return;
        }
        String mediaType = mediaFile.getMediaType();
        //assign media type to destination.
        destination += "\\"+mediaType;
        //return if destination does not exist in the file structure.
        if(!Utilities.fileExists(destination)){
            return;
        }
        /*We have two cases where the file can be located.
        * Either the file is located in:
        * {title}\{title} Season {SNum}\
        * or
        * {title} Season {SNum}\
        * or
        * {title}\*/
        String mediaName = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
        String seasonNumber = Integer.toString(Integer.parseInt(mediaFile.getSeasonNumber()));
        String seasonFolder = destination+"\\"+mediaName+"\\"+mediaName+" Season "+seasonNumber;
        String media = Utilities.parseFilenameFromPath(mediaFile.toString());
        if(Utilities.fileExists(seasonFolder)){
            mediaFile.setCopyLocation(seasonFolder+"\\"+media);
            return;
        }
        seasonFolder = destination+"\\"+mediaName+" Season "+seasonNumber;
        if(Utilities.fileExists(seasonFolder)){
            mediaFile.setCopyLocation(seasonFolder+"\\"+media);
            return;
        }
        seasonFolder = destination+"\\"+mediaName;
        if(Utilities.fileExists(seasonFolder)){
            mediaFile.setCopyLocation(seasonFolder+"\\"+media);
        }
    }
}
