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
        //synth = new Synth();
        drums = new Drums();
        //synth.open();
        drums.open();
    }

    private void playBar() {
        ArrayList<Note> sequence = new ArrayList();
        sequence.add(new Note(60, 127, 750));
        sequence.add(new Note(62, 127, 250));
        sequence.add(new Note(64, 127, 750));
        sequence.add(new Note(60, 127, 250));
        sequence.add(new Note(64, 127, 500));
        sequence.add(new Note(60, 127, 500));
        sequence.add(new Note(64, 127, 1000));
        
        for(Note n: sequence) {
            if(playing) {
                //synth.playNote(n);
            }
        }
    }
    
    private void randomlyGenerate(int lowerBound, int upperBound) {
        do {
            Note n = new Note((new Random()).nextInt() % (upperBound - lowerBound) + lowerBound, 127, 500);
            drums.playNote(n);
        } while(playing);
    }
    
    public void start() {
        thread = new Thread(this);
        thread.start();
        playing = true;
    }
    
    public void stop() {
        playing = false;
        try { thread.join(); } catch(Exception e) { }
    }
    
    @Override
    public void run() {
        randomlyGenerate(60, 72);
    }
}
