package com.movienights.api.repos;

import com.movienights.api.entities.SearchResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchResultRepo extends MongoRepository<SearchResult, ObjectId> {
    SearchResult findDistinctFirstByPath(String path);
}
