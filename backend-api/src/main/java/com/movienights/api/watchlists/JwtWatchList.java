package com.movienights.api.watchlists;

import org.springframework.ui.ConcurrentModel;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JwtWatchList {

    public static JwtWatchList getInstance() {
        if(instance == null){
            instance = new JwtWatchList();
        }
        return instance;
    }
    private static JwtWatchList instance;

    private ConcurrentHashMap<String, Long> namesToInspect;

    private JwtWatchList() {
        namesToInspect = new ConcurrentHashMap<>();
        Calendar.getInstance();
        new Thread(this::run).start();
    }


    public boolean isOnWatchList(String username){
        return namesToInspect.containsKey(username);
    }

    public void watchFor(String username) {
        namesToInspect.put(username, (Calendar.getInstance().getTimeInMillis() + 3600000));
        System.out.println("Watchlist increased by one " + namesToInspect.size());
    }

    private void run() {
        while(true){
            try{
                Thread.sleep(60000);
            } catch (Exception ignored){}

            System.out.println("Currently there are " + namesToInspect.size() + " names on the system watchlist...");

            namesToInspect.entrySet()
                    .stream().filter(entry -> entry.getValue() < Calendar.getInstance().getTimeInMillis())
                    .map(Map.Entry::getKey)
                    .forEach(s -> namesToInspect.remove(s));
        }

    }

}
