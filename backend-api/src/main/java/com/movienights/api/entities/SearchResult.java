package com.movienights.api.entities;

import com.movienights.api.dto.MovieSearchList;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class SearchResult {

    @BsonId
    private ObjectId id;
    private String path;
    private MovieSearchList result;

    public SearchResult() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MovieSearchList getResult() {
        return result;
    }

    public void setResult(MovieSearchList result) {
        this.result = result;
    }
}
