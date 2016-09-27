package com.kevinmenhinick.generativemusic;

public class BeatTracker {
    
    private int beat;
    private int of;
    
    public BeatTracker(int beatsPerBar) {
        beat = 1;
        of = beatsPerBar;
    }
    
    public int beat() {
        return beat;
    }
    
    public int beatsPerBar() {
        return of;
    }
    
    public int nextBeat() {
        beat = beat % of + 1;
        return beat;
    }
}
