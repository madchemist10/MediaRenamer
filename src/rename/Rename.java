package rename;

import errorHandle.ErrorHandler;

import java.util.HashMap;

/**
 * Media rename core algorithms.
 */
public class Rename {
    /**Map of settings to the actual setting values.*/
    private HashMap<String, String> settings;
    /**Map of original media names to preferred media names.*/
    private HashMap<String, String> specialRenameCases;
    /**Map of special case episode values.
     * Used when show uses different numbering scheme than desired.
     * Example: SHOW S01E26 should be SHOW S02E01.
     * Offset value of 25 should be given and specified in format of
     * S01E25, giving the previous season and final episode of that season.*/
    private HashMap<String, String> specialEpisodeCases;

    /**
     * Create a new rename object, used for generating new media files to assign
     * formatted names.
     * @param settings user specified settings for media rename.
     * @param specialRenameCases special cases for assigning user specified names to determined names.
     * @param specialEpisodeCases special episode cases that are not as desired.
     */
    public Rename(HashMap<String, String> settings, HashMap<String, String> specialRenameCases, HashMap<String, String> specialEpisodeCases) {
        this.settings = settings;
        this.specialRenameCases = specialRenameCases;
        this.specialEpisodeCases = specialEpisodeCases;
    }

    /**
     * Algorithm to rename a media file that is given.
     * No need to return the mediaFile as only dot operators
     * are called on the passed in mediaFile.
     * @param mediaFile to be renamed.
     */
    public void rename(MediaFile mediaFile){
        String tempFileName = mediaFile.getOriginalFileName();
        /*Assign file extension*/
        mediaFile.setFileExt(getFileExt(tempFileName));
        /*Strip away everything in parentheses and brackets.*/
        //replace everything enclosed in []
        tempFileName = tempFileName.replaceAll("\\([^)]*\\)","");
        //replace everything enclosed in ()
        tempFileName = tempFileName.replaceAll("\\[[^]]*\\]","");
        //replace all "."
        tempFileName = tempFileName.replaceAll("\\."," ");
        //replace all " - "
        tempFileName = tempFileName.replaceAll("[\\s+](-)[\\s+]"," ");
        System.out.println(tempFileName);
    }

    /**
     * Helper method to locate the file extension.
     * @param fileName to search for file ext in.
     * @return file ext if found, null otherwise.
     */
    private static String getFileExt(String fileName){
        String fileExt = null;
        try{
            int index = fileName.lastIndexOf(".");
            fileExt = fileName.substring(index);
        }catch (Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        return fileExt;
    }
}
