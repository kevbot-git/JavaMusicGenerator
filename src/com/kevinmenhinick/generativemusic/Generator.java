package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generator implements Runnable {
    
    private Track t1;
    private Thread thread;
    private boolean playing;
    private int beatsPerBar;
    private int beat = 1;
    
    public Generator() throws GeneratorException {
        t1 = new Track(new Drums());
        beatsPerBar = 4;
    }
    
    private void randomlyGenerate(Synth synth, int lowerBound, int upperBound) {
        Note n = new Note((new Random()).nextInt() % (upperBound - lowerBound) + lowerBound, 127, 500);
        //synth.playChord(Chord.createMajor(n), 40);
        synth.playChord(Chord.createMajor(new Note(64, 40, 500)), 40);
        nextBeat();
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
        t1.stop();
    }
    
    @Override
    public void run() {
        t1.start();
        do {
            try {
                //randomlyGenerate(36, 48);
                //randomlyGenerate(synth, 64, 72);
                Thread.sleep(10000);
            } catch (Exception ex) { }
            t1.interval = 100;
            
        } while(playing);
        System.out.println("Finished");
        t1.stop();
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public void nextBeat() {
        beat = beat % beatsPerBar + 1;
    }
    
    public int getBeat() {
        return beat;
    }
    
}