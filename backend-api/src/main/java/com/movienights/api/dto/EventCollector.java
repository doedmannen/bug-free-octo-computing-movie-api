package com.movienights.api.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventCollector {
    private List<BusyInterval> busyIntervals;

    public EventCollector(){
        busyIntervals = new ArrayList<>();
    }

    public void addBusyInterval(BusyInterval interval){
        interval.expandBusy();
        busyIntervals.add(interval);
        sortBusyIntervals();
        mergeBusyIntervals();
    }

    private void sortBusyIntervals(){
        busyIntervals.sort(Comparator.comparing(BusyInterval::getStart));
    }

    private void mergeBusyIntervals(){
        sortBusyIntervals();
        List<BusyInterval> mergedList = new ArrayList<>();
        // No need to merge
        if(busyIntervals.size() <= 1)
            return;

        BusyInterval previous = busyIntervals.get(0);

        for(int i = 1; i < busyIntervals.size(); i++){
            BusyInterval current = busyIntervals.get(i);
            if(current.getStart() <= previous.getEnd()){
                previous.setEnd(Math.max(current.getEnd(), previous.getEnd()));
            } else {
                mergedList.add(new BusyInterval(previous.getStart(), previous.getEnd()));
                previous = current;
            }
            mergedList.add(new BusyInterval(previous.getStart(), previous.getEnd()));
        }
        busyIntervals = mergedList;
    }

    public boolean isEventSuggestionAvailable(long start, long end){
        return busyIntervals.stream().noneMatch(interval -> interval.intersectsWithTimeSpan(start, end));
    }
}
