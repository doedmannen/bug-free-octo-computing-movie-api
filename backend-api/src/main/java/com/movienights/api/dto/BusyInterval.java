package com.movienights.api.dto;

public class BusyInterval {
     private long start;
     private long end;
     public BusyInterval(){}

    public BusyInterval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    // We'll assume that each event needs a timespan of 30 minutes before and after each event
    public void expandBusy(){
         setEnd(end + 1800000);
         setStart(start - 1800000);
    }

    public boolean intersectsWithTimeSpan(long _start, long _end){
         return _start < end && _start > start || _end > start && _end < end;
    }
}
