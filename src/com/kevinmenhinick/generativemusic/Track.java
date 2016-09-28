package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Track implements Runnable {
    
    private Synth instrument;
    private Thread thread;
    private boolean playing;
    private Beat beat;
    
    public Track(Synth instrument, Beat beatTracker) {
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

    public abstract void generateOnBeat(Beat beat);
    
    @Override
    public void run() {
        //System.out.println("Started listening...");
        int total = 40;
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
                generateOnBeat(beat);
            }
        } while(playing);
        //System.out.println("Finished listening.");
    }
    
    public static int maxLengthFromBeat(Beat beatTracker) {
        return (6000 / beatTracker.tempo());
    }
    
    public Synth getInstrument() {
        return instrument;
    }
}
