package com.kevinmenhinick.generativemusic;

import java.util.Iterator;
import java.util.LinkedList;

public class Chord {
    
    private LinkedList<Note> notes;
    
    public Chord(Note... notes) {
        this.notes = new LinkedList<Note>();
        for(Note note: notes) {
            this.notes.add(note);
        }
        if(notes.length == 0) {
            System.out.println("Warning: Chord was passed 0 notes in constructor");
            this.notes.add(new Note(64, 50, 500));
        }
    }
    
    public Iterator<Note> getIterator() {
        return this.notes.iterator();
    }
    
    public Note bassNote() {
        return this.notes.getFirst();
    }
    
}
