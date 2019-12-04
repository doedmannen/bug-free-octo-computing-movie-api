package com.movienights.api.controllers;


import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.services.DbUserService;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("{id}")
    ResponseEntity<DbUser> getOneUser(@PathVariable String id) {
        DbUser user = dbUserService.getOneUser(id);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with that id was found");
        }
    }

}
