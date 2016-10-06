package copy;

import constants.Constants;
import rename.MediaFile;
import utilities.Utilities;

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
        /*determine what the new folder should be
        * Should be the same as the media files name*/
        String mediaName = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
        String newFolder = mediaName.trim();

        /*Determine the new destination path from the user specified destination
        * plus the new folder determination.*/
        String newPath = destination+"\\"+newFolder;
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
        /*Execute the copy command.*/
        Utilities.copyWithProgress(mediaFile.toString(),dest);

        /*If the transferred file exists, and the original
        * still exists, delete the original.*/
        if(Utilities.fileExists(dest)) {
            if (Utilities.fileExists(mediaFile.toString())) {
                Utilities.deleteFile(mediaFile.toString());
            }
        }
    }
}
