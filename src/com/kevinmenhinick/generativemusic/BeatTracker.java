package com.kevinmenhinick.generativemusic;

public class BeatTracker {
    
    private int tempo;
    private int beat;
    private int of;
    
    public BeatTracker(int tempo, int beatsPerBar) {
        this.tempo = tempo;
        beat = 1;
        of = beatsPerBar;
    }
    
    public int tempo() {
        return tempo;
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
