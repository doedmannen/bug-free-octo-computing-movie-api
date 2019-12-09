package com.movienights.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DbUserService {

    @Autowired
    DbUserRepo userRepo;

    @Autowired
    MyUserDetailService userService;

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

    public DbUser updateUser(DbUser user) {
        return userRepo.save(user);
    }

    public boolean checkIfUsernameExist(String username) {
        DbUser user = userRepo.findDistinctFirstByUsernameIgnoreCase(username);
        return user != null;
    }

    public ResponseEntity<DbUser> validateNewUser(DbUser user) throws JsonProcessingException {
        HashMap<String, String> map = new HashMap<>();
        if (user.getUsername().equals("") || user.getUsername().length() < 5 || user.getUsername().length() > 30) {
            map.put("username", "Username has to be between 5-30 characters");
        }
        if (user.getPassword().equals("") || user.getPassword().length() < 5 || user.getPassword().length() > 30)
            map.put("password", "Password has to be between 5-30 characters");
        if (map.isEmpty()) {
            return registerUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, new ObjectMapper().writeValueAsString(map));

        }
    }

    ResponseEntity<DbUser> registerUser(DbUser user) {
        DbUser newUser = new DbUser(user.getUsername(), userService.getEncoder().encode(user.getPassword()), user.getAccessToken(), user.getRefreshToken(), user.getExpiresAt());
        try {
            userRepo.save(newUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

}
