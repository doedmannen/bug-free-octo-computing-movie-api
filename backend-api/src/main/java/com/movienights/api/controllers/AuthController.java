package com.movienights.api.controllers;

import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.dto.SignInRequest;
import com.movienights.api.dto.TokenResponse;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.services.AuthService;
import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("login")
    private ResponseEntity<TokenResponse> login(@RequestBody SignInRequest signInRequest){
        return new ResponseEntity<>(authService.login(signInRequest.username, signInRequest.password), HttpStatus.OK);
    }

    @DeleteMapping
    private void invalidateAllMyTokens(HttpServletRequest request){
        authService.invalidateAllUserTokens(request);
    }

    @GetMapping("refresh")
    private ResponseEntity<TokenResponse> refresh(HttpServletRequest request){
        return authService.refresh(request);
    }

}
