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
    /**Map of original media names to preferred media names.*/
    private HashMap<String, String> specialRenameCases;

    /**
     * Create a new copy module to copy the media file
     * to the user specified copy directory.
     * @param settings user specified settings for media copy.
     * @param specialRenameCases special cases for assigning user specified names to determined names.
     */
    public Copy(HashMap<String, String> settings, HashMap<String, String> specialRenameCases) {
        this.settings = settings;
        this.specialRenameCases = specialRenameCases;
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

        /*If the user has defined a file structure, user this user defined file structure.*/
        String userCopyFileStructure = settings.get(Constants.COPY_FILE_STRUCTURE);
        if(userCopyFileStructure != null && !userCopyFileStructure.equals("")){
            String copyLocation = userCopyFileStructure.replace(Constants.TITLE_REPLACEMENT,newFolder);
            String mediaType = mediaFile.getMediaType();
            if(mediaType != null) {
                String seasonNumber = Integer.toString(Integer.parseInt(mediaFile.getSeasonNumber()));
                copyLocation = copyLocation.replace(Constants.SEASON_REPLACEMENT, Constants.SEASON + " " + seasonNumber);
                mediaName = Utilities.parseFilenameFromPath(mediaFile.toString());
                mediaFile.setCopyLocation(destination + "\\" + mediaType + "\\" + copyLocation + "\\" + mediaName);
                return;
            }
        }

        //predict where the file belongs.
        predictiveCopyAlgorithm(mediaFile);

        String predictiveCopyLocation = mediaFile.getCopyLocation();
        if(predictiveCopyLocation != null){
            return;
        }

        /*Determine the new destination path from the user specified destination
        * plus the new folder determination.*/
        String mediaType = "";
        String tempMediaType = mediaFile.getMediaType();
        if(tempMediaType != null){
            /*We can have a movie file*/
            if(tempMediaType.equals(Constants.MOVIES)){
                String media = Utilities.parseFilenameFromPath(mediaFile.toString());
                String copyLocation = destination+"\\"+Constants.MOVIES+"\\"+media;
                mediaFile.setCopyLocation(copyLocation);
                return;
            }
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

        mediaFile.setCopyLocation(dest);
    }

    /**
     * Execute the copy and delete the old file if the new file exists
     * and the file size of the original and new are equal.
     * @param source of the file to copy.
     * @param destination of where the file belongs.
     */
    public static void executeCopy(String source, String destination){
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
     * This predictive algorithm only works with a pre-existing file structure of:
     *  {title}\{title} Season {SNum} or {title}.
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
        handleDefaultMediaFileStructureCases(mediaFile, mediaName, destination);
        /*If we are unable to find a match, lets see if we can do a replacement
        * on the name from the rename special case file.*/
        for(String originalName: specialRenameCases.keySet()){
            if(("$$"+mediaName).equals(originalName)){
                mediaName = specialRenameCases.get(originalName);
                break;
            }
        }
        handleDefaultMediaFileStructureCases(mediaFile, mediaName, destination);
    }

    /**
     * Helper method to determine where the file belongs in the default file structure.
     * @param mediaFile of the file to determine placement.
     * @param mediaName of what the media file folder is changed to. (Special Rename Case File)
     * @param destination of where the parent directory of where the file belongs.
     */
    private static void handleDefaultMediaFileStructureCases(MediaFile mediaFile, String mediaName, String destination){
        String seasonNumber = Integer.toString(Integer.parseInt(mediaFile.getSeasonNumber()));
        String seasonFolder = destination+"\\"+mediaName+"\\"+mediaName+" "+Constants.SEASON+" "+seasonNumber;
        String media = Utilities.parseFilenameFromPath(mediaFile.toString());
        if(Utilities.fileExists(seasonFolder)){
            mediaFile.setCopyLocation(seasonFolder+"\\"+media);
            return;
        }
        seasonFolder = destination+"\\"+mediaName+" "+Constants.SEASON+" "+seasonNumber;
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
