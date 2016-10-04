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
        //remove file extension from temp filename
        tempFileName = tempFileName.replace("."+mediaFile.getFileExt(),"");
        //remove prepended or trailing spaces
        tempFileName = tempFileName.trim();
        /*Strip away everything in parentheses and brackets.*/
        //replace everything enclosed in []
        tempFileName = tempFileName.replaceAll("\\([^)]*\\)","");
        //replace everything enclosed in ()
        tempFileName = tempFileName.replaceAll("\\[[^]]*\\]","");
        //replace all "."
        tempFileName = tempFileName.replaceAll("\\."," ");
        //replace all " - "
        tempFileName = tempFileName.replaceAll("[\\s+](-)[\\s+]"," ");

        //assign episode number to mediaFile
        String episodeNumber = parseEpisodeNumber(tempFileName);
        episodeNumber = episodeNumber.trim();
        mediaFile.setEpisodeNumber(episodeNumber);
        //assign season number to mediaFile
        String seasonNumber = parseSeasonNumber(tempFileName);
        seasonNumber = seasonNumber.trim();
        mediaFile.setSeasonNumber(seasonNumber);

        //remove all numbers and we then have mediaName
        tempFileName = tempFileName.replaceAll("[0-9]","");
        //remove prepended or trailing spaces
        tempFileName = tempFileName.trim();

        //assign media name
        mediaFile.setMediaName(tempFileName);
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
            fileExt = fileName.substring(index+1);  //we don't want the dot
        }catch (Exception e){
            ErrorHandler.printError(e.getClass().getName() + ": " + e.getMessage());
        }
        return fileExt;
    }

    private static String parseEpisodeNumber(String filename){
        /*Remove all alphabetic characters and replace with empty space*/
        String numbersOnly = filename.replaceAll("\\p{Alpha}","");
        numbersOnly = numbersOnly.trim();   //remove spaces
        /*The episode number will be the last 2-3 digits in the filename*/
        //if the length if 2, then we know we only have episodeNumber
        if(numbersOnly.length() == 2){
            return numbersOnly;
        }
        /*if the length is 3, then we know we have 3 digit episodeNumber or
        * season number and 2 digit episodeNumber.
        * To determine this, we can look at the original filename and see if this
        * exact sequence exists in the original.
        * If the exact sequence exists, then we know we have only episode number*/
        if(numbersOnly.length() == 3){
            if(filename.contains(numbersOnly)){
                return numbersOnly;
            }
            return numbersOnly.substring(1);
        }
        /*if the length is 4, then we know we have 2 digit episodeNumber*/
        if(numbersOnly.length() == 4){
            return numbersOnly;
        }
        //any other set of numbers and something went wrong.
        return null;
    }

    private static String parseSeasonNumber(String filename){
        String numbersOnly = filename.replaceAll("\\p{Alpha}","");
        return numbersOnly;
    }
}
