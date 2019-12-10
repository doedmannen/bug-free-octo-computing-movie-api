package com.movienights.api.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/calendar")
public class GoogleCalendarController {

    @Autowired
    DbUserRepo userRepo;

    @GetMapping("{username}")
    public FreeBusyResponse getCalendar(@PathVariable String username) {

        DbUser user = userRepo.findDistinctFirstByUsernameIgnoreCase(username);
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime then = new DateTime(System.currentTimeMillis() + 2629743000L);

        GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
        Calendar calendar =
                new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("Movie Nights")
                        .build();

        try {
            FreeBusyRequest request = new FreeBusyRequest();
            request.setTimeMin(now);
            request.setTimeMax(then);
            request.setItems(List.of(new FreeBusyRequestItem().setId("primary")));
            FreeBusyResponse response = calendar.freebusy().query(request).execute();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setTimeMax(then)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
