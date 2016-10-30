package movieDB;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import errorHandle.ErrorHandler;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is responsible for handling request to the
 * movie database.
 * This movie database existence is a singleton.
 */
public class MovieDatabase {

    private static final String MOVIE_DATABASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "";
    private static final String MOVIE_TITLE = "";
    private static MovieDatabase instance = null;

    private MovieDatabase(){

    }

    public static MovieDatabase getInstance(){
        if(instance == null){
            instance = new MovieDatabase();
        }
        return instance;
    }

    public static String getYear(String movieTitle){
        JsonNode movie = null;
        try {
            movie = getMovie(movieTitle);
        } catch (Exception e) {
            ErrorHandler.printError("Unable to get movie."+e.getMessage());
        }
        if(movie != null){
            return movie.get(MOVIE_TITLE).asText();
        }
        return null;
    }

    private static JsonNode getMovie(String movieTitle) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestMovieData(movieTitle), JsonNode.class);
    }

    private static String requestMovieData(String movieTitle) throws Exception{
        URL url = new URL(buildMovieDBQuery(""));
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        String encoding = connection.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        return IOUtils.toString(inputStream, encoding);
    }

    private static String buildMovieDBQuery(String query){
        return MOVIE_DATABASE_URL+query+"?api_key="+API_KEY;
    }

}
