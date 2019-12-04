package com.movienights.api.controllers;

import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.dto.SignInRequest;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    MyUserDetailService userDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    DbUserRepo userRepo;

    @GetMapping("closed")
    private String closedPath(){
        return "Hejsan detta är en stängd path";
    }
    @GetMapping("open")
    private String openPath(){
        return "Hejsan detta är en öppen path";
    }

    @PostMapping("login")
    private String login(@RequestBody SignInRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
            return tokenProvider.createToken(request.username, userRepo.findDistinctFirstByUsernameIgnoreCase(request.username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
