package com.movienights.api.services;

import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserService {

    @Autowired
    DbUserRepo userRepo;

    public List<DbUser> getAllUsers() {
        return userRepo.findAll();
    }

    public DbUser getOneUser(String id) {
        try {
            ObjectId _id = new ObjectId(id);
            return userRepo.findDbUserById(_id);
        } catch (Exception e) {
            return null;
        }
    }


}
