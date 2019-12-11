package com.movienights.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movienights.api.dto.MovieSearchList;
import com.movienights.api.entities.Movie;
import com.movienights.api.entities.SearchResult;
import com.movienights.api.keys.ApiKeys;
import com.movienights.api.repos.MovieRepo;
import com.movienights.api.repos.SearchResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
public class OmdbWebServiceClient {

    private final String SEARCH_URL = "https://www.omdbapi.com/?t=TITLE&apikey=APIKYE";
    private final String SEARCH =  "http://www.omdbapi.com/?apikey=APIKYE&s=SEARCHWORD&page=PAGENR";

    @Autowired
    MovieRepo movieRepo;

    @Autowired
    SearchResultRepo searchResultRepo;

    public Optional<Movie> getFromOmdb(String title){
        String jsonResponse = searchMovieByTitle(title, ApiKeys.ombiKey);
        return saveToDb(jsonResponse);
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

    private Optional<Movie> saveToDb(String jsonResponse) {
        try{
            ObjectMapper orm = new ObjectMapper();
            Movie m = orm.readValue(jsonResponse, Movie.class);
            return Optional.of(movieRepo.save(m));
        } catch (Exception e){
        }
        return Optional.empty();
    }

    public void getSearch(String title, String page){
        if (searchResultRepo.findDistinctFirstByPath(title+"&page="+page) != null){
            return;
        }
        String jsonResponse = sertchMovies(title, ApiKeys.ombiKey, page);
        try{
            MovieSearchList list = new ObjectMapper().readValue(jsonResponse, MovieSearchList.class);
            SearchResult searchResult = new SearchResult();
            searchResult.setPath(title+"&page="+page);
            searchResult.setResult(list);
            searchResultRepo.save(searchResult);
        } catch (Exception e){
        }
    }

    private String sertchMovies(String title, String key, String page){
        String requestUrl = SEARCH.replaceAll("SEARCHWORD", title).replaceAll("APIKYE",key).replaceAll("PAGENR", page);
        return sendGetRequest(requestUrl);
    }



}
