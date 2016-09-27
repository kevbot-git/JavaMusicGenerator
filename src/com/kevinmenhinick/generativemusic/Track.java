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
        do {
            try {
                System.out.println("Wait...");
                synchronized(beat) {
                    beat.wait();
                }
            } catch(InterruptedException e) { }
            System.out.println("Beat: ");// + beat.beat());
        } while(playing);
    }
    
    public Synth getInstrument() {
        return instrument;
    }
}
