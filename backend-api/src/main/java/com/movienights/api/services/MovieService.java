package com.movienights.api.services;

import com.movienights.api.entities.Movie;
import com.movienights.api.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

   @Autowired
   MovieRepo movieRepo;
   @Autowired
   OmdbWebServiceClient omdbWebServiceClient;

   public Optional<Movie> findMovieByTitle(String title){
      Optional<Movie> movie = movieRepo.findByTitleIgnoreCase(title);
      if (!movie.isPresent()) {
         title = title.replaceAll(" ", "+");
         movie = omdbWebServiceClient.getFromOmdb(title);
      }
      return movie;
   }

}
