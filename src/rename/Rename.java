package rename;

import constants.Constants;
import errorHandle.ErrorHandler;
import utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

        //tempFileName could have path appended to filename
        String path = Utilities.removeFilenameFromPath(tempFileName);
        path = path.trim();

        String filename = Utilities.parseFilenameFromPath(tempFileName);
        tempFileName = filename.trim();

        /*Assign file extension*/
        mediaFile.setFileExt(getFileExt(tempFileName));
        //remove file extension from temp filename
        tempFileName = tempFileName.replace("."+mediaFile.getFileExt(),"");

        //remove prepended or trailing spaces
        tempFileName = tempFileName.trim();
        /*Strip away everything in parentheses and brackets.*/
        //replace everything enclosed in ()
        tempFileName = tempFileName.replaceAll("\\([^)]*\\)","");
        //replace everything enclosed in []
        tempFileName = tempFileName.replaceAll("\\[[^]]*\\]","");
        //replace all "."
        tempFileName = tempFileName.replaceAll("\\."," ");
        //replace all " - "
        tempFileName = tempFileName.replaceAll("\\s+(-)\\s+"," ");
        //replace all x### {can be x264 or x265}
        tempFileName = tempFileName.replaceAll("x\\d{3}","");
        //replace all ####p {can be 1080p, or 720p}
        tempFileName = tempFileName.replaceAll("\\d{3,4}p","");

        //assign episode number to mediaFile
        String UserMaxEpisodeCount = settings.get(Constants.DEFAULT_MAX_EPISODE_COUNT);
        int maxNum = 999;
        if(UserMaxEpisodeCount != null){
            maxNum = Integer.parseInt(UserMaxEpisodeCount);
        }
        String episodeNumber = parseEpisodeNumber(tempFileName, maxNum);
        mediaFile.setEpisodeNumber(episodeNumber);

        //assign season number to mediaFile
        String seasonNumber = parseSeasonNumber(tempFileName, maxNum);
        mediaFile.setSeasonNumber(seasonNumber);

        //assign year to mediaFile
        String year = parseYear(tempFileName);
        mediaFile.setYear(year);

        /*Remove the following cases:
        * where stuff can be any alphabetic char, space, or "-"
        * S#{stuff}
        * S##{stuff}
        * E#{stuff}
        * E##{stuff}
        * ###{stuff}
        * ####{stuff}
        */
        tempFileName = tempFileName.replaceAll("(((S|E)\\d{1,2})+|\\d{3,4})[-\\w\\s]+","");

        /*Replace all instances of the keyword "Episode "*/
        tempFileName = tempFileName.replaceAll("(Episode|episode)\\s*","");

        /*If the filename contains the keyword special or Special,
        * then season number must be 0.
        * Only if the keyword special appears as the last word in the filename.*/
        if(tempFileName.contains("Special") || tempFileName.contains("special")){
            mediaFile.setSeasonNumber("0");
            tempFileName = tempFileName.replaceAll("(Special|special)\\s*","");
        }

        /*Attempt to replace instance of episode number*/
        if(episodeNumber != null){
            tempFileName = tempFileName.replaceAll(episodeNumber,"");
        }
        /*Attempt to replace instance of season number*/
        tempFileName = tempFileName.replaceAll(seasonNumber, "");

        //remove prepended or trailing spaces
        tempFileName = tempFileName.trim();

        //join the path back to the original if we should do so
        if(path.length()>0) {
            tempFileName = path + tempFileName;
        }

        //assign media name
        mediaFile.setMediaName(tempFileName);

        /*Attempt to replace current media file name with user specified filename.*/
        exchangeFileName(mediaFile);

        /*if the name changed, means it was exchanged and do not do replacement
        * if name not changed, perform replacement.*/
        if(tempFileName.equals(mediaFile.getMediaName())){
            filename = Utilities.parseFilenameFromPath(tempFileName);
            filename = filename.trim();
            /*if there is a "  " {double space} followed by a character and
            * other characters, remove everything following the "  "{Alpha}*/
            filename = filename.replaceAll("\\s{2}[-\\s\\w]+","");
            mediaFile.setMediaName(path+filename);
            //attempt to re-exchange the filename
            exchangeFileName(mediaFile);
        }

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
        if(fileName.contains(".")) {
            int index = fileName.lastIndexOf(".");
            /*Ensure valid file extension of length 3*/
            String tempFileExt = fileName.substring(index + 1);  //we don't want the dot
            if(tempFileExt.length() == 3) {
                fileExt = tempFileExt;
            }
        }
        return fileExt;
    }

    /**
     * Episode parsing from given filename.
     * @param filename to search for episodeNumber.
     * @return parsed episode number if exists, otherwise null.
     */
    private static String parseEpisodeNumber(String filename, int maxEpisode){
        filename = Utilities.parseFilenameFromPath(filename);
        /*Remove all alphabetic characters and replace with empty space*/
        String numbersOnly = filename.replaceAll("[^0-9]+","");
        numbersOnly = numbersOnly.replaceAll(" ","");   //remove all spaces
        numbersOnly = numbersOnly.trim();   //remove leading/trailing spaces
        /*The episode number will be the last 2-3 digits in the filename*/
        //if the length is 1 or 2, then we know we only have episodeNumber
        if(numbersOnly.length() == 2 || numbersOnly.length() == 1){
            return numbersOnly;
        }
        /*if the length is 3, then we know we have 3 digit episodeNumber or
        * season number and 2 digit episodeNumber.
        * To determine this, we can look at the original filename and see if this
        * exact sequence exists in the original.
        * If the exact sequence exists, then we know we have only episode number*/
        if(numbersOnly.length() == 3){
            /*If the current parsed digits are less than maximum  allowed for episode*/
            if(Integer.parseInt(numbersOnly) < maxEpisode){
                if(filename.contains(numbersOnly)){
                    return numbersOnly;
                }
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
        /*If we reached this far, the format is not normal, there are too
        * many numbers in the string. Using some simple predictive knowledge,
        * if the first character of the numbers found exists by itself (with
        * space following), it is
        * possible that this number belongs to the title. If we act on this assumption
        * that the first character belongs to the title, reprocess the last digits
        * through this algorithm.*/
        if(numbersOnly.length() > 4){
            if(filename.contains(numbersOnly.substring(0,1)+" ")){
                //replace first in case actual number is part of episode
                String temp = filename.replaceFirst(numbersOnly.substring(0,1)+" ","").trim();
                return parseEpisodeNumber(temp,maxEpisode);
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
    private static String parseSeasonNumber(String filename, int maxEpisode){
        filename = Utilities.parseFilenameFromPath(filename);
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
            /*If the current parsed digits are less than maximum allowed for episode*/
            if(Integer.parseInt(numbersOnly) < maxEpisode) {
                if (filename.contains(numbersOnly)) {
                    return defaultSeasonNumber;
                }
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
        /*If we reached this far, the format is not normal, there are too
        * many numbers in the string. Using some simple predictive knowledge,
        * if the first character of the numbers found exists by itself, it is
        * possible that this number belongs to the title. If we act on this assumption
        * that the first character belongs to the title, reprocess the last digits
        * through this algorithm.*/
        if(numbersOnly.length() > 4){
            if(filename.contains(numbersOnly.substring(0,1)+" ")){
                //replace first in case actual number is part of season
                String temp = filename.replaceFirst(numbersOnly.substring(0,1)+" ","").trim();
                return parseSeasonNumber(temp,maxEpisode);
            }
        }
        //could not be determined so use default.
        return defaultSeasonNumber;
    }

    /**
     * Helper method to parse the year from a given filename.
     * @param filename to have year parsed from.
     * @return year parsed from the filename.
     */
    private static String parseYear(String filename){
        filename = Utilities.parseFilenameFromPath(filename);
        String numbersOnly = filename.replaceAll("[^0-9]+","");
        numbersOnly = numbersOnly.trim();
        /*All numbers in filename equal to 4, we have potentially found a year.*/
        if(numbersOnly.length() == 4){
            if(filename.contains(numbersOnly)){
                if(validateYear(numbersOnly)) {
                    return numbersOnly;
                }
            }
        }
        /*If the length of numbers found is more than four, then a number may be buried within
        * the filename that does not belong. Chop off the first and recheck, chop off the last and recheck.*/
        if(numbersOnly.length() > 4){
            //continue to chop off front to determine year.
            String tempFileName = filename.replaceFirst(numbersOnly.substring(0,1),"");
            String tempYearFront = parseYear(tempFileName);
            //continue to chop off back to determine year.
            String reverseString = new StringBuilder(filename).reverse().toString();
            tempFileName = reverseString.replaceFirst(numbersOnly.substring(numbersOnly.length()-1),"");
            tempFileName = new StringBuilder(tempFileName).reverse().toString();
            String tempYearBack = parseYear(tempFileName);
            if(tempYearFront == null && tempYearBack != null){
                return tempYearBack;
            }
            if(tempYearFront != null && tempYearBack == null){
                return tempYearFront;
            }
        }
        return null;
    }

    /**
     * Helper method to validate that the given year
     * value is after 1950 and value is this year or earlier.
     * @param year to validate.
     * @return true if 1950 < year <= currentYear; false otherwise
     */
    private static boolean validateYear(String year){
        int yearVal = Integer.parseInt(year);
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        return yearVal > 1950 && yearVal <= currentYear;
    }

    /**
     * Helper method to exchange a given filename with the
     * user specified filename.
     * @param mediaFile to exchange filenames with.
     */
    private void exchangeFileName(MediaFile mediaFile){
        for(String originalName: specialRenameCases.keySet()){
            String filename = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
            String path = "";
            String tempPath = Utilities.removeFilenameFromPath(mediaFile.getMediaName());
            if(tempPath.length()>0){
                path = tempPath;
            }
            if(originalName.equalsIgnoreCase(filename)){
                mediaFile.setMediaName(path+specialRenameCases.get(originalName));
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
            String filename = Utilities.parseFilenameFromPath(mediaFile.getMediaName());
            if(originalName.equals(filename)){
                String episodeNumber = mediaFile.getEpisodeNumber();
                if(episodeNumber == null){
                    return; //cant continue if episode number not defined.
                }
                //get int representation of episode number
                int epNum = Integer.parseInt(episodeNumber);
                String seasonNumber = mediaFile.getSeasonNumber();
                //should never be null, reason why unit tests do not hit this return
                if(seasonNumber == null){
                    return; //cant continue if season number not defined.
                }
                int sNum = Integer.parseInt(seasonNumber);
                String userSpecialCase = specialEpisodeCases.get(originalName);
                String seasonNum = userSpecialCase.replaceAll("E\\d{2}","");
                String episodeNum = userSpecialCase.replaceAll("S\\d{2}","");
                seasonNum = seasonNum.replaceAll("S","");
                episodeNum = episodeNum.replaceAll("E","");
                /*Account for when S## is given for a specific title in the config settings.*/
                if(episodeNum.equals("") && !seasonNum.equals("")){
                    mediaFile.setSeasonNumber(seasonNum);
                    return;
                }
                int userEp = Integer.parseInt(episodeNum);
                int userS = Integer.parseInt(seasonNum);
                /*If we have determined that the parsed season number is equal to
                * one + the user season number,
                * and
                * the parsed episode number is greater than the user defined episode
                * decrement the current defined parsed ep by the user ep*/
                if(epNum > userEp){
                    mediaFile.setEpisodeNumber(Integer.toString(epNum - userEp));
                }
                if(sNum == userS){
                    mediaFile.setSeasonNumber(Integer.toString(userS+1));
                }
            }
        }
    }
}
