package com.kevinmenhinick.generativemusic;

import java.util.ArrayList;

public abstract class Key {
    
    private int rootNote;
    private ArrayList<Integer> notes;
    
    public Key(int rootNote) {
        this.rootNote = rootNote % 12;
    }
    
    public abstract Key relative();
    
    public Key modulate(int change) {
        if(this instanceof MajorKey)
            return new MajorKey(this.rootNote + change);
        else
            return new MinorKey(this.rootNote + change);
    }
    
    public String of() {
        String notes[] = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
        String note = notes[getRoot()];
        return note;
    }
    
    public int getRoot() {
        return this.rootNote;
    }
}
