package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.ArrayList;
import java.util.Random;

public class Generator implements Runnable {
    
    private Synth synth;
    private Drums drums;
    private Thread thread;
    private boolean playing;
    
    public Generator() throws GeneratorException {
        synth = new Synth();
        //drums = new Drums();
        synth.open();
        //drums.open();
    }
    
    private void randomlyGenerate(int lowerBound, int upperBound) {
        Note n = new Note((new Random()).nextInt() % (upperBound - lowerBound) + lowerBound, 127, 500);
        
        synth.playChord(Chord.createMajor(n), 40);
        synth.playChord(Chord.createMinor(n), 40);
    }
    
    public void start() {
        if(!playing) {
            thread = new Thread(this);
            thread.start();
            playing = true;
        }
    }
    
    public void stop() {
        playing = false;
        try { thread.join(); } catch(Exception e) { }
    }
    
    @Override
    public void run() {
        do {
            //randomlyGenerate(36, 48);
            randomlyGenerate(64, 72);
        } while(playing);
    }
    
    public boolean isPlaying() {
        return playing;
    }
}