package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Track implements Runnable {
    
    private Synth instrument;
    private Thread thread;
    private boolean playing;
    private BeatTracker beat;
    
    public Track(Synth instrument, BeatTracker beatTracker) {
        this.instrument = instrument;
        this.playing = false;
        try {
            instrument.open();
        } catch(GeneratorException e) {
            System.out.println(e);
        }
        beat = beatTracker;
    }
    
    public void start() {
        if(!playing) {
            thread = new Thread(this);
            playing = true;
            thread.start();
        }
    }
    
    public void stop() {
        playing = false;
    }

    @Override
    public void run() {
        //System.out.println("Started listening...");
        boolean firstCountOfOne = true;
        do {
            try {
                //System.out.println("Waiting...");
                synchronized(beat) {
                    beat.wait();
                }
            } catch(InterruptedException e) { }
            
            if(firstCountOfOne)
                if(beat.beat() == 1)
                    firstCountOfOne = false;
            
            if(!firstCountOfOne) {
                System.out.println("Beat: " + beat.beat());
                if(beat.beat() % 2 == 0) { // 2 & 4
                    System.out.println("Was 2 or 4");
                    getInstrument().playNote(new Note(40, 127, maxLengthFromBeat(beat)));
                }
                getInstrument().playNote(new Note(35, 127, maxLengthFromBeat(beat)));
            }
        } while(playing);
        //System.out.println("Finished listening.");
    }
    
    public static int maxLengthFromBeat(BeatTracker beatTracker) {
        return (6000 / beatTracker.tempo() - 1);
    }
    
    public Synth getInstrument() {
        return instrument;
    }
}
