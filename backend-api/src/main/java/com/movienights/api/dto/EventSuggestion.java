package com.movienights.api.dto;

import java.time.LocalDateTime;

public class EventSuggestion {
    private long start;
    private long stop;

    public EventSuggestion() { }

    public EventSuggestion(long _start, long _end){
        start = _start;
        stop = _end;
    }


    public void setStart(long start) {
        this.start = start;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public long getStart() {
        return start;
    }

    public long getStop() {
        return stop;
    }
}
