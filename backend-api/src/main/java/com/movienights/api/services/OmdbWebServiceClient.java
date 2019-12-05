package com.movienights.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movienights.api.entities.Movie;
import com.movienights.api.keys.ApiKeys;
import com.movienights.api.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class OmdbWebServiceClient {

    private final String SEARCH_URL = "https://www.omdbapi.com/?t=TITLE&apikey=APIKYE";
    private final String SEARCH =  "http://www.omdbapi.com/?apikey=APIKYE&s=TITLE";

    @Autowired
    MovieRepo movieRepo;

    public void getFromOmdb(String title){
        String jsonResponse = searchMovieByTitle(title, ApiKeys.ombiKey);
        saveToDb(jsonResponse);
    }

    private String sendGetRequest(String requestUrl){
        StringBuffer response = new StringBuffer();

        try{
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader  buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                response.append(line);
            }
            buffer.close();
            connection.disconnect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response.toString();
    }

    private String searchMovieByTitle(String title, String key){
        String requestUrl = SEARCH_URL.replaceAll("TITLE", title).replaceAll("APIKYE",key);
        return sendGetRequest(requestUrl);
    }

    private void saveToDb(String jsonResponse) {
        try{
            ObjectMapper orm = new ObjectMapper();
            Movie m = orm.readValue(jsonResponse, Movie.class);
            movieRepo.save(m);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSearch(String title){
        String jsonResponse = sertchMovies(title, ApiKeys.ombiKey);
        System.out.println(jsonResponse);
        return jsonResponse;
    }

    private String sertchMovies(String title, String key){
        String requestUrl = SEARCH.replaceAll("TITLE", title).replaceAll("APIKYE",key);
        return sendGetRequest(requestUrl);
    }
}
