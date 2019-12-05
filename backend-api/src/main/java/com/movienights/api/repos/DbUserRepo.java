package com.movienights.api.repos;

import com.movienights.api.entities.DbUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DbUserRepo extends MongoRepository <DbUser, ObjectId> {

    DbUser findDistinctFirstByUsernameIgnoreCase(String username);
    DbUser findDbUserById(ObjectId id);

}