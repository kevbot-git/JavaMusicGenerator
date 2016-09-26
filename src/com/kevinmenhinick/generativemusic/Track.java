package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;

public class Track implements Runnable {
    
    private Synth instrument;
    private Thread thread;
    private boolean playing;
    public int interval = 300;
    
    public Track(Synth instrument) {
        this.instrument = instrument;
        this.playing = false;
        try {
            instrument.open();
        } catch(GeneratorException e) {
            System.out.println(e);
        }
    }
    
    private void generate() {
        instrument.playNote(new Note(35, 127, interval));
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
            generate();
        } while(playing);
    }
    
    public Synth getInstrument() {
        return instrument;
    }
}
