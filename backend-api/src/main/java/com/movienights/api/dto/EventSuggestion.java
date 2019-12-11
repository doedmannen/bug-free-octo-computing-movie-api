package com.movienights.api.dto;

public class EventSuggestion {
    private long start;
    private long stop;

    public EventSuggestion() {
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
