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
            this.notes.add(new Note(64, 50));
        }
    }
    
    public Iterator<Note> getIterator() {
        return this.notes.iterator();
    }
    
    public Note bassNote() {
        return this.notes.getFirst();
    }
    
    public static Chord createMajor(Note rootNote) {
        return create(rootNote, 1, 4, 7);
    }
    
    public static Chord createMinor(Note rootNote) {
        return create(rootNote, 1, 3, 7);
    }
    
    public static Chord create(Note rootNote, Integer... intervalIndexes) {
        LinkedList<Note> notes = new LinkedList<Note>();
        
        if(intervalIndexes.length > 0) {
            for(Integer i: intervalIndexes) {
                if(i == 1) {
                    notes.add(rootNote);
                }
                else {
                    notes.add(new Note(rootNote.getNote() + i, rootNote.getVelocity()));
                }
            }
        }
        return new Chord(notes.toArray(new Note[notes.size()]));
    }
    
}
