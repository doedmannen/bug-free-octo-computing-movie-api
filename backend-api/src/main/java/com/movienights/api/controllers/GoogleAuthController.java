package com.movienights.api.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.movienights.api.keys.ApiKeys;
import com.movienights.api.services.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static com.movienights.api.keys.ApiKeys.CLIENT_ID;
import static com.movienights.api.keys.ApiKeys.CLIENT_SECRET;

@RestController
@RequestMapping("api/googleauth")
public class GoogleAuthController {

    @Autowired
    GoogleAuthService googleAuthService;

    @PostMapping("/storeauthcode")
    ResponseEntity<GoogleTokenResponse> storeAuthCode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
//             Without the `X-Requested-With` header, this request could be forged. Aborts.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error, wrong headers");
        } else {
            GoogleTokenResponse tokenResponse = googleAuthService.getTokens(code);
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        }
    }

}
