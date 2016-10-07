package rename;

import constants.Constants;

/**
 * Representation of a media file.
 */
public class MediaFile {
    /**Original filename of this media file.*/
    private final String originalFileName;
    /**Parsed media name of this media file.*/
    private String mediaName = null;
    /**Parsed episode number of this media file.*/
    private String episodeNumber = null;
    /**Parsed season number of this media file.*/
    private String seasonNumber = null;
    /**Parsed fileExt of this media file.*/
    private String fileExt = null;
    /**Parsed year for this media file. Only used in movie cases.*/
    private String year = null;

    /**
     * Create a new MediaFile object with the original
     * filename.
     * @param originalFileName to be assigned to this media file.
     */
    public MediaFile(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    /**
     * Assign new value to this media file's media name.
     * @param mediaName to be assigned to this media file.
     */
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    /**
     * Assign new value to this media file's episode number.
     * @param episodeNumber to be assigned to this media file.
     */
    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    /**
     * Assign new value to this media file's season number.
     * @param seasonNumber to be assigned to this media file.
     */
    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    /**
     * Assign new value to this media file's fileExt.
     * @param fileExt to be assigned to this media file.
     */
    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    /**
     * Assign new value to this media file's year.
     * @param year to be assigned to this media file.
     */
    void setYear(String year) {
        this.year = year;
    }

    /**
     * Retrieve this media file's original filename.
     * @return the original file name for this media file.
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * Retrieve this media file's media name.
     * @return the parsed mediaName for this media file.
     */
    public String getMediaName() {
        return mediaName;
    }

    /**
     * Retrieve this media file's episode number.
     * If null, return null.
     * If not null, format for either 3 digit or 2 digit episode number.
     * @return formatted episode number for this media file.
     */
    String getEpisodeNumber() {
        if(episodeNumber == null){
            return null;
        }
        switch(episodeNumber.length()){
            case 3:
                return String.format("%3d", Integer.parseInt(episodeNumber)).replace(" ","0");
            default:
                return String.format("%2d", Integer.parseInt(episodeNumber)).replace(" ","0");
        }
    }

    /**
     * Retrieve this media file's season number.
     * If null, return null.
     * If not null, format for 2 digit season number.
     * @return formatted season number for this media file.
     */
    String getSeasonNumber() {
        if(seasonNumber == null){
            return null;
        }
        return String.format("%2d", Integer.parseInt(seasonNumber)).replace(" ","0");
    }

    /**
     * Retrieve this media file's file ext.
     * @return the parsed fileExt of this media file.
     */
    String getFileExt() {
        return fileExt;
    }

    /**
     * Retrieve this media file's year.
     * @return the parsed year of this media file.
     */
    private String getYear() {
        return year;
    }

    /**
     * If all valid fields are not null, build and return formatted output.
     * If any of valid fields are null, return null.
     * @return null or formatted output.
     */
    @Override
    public String toString() {
        if(getMediaName() != null && getSeasonNumber() != null && getEpisodeNumber() != null && getFileExt() != null && getYear() == null){
            String preBuiltMedia = Constants.DEFAULT_MEDIA_NAME;
            preBuiltMedia = preBuiltMedia.replace(Constants.MEDIA_NAME, getMediaName());
            preBuiltMedia = preBuiltMedia.replace(Constants.XX, getSeasonNumber());
            preBuiltMedia = preBuiltMedia.replace(Constants.YYY, getEpisodeNumber());
            preBuiltMedia = preBuiltMedia.replace(Constants.FILE_EXT, getFileExt());
            return preBuiltMedia;
        }
        if(getMediaName() != null && getFileExt() != null && getYear() != null){
            String preBuiltMedia = Constants.DEFAULT_MEDIA_NAME;
            preBuiltMedia = preBuiltMedia.replace(Constants.MEDIA_NAME, getMediaName());
            preBuiltMedia = preBuiltMedia.replace("S"+Constants.XX+"E"+Constants.YYY, getYear());
            preBuiltMedia = preBuiltMedia.replace(Constants.FILE_EXT, getFileExt());
            return preBuiltMedia;
        }
        return null;
    }
}
