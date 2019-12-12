package com.movienights.api.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.movienights.api.dto.BusyInterval;
import com.movienights.api.dto.EventCollector;
import com.movienights.api.dto.EventSuggestion;
import com.movienights.api.entities.DbUser;
import com.movienights.api.entities.Movie;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.repos.MovieRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.*;
import java.util.*;

@Service
public class GoogleCalendarService {

    @Autowired
    DbUserRepo dbUserRepo;

    @Autowired
    MovieRepo movieRepo;

    @Autowired
    DbUserService dbUserService;



    public List<EventSuggestion> getEventSuggestions(List<String> usernames, String movieId) {
        // Renew all access tokens
        usernames.forEach(s -> dbUserService.refreshAccessToken(s));

        Set<DbUser> users = getUsersFromDb(usernames);
        Set<Calendar> calendars = getUsersCalendars(users);
        Set<FreeBusyResponse> freeBusyResponses = getFreeBusyResponses(calendars);
        Optional<Movie> movie = movieRepo.findById(new ObjectId(movieId));
        EventCollector eventCollector = getEventCollector(freeBusyResponses);

        if(movie.isEmpty()){
            throw new CustomException("Invalid movie", HttpStatus.NOT_FOUND);
        }
        List<EventSuggestion> eventSuggestions = produceEventSuggestions(eventCollector, movie.get());
        if(eventSuggestions.isEmpty()){
            throw new CustomException("Nobody has time", HttpStatus.NOT_FOUND);
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
    private Set<Calendar> getUsersCalendars(Set<DbUser> users) {
        Set<Calendar> calendars = new HashSet<>();
        users.forEach(user -> {
            GoogleCredential credential = new GoogleCredential().setAccessToken(user.getAccessToken());
            calendars.add(new Calendar.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Movie Nights")
                    .build());
        });
        return calendars;
    }

    private Set<FreeBusyResponse> getFreeBusyResponses(Set<Calendar> calendars){
        Set<FreeBusyResponse> responses = new HashSet<>();
        DateTime starting = new DateTime(java.util.Calendar.getInstance().getTimeInMillis());
        DateTime ending = new DateTime(java.util.Calendar.getInstance().getTimeInMillis() + 31556926000L);
        try {
            for(Calendar calendar : calendars){
                FreeBusyRequest request = new FreeBusyRequest();
                request.setTimeMin(starting);
                request.setTimeMax(ending);
                request.setItems(List.of(new FreeBusyRequestItem().setId("primary")));
                responses.add(calendar.freebusy().query(request).execute());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Set.of();
        }
        return responses;
    }

    private EventCollector getEventCollector(Set<FreeBusyResponse> freeBusyResponses){
        EventCollector eventCollector = new EventCollector();

        freeBusyResponses.forEach(freeBusyResponse -> {
            freeBusyResponse.getCalendars().values()
                    .forEach(freeBusyCalendar -> freeBusyCalendar.getBusy()
                            .forEach(busy -> eventCollector.addBusyInterval(new BusyInterval(busy.getStart().getValue(), busy.getEnd().getValue()))));
        });
        return eventCollector;
    }

    private List<EventSuggestion> produceEventSuggestions(EventCollector eventCollector, Movie movie){
        long movieLengthMillis;
        List<EventSuggestion> suggestions = new ArrayList<>();
        try{
            movieLengthMillis = Long.parseLong(movie.getRuntime().replaceAll("[^0-9]","")) * 60000L;
        } catch(Exception e){
            throw new CustomException("The specific movie has no runtime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LocalDateTime current = LocalDateTime.now();
        ZonedDateTime timeSuggestor = ZonedDateTime.of(current.getYear(), current.getMonthValue(), current.getDayOfMonth(), 19, 0, 0, 0, ZoneId.systemDefault());

        for(int i = 0; i < 100 || suggestions.size() > 20; i++) {
            timeSuggestor.plusDays(1);
            long start = timeSuggestor.toEpochSecond() * 1000L;
            long end = start + movieLengthMillis;
            if (eventCollector.isEventSuggestionAvailable(start, end)) {
                suggestions.add(new EventSuggestion(start, end));
            }
        }
        return suggestions;
    }

}
