package com.movienights.api.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.movienights.api.keys.ApiKeys;
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

    @PostMapping("/storeauthcode")
    ResponseEntity<GoogleTokenResponse> storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
//             Without the `X-Requested-With` header, this request could be forged. Aborts.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error, wrong headers");
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    "http://localhost:8080") // Make sure you set the correct port
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store these 3 in your DB
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);
        tokenResponse.setExpiresInSeconds(expiresAt);

        // Debug purpose only
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
