package com.movienights.api.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.movienights.api.dto.EventSuggestion;
import com.movienights.api.entities.DbUser;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoogleCalendarService {

    @Autowired
    DbUserRepo dbUserRepo;

    @Autowired
    MovieRepo movieRepo;


    public Set<EventSuggestion> getEventSuggestions(List<String> usernames, String movieId) {
        Set<DbUser> users = getUsersFromDb(usernames);
        Set<EventSuggestion> eventSuggestions = new HashSet<EventSuggestion>();
        //Set<>
        if(!users.isEmpty()){
            DateTime starting = new DateTime(java.util.Calendar.getInstance().getTimeInMillis());
            DateTime ending = new DateTime(java.util.Calendar.getInstance().getTimeInMillis() + 31556926000L);

        }

        return eventSuggestions;
    }

    private Set<DbUser> getUsersFromDb(List<String> usernames) {
        Set<DbUser> users = new HashSet<>();
        usernames.forEach(s -> {
            DbUser user = dbUserRepo.findDistinctFirstByUsernameIgnoreCase(s);
            users.add(user);
        });
        return users;
    }

    @SuppressWarnings("deprecation")
    private Set<Calendar> getUsersCalendars(Set<DbUser> users){
        Set<Calendar> calendars = new HashSet<>();
        users.forEach(user -> {
            GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
            calendars.add(new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Movie Nights")
                    .build());
        });
        return calendars;
    }




//        try {
//        FreeBusyRequest request = new FreeBusyRequest();
//        request.setTimeMin(now);
//        request.setTimeMax(then);
//        request.setItems(List.of(new FreeBusyRequestItem().setId("primary")));
//        FreeBusyResponse response = calendar.freebusy().query(request).execute();
//
//        return response;
//    } catch (
//    IOException e) {
//        e.printStackTrace();
//    }
}
