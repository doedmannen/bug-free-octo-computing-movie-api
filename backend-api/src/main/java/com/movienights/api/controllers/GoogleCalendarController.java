package com.movienights.api.controllers;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.movienights.api.dto.EventSuggestion;
import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.services.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/calendar")
public class GoogleCalendarController {

    @Autowired
    GoogleCalendarService googleCalendarService;

    @GetMapping()
    public ResponseEntity<Set<EventSuggestion>> getCalendar(@RequestParam("user") List<String> usernames, @RequestParam("movieId") String movieId) {
        return new ResponseEntity<>(googleCalendarService.getEventSuggestions(usernames, movieId), HttpStatus.OK);
    }
}
