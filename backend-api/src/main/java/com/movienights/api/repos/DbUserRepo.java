package com.movienights.api.repos;

import com.movienights.api.entities.DbUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DbUserRepo extends MongoRepository <DbUser, ObjectId> {

    DbUser findDistinctFirstByUsernameIgnoreCase(String username);
    DbUser findDbUserById(ObjectId id);
    List<DbUser> findDbUsersByUsernameIsLikeIgnoreCase(String username);

}