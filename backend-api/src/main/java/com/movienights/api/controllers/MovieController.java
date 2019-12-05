package com.movienights.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movienights.api.entities.Movie;
import com.movienights.api.repos.MovieRepo;
import com.movienights.api.services.OmdbWebServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping()
    ResponseEntity<Movie> getByTitle(@RequestParam String title) {
        Optional<Movie> movie = movieRepo.findByTitleIgnoreCase(title);
        if (!movie.isPresent()) {
            title = title.replaceAll(" ", "+");
            omdbWebServiceClient.getFromOmdb(title);
            movie = movieRepo.findByTitleIgnoreCase(title);
        }
        if (movie.isPresent()){
            return ResponseEntity.ok(movie.get());
        }
        return ResponseEntity.notFound().build();
    }

/*
    @GetMapping("/search")
    ResponseEntity<List<Movie>> getMovie(@RequestParam String title) {
        String a = omdbWebServiceClient.getSearch(title);
        System.out.println(a);
        return ResponseEntity.notFound().build();
    }
*/
}
