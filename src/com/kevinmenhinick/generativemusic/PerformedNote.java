package com.kevinmenhinick.generativemusic;

public class PerformedNote {
    
    private Note note;
    private int time;
    
    public PerformedNote(Note note, int time) {
        this.note = note;
        this.time = time;
    }

    public Note getNote() {
        return note;
    }

    public int getTime() {
        return time;
    }
}
