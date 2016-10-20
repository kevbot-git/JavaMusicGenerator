package com.kevinmenhinick.generativemusic;

public class Beat {
    
    private Integer tempo;
    private int beat;
    private int of;
    
    public Beat(Integer tempo, int beatsPerBar) {
        this.tempo = tempo;
        beat = 1;
        of = beatsPerBar;
    }
    
    public int length(int numerator, int denominator) {
        return (60000 / this.tempo() / denominator * numerator);
    }
    
    public int tempo() {
        return tempo;
    }
    
    public void setTempo(Integer tempo) {
        this.tempo = tempo;
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
