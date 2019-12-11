package com.movienights.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movienights.api.dto.MovieSearchList;
import com.movienights.api.entities.Movie;
import com.movienights.api.entities.SearchResult;
import com.movienights.api.repos.MovieRepo;
import com.movienights.api.repos.SearchResultRepo;
import com.movienights.api.services.OmdbWebServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/movie")
public class MovieController {

    @Autowired
    private MovieRepo movieRepo;
    @Autowired
    private OmdbWebServiceClient omdbWebServiceClient;
    @Autowired
    private SearchResultRepo searchResultRepo;
    @GetMapping()
    ResponseEntity<Movie> getByTitle(@RequestParam("t") String title) {
        Optional<Movie> movie = movieRepo.findByTitleIgnoreCase(title);
        if (!movie.isPresent()) {
            title = title.replaceAll(" ", "+");
            movie = omdbWebServiceClient.getFromOmdb(title);
        }
        if (movie.isPresent()){
            return ResponseEntity.ok(movie.get());
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("search")
    ResponseEntity<SearchResult> getMovie(@RequestParam("s") String searchForTitle, @RequestParam("p") int page) {
        searchForTitle = searchForTitle.replaceAll(" ", "+");

        omdbWebServiceClient.getSearch(searchForTitle, "" + page);
        SearchResult searchResult = searchResultRepo.findDistinctFirstByPath(searchForTitle+"&page="+page);
        if(searchResult != null){
            return new ResponseEntity<>(searchResult,HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
