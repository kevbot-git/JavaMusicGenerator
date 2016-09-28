package com.kevinmenhinick.generativemusic;

import java.util.ArrayList;
import java.util.LinkedList;

class DrumTrack extends Track {

    private int bassHit;
    private int accentHit;
    private int tickHit;
    
    public DrumTrack(Drums drums, Beat beat) {
        super(drums, beat);
        bassHit = Drums.randomBassHit();
        accentHit = Drums.randomAccentHit();
        tickHit = Drums.randomTickHit();
    }

    @Override
    public void generateOnBeat(Beat beat) {
        LinkedList<Note> notes = new LinkedList();
        
        if(beat.beat() % beat.beatsPerBar() == 1) {
            notes.add(new Note(bassHit, 127));
        }
        
        if(beat.beatsPerBar() == 4 && beat.beat() == 3) {
            notes.add(new Note(accentHit, 127));
        }
        
        notes.add(new Note(tickHit, 100));
        
        for(Note note: notes)
            getInstrument().playNote(note, beat.length(1, 1));
    }
}
