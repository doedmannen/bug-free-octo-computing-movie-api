package com.movienights.api.services;

import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.dto.TokenResponse;
import com.movienights.api.entities.DbUser;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.tokenproviders.JwtTokenProvider;
import com.movienights.api.watchlists.JwtWatchList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {
    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    MyUserDetailService userDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    DbUserRepo userRepo;

    @Autowired
    DbUserService userService;

    public TokenResponse login(String username, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            DbUser user = userRepo.findDistinctFirstByUsernameIgnoreCase(username);
            return new TokenResponse(tokenProvider.createToken(user.getUsername(), user.getJwtSalt().toString(), user.getRoles()));
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void invalidateAllUserTokens(HttpServletRequest request){
        if(!tokenProvider.validateToken(tokenProvider.resolveToken(request)))
            return;
        userService.revokeUserJwt(tokenProvider.getUsername(request));
        JwtWatchList.getInstance().watchFor(tokenProvider.getUsername(request));
    }

    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request){
        String token = tokenProvider.resolveToken(request);
        DbUser user = tokenProvider.getUserForTokenRenewal(token);
        if(user != null) {
            return new ResponseEntity<>(new TokenResponse(tokenProvider.createToken(user.getUsername(), user.getJwtSalt().toString(), user.getRoles())), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
