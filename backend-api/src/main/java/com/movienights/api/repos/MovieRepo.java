package com.movienights.api.repos;

import com.movienights.api.entities.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MovieRepo extends MongoRepository<Movie, ObjectId> {
    Optional<Movie> findByTitleIgnoreCase(String title);
}
