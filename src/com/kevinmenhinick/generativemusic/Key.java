package com.kevinmenhinick.generativemusic;

import java.util.ArrayList;

public abstract class Key {
    
    private int rootNote;
    private ArrayList<Integer> notes;
    
    public String of() {
        String notes[] = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        String note = notes[getRoot()];
        return note;
    }
    
    public int getRoot() {
        return this.rootNote % 12;
    }
}
