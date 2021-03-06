package com.movienights.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.entities.DbUser;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DbUserService {

    @Autowired
    DbUserRepo userRepo;

    @Autowired
    MyUserDetailService userDetailService;

    @Autowired
    GoogleAuthService googleAuthService;

    public List<DbUser> getAllUsers() {
        return userRepo.findAll();
    }

    public List<DbUser> getSearchedUsers(String searchQuery, String username) {
        return userRepo.findDbUsersByUsernameIsLikeIgnoreCase(searchQuery).stream().filter(dbUser -> dbUser.getAccessToken() != null)
                .filter(dbUser -> !dbUser.getUsername().equalsIgnoreCase(username)).collect(Collectors.toList());
    }

    public DbUser getOneUser(String id) {
        try {
            ObjectId _id = new ObjectId(id);
            return userRepo.findDbUserById(_id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @noinspection deprecation
     */
    public void refreshAccessToken(String username) {
        DbUser user = userRepo.findDistinctFirstByUsernameIgnoreCase(username);
        if(user == null)
            throw new CustomException("Unknown user " + username, HttpStatus.INTERNAL_SERVER_ERROR);
        if(user.getRefreshToken() == null)
            return;
        if(Calendar.getInstance().getTimeInMillis() + 60000L < user.getExpiresAt())
            return;

        GoogleTokenResponse credential = googleAuthService.getRefreshedCredentials(user.getRefreshToken());
        Long expiresAt = Calendar.getInstance().getTimeInMillis() + (credential.getExpiresInSeconds() * 1000);
        user.setAccessToken(credential.getAccessToken());
        user.setExpiresAt(expiresAt);
        userRepo.save(user);
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
        DbUser newUser = new DbUser(user.getUsername(), userDetailService.getEncoder().encode(user.getPassword()), user.getAccessToken(), user.getRefreshToken(), user.getExpiresAt());
        try {
            userRepo.save(newUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    void revokeUserJwt(String username) {
        DbUser user = userRepo.findDistinctFirstByUsernameIgnoreCase(username);
        user.setJwtSalt(UUID.randomUUID());
        userRepo.save(user);
    }

}
