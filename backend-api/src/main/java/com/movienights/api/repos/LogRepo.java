package com.movienights.api.repos;

import com.movienights.api.entities.Log;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepo extends MongoRepository<Log, ObjectId> {
}
