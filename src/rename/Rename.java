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
        //replace all x### {can be x264 or x265}
        tempFileName = tempFileName.replaceAll("[x]\\d{3}","");
        //replace all ###p {can be 1080p}
        tempFileName = tempFileName.replaceAll("\\d{4}[p]","");
        //replace all ###p {can be 720p}
        tempFileName = tempFileName.replaceAll("\\d{3}[p]","");

        //assign episode number to mediaFile
        String episodeNumber = parseEpisodeNumber(tempFileName);
        mediaFile.setEpisodeNumber(episodeNumber);
        //assign season number to mediaFile
        String seasonNumber = parseSeasonNumber(tempFileName);
        mediaFile.setSeasonNumber(seasonNumber);

        /*replace all S##E## and everything after
        * we only want to keep what is before S##E##*/
        tempFileName = tempFileName.replaceAll("[S]\\d{2}[E]\\d{2}.+","");

        /*Handle special case where season could be pretexted with
        * "S"
        * Need to remove S from determined mediaName*/
        tempFileName = tempFileName.replaceAll("[S]\\d{2}",""); //"S##"
        tempFileName = tempFileName.replaceAll("[S]\\d","");    //"S#"

        /*Handle special case where episode could be pretexted with
        * "E"
        * Need to remove E from determined mediaName*/
        tempFileName = tempFileName.replaceAll("[E]\\d{2}",""); //"E##"
        tempFileName = tempFileName.replaceAll("[E]\\d",""); //"E#"

        //remove all numbers and we then have mediaName
        tempFileName = tempFileName.replaceAll("[0-9]","");
        //remove prepended or trailing spaces
        tempFileName = tempFileName.trim();

        //assign media name
        mediaFile.setMediaName(tempFileName);

        /*Attempt to replace current media file name with user specified filename.*/
        exchangeFileName(mediaFile);

        /*Attempt to replace current episode number with user specified one.*/
        verifyEpisodeNumber(mediaFile);
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

    /**
     * Episode parsing from given filename.
     * @param filename to search for episodeNumber.
     * @return parsed episode number if exists, otherwise null.
     */
    private static String parseEpisodeNumber(String filename){
        /*Remove all alphabetic characters and replace with empty space*/
        String numbersOnly = filename.replaceAll("[^0-9]+","");
        numbersOnly = numbersOnly.replaceAll(" ","");   //remove all spaces
        numbersOnly = numbersOnly.trim();   //remove leading/trailing spaces
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
        /*if the length is 4, we have 2 digit episode & 2 digit season or
        * 3 digit episode & 1 digit season*/
        if(numbersOnly.length() == 4){
            //check 3 digit first
            String potentialEpisode = numbersOnly.substring(1);
            if(filename.contains(potentialEpisode)){
                return potentialEpisode;
            }
            //check 2 digit next
            potentialEpisode = numbersOnly.substring(2);
            if(filename.contains(potentialEpisode)){
                return potentialEpisode;
            }
        }
        //any other set of numbers and something went wrong.
        return null;
    }

    /**
     * Season parsing from given filename.
     * Worst case, parsing could not determine season number, therefore
     * we use default case of "01"
     * @param filename to search for seasonNumber.
     * @return parsed season number if exists, otherwise null.
     */
    private static String parseSeasonNumber(String filename){
        String defaultSeasonNumber = "01";
        String numbersOnly = filename.replaceAll("[^0-9]+","");
        numbersOnly = numbersOnly.trim();
        /*If only two numbers found, we have only episode
        * default season is "01"*/
        if(numbersOnly.length() == 2){
            return defaultSeasonNumber;
        }
        /*If three numbers found, we have single digit season & 2 digit ep
        * or 3 digit episode number.
        * If 3 digit episode, then use default season number.*/
        if(numbersOnly.length() == 3){
            if(filename.contains(numbersOnly)){
                return defaultSeasonNumber;
            }
            return numbersOnly.substring(0,1);
        }
        /*If four numbers found, we have 2 digit season & 2 digit episode
        * or 1 digit season & 3 digit episode.
        * If 3 digit episode, return first number as season number.
        * If 2 digit episode, return first two numbers as season.*/
        if(numbersOnly.length() == 4){
            //check 3 digit first
            String potentialEpisode = numbersOnly.substring(1);
            if(filename.contains(potentialEpisode)){
                return numbersOnly.substring(0,1);
            }
            //check 2 digit next
            potentialEpisode = numbersOnly.substring(2);
            if(filename.contains(potentialEpisode)){
                return numbersOnly.substring(0,2);
            }
        }
        //could not be determined so use default.
        return defaultSeasonNumber;
    }

    /**
     * Helper method to exchange a given filename with the
     * user specified filename.
     * @param mediaFile to exchange filenames with.
     */
    private void exchangeFileName(MediaFile mediaFile){
        for(String originalName: specialRenameCases.keySet()){
            if(originalName.equals(mediaFile.getMediaName())){
                mediaFile.setMediaName(specialRenameCases.get(originalName));
            }
        }
    }

    /**
     * Verify episode number assumes the user predefined data is correct.
     * User input should be in format of S##E## and consist of the last
     * episode number of the given season. This allows this parser to
     * intelligently determine if the current parsed episode number
     * should be offset based on user specification.
     * @param mediaFile to verify episode number with.
     */
    private void verifyEpisodeNumber(MediaFile mediaFile){
        for(String originalName: specialEpisodeCases.keySet()){
            if(originalName.equals(mediaFile.getMediaName())){
                String episodeNumber = mediaFile.getEpisodeNumber();
                if(episodeNumber == null){
                    return; //cant continue if episode number not defined.
                }
                //get int representation of episode number
                int epNum = Integer.parseInt(episodeNumber);
                String seasonNumber = mediaFile.getSeasonNumber();
                if(seasonNumber == null){
                    return; //cant continue if season number not defined.
                }
                int sNum = Integer.parseInt(seasonNumber);
                String userSpecialCase = specialEpisodeCases.get(originalName);
                String seasonNum = userSpecialCase.replaceAll("[E]\\d{2}","");
                String episodeNum = userSpecialCase.replaceAll("[S]\\d{2}","");
                seasonNum = seasonNum.replaceAll("[S]","");
                episodeNum = episodeNum.replaceAll("[E]","");
                int userEp = Integer.parseInt(episodeNum);
                int userS = Integer.parseInt(seasonNum);
                /*If we have determined that the parsed season number is equal to
                * one + the user season number,
                * and
                * the parsed episode number is greater than the user defined episode
                * decrement the current defined parsed ep by the user ep*/
                if(sNum == userS+1 && epNum > userEp){
                    mediaFile.setEpisodeNumber(Integer.toString(epNum - userEp));
                }
            }
        }
    }
}
