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
import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/calendar")
public class GoogleCalendarController {

    @Autowired
    GoogleCalendarService googleCalendarService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public ResponseEntity<List<EventSuggestion>> getCalendar(@RequestParam("users") List<String> users, @RequestParam("movieTitle") String movieTitle, HttpServletRequest request) {
        users.add(jwtTokenProvider.getUsername(request));
        return new ResponseEntity<>(googleCalendarService.getEventSuggestions(users, movieTitle), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertEventIntoCalendars(@RequestParam("users") List<String> users, @RequestParam("movieTitle") String movieTitle, @RequestParam("start") long start, HttpServletRequest request){
        users.add(jwtTokenProvider.getUsername(request));
        googleCalendarService.insertEventIntoCalendars(users, movieTitle, start);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
