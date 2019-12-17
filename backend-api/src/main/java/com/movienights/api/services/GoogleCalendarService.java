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
import java.io.IOException;
import java.time.*;
import java.util.*;

@Service
public class GoogleCalendarService {

    @Autowired
    DbUserRepo dbUserRepo;

    @Autowired
    MovieService movieService;

    @Autowired
    DbUserService dbUserService;



    public List<EventSuggestion> getEventSuggestions(List<String> usernames, String movieTitle) {
        // Renew all access tokens
        usernames.forEach(s -> dbUserService.refreshAccessToken(s));

        Set<DbUser> users = getUsersFromDb(usernames);
        Set<Calendar> calendars = getUsersCalendars(users);
        Set<FreeBusyResponse> freeBusyResponses = getFreeBusyResponses(calendars);
        Optional<Movie> movie = movieService.findMovieByTitle(movieTitle);
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

    public void insertEventIntoCalendars(List<String> usernames, String movieTitle, long startTime){
        // Renew all access tokens
        usernames.forEach(s -> dbUserService.refreshAccessToken(s));

        Set<DbUser> users = getUsersFromDb(usernames);
        Set<Calendar> calendars = getUsersCalendars(users);
        Optional<Movie> movie = movieService.findMovieByTitle(movieTitle);
        if(movie.isEmpty())
            throw new CustomException("Not a valid movie", HttpStatus.NOT_FOUND);

        calendars.forEach(calendar -> createEvent(calendar, movie.get(), startTime));
    }

    private void createEvent(Calendar calendar, Movie movie, long startTime){
        Event event = new Event();
        DateTime startDateTime = new DateTime(startTime);
        DateTime endDateTime = new DateTime(startTime + Long.parseLong(movie.getRuntime().replaceAll("[^0-9]", "")) * 60000L);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setSummary("Movie night: " + movie.getTitle());
        event.setDescription("Movie and chill...");
        event.setStart(start);
        event.setEnd(end);
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
        try {
            calendar.events().insert("primary", event).execute();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("Failed to write into calendar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<DbUser> getUsersFromDb(List<String> usernames) {
        Set<DbUser> users = new HashSet<>();
        usernames.forEach(s -> {
            DbUser user = dbUserRepo.findDistinctFirstByUsernameIgnoreCase(s);
            if(user != null) {
                if (user.getAccessToken() != null) {
                    users.add(user);
                }
            } else {
                throw new CustomException("Failed to fetch user " + s, HttpStatus.NOT_FOUND);
            }
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
        DateTime ending = new DateTime(java.util.Calendar.getInstance().getTimeInMillis() + 2629743000L);
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
        ZonedDateTime timeSuggestor = ZonedDateTime.of(current.getYear(), current.getMonthValue(), current.getDayOfMonth(), 18, 0, 0, 0, ZoneId.systemDefault());

        for(int i = 0; i < 30; i++) {
            long start = timeSuggestor.plusDays(i + 1).toEpochSecond() * 1000L;
            long end = start + movieLengthMillis;
            if (eventCollector.isEventSuggestionAvailable(start, end)) {
                suggestions.add(new EventSuggestion(start, end));
            }
            if(eventCollector.isEventSuggestionAvailable(start + 3600000L, end + 3600000L)){
                suggestions.add(new EventSuggestion(start + 3600000L, end + 3600000L));
            }
            if(eventCollector.isEventSuggestionAvailable(start + 7200000L, end + 7200000L)){
                suggestions.add(new EventSuggestion(start + 7200000L, end + 7200000L));
            }
        }
        return suggestions;
    }

}
