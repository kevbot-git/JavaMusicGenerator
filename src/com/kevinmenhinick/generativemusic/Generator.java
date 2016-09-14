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
        Note n = new Note((new Random()).nextInt() % (upperBound - lowerBound) + lowerBound, 127, 500);
        Note n1 = new Note(n.getNote() + 4, 127, 500);
        Note n2 = new Note(n.getNote() + 7, 127, 500);
        
        synth.playChord(new Chord(n, n1, n2));
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
            randomlyGenerate(64, 72);
        } while(playing);
    }
    
    public boolean isPlaying() {
        return playing;
    }
}