package com.movienights.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.services.DbUserService;
import com.movienights.api.services.GoogleAuthService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class DbUserController {

    @Autowired
    DbUserService dbUserService;

    @GetMapping
    ResponseEntity<List<DbUser>> getAllUsers() {
        List users = dbUserService.getAllUsers();

        if (users != null) {
            if (users.size() != 0) {
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("refresh-access-token")
    public void refreshaccesstoken(){
                dbUserService.refreshAccessToken("Pelle");
    }

    @GetMapping("{id}")
    ResponseEntity<DbUser> getOneUser(@PathVariable String id) {
        DbUser user = dbUserService.getOneUser(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id was found");
        }
    }

    @PutMapping("{id}")
    ResponseEntity<DbUser> updateUser(@PathVariable String id, @RequestBody DbUser body) {
        DbUser user = dbUserService.getOneUser(id);

        if (user != null) {
            if (body.getFavoriteGenre() != null) {
                user.setFavoriteGenre(body.getFavoriteGenre());
            }
            return new ResponseEntity<>(dbUserService.updateUser(user), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find a user with that id");
        }
    }

    @PostMapping("check-available")
    public ResponseEntity<Boolean> checkUsername(@RequestBody DbUser body){
        boolean user = dbUserService.checkIfUsernameExist(body.getUsername());
        if(user){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }  else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<DbUser> registerUser(@RequestBody DbUser body) throws JsonProcessingException {
        boolean user = dbUserService.checkIfUsernameExist(body.getUsername());
        if (user) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        } else {
            return dbUserService.validateNewUser(body);
        }
    }

}
