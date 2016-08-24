package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;

public class Generator implements Runnable {
    
    private Synth synth;
    private Thread thread;
    private boolean playing;
    
    public Generator() throws GeneratorException {
        synth = new Synth();
        
        synth.open();
    }

    private void playBar() {
        synth.playNote(60, 127, 1000);
    }
    
    public void start() {
        playing = true;
        thread = new Thread(this);
        thread.start();
        System.out.println("Started");
    }
    
    public void stop() {
        try { thread.join(); } catch(Exception e) { }
        playing = false;
        System.out.println("Stopped");
    }
    
    @Override
    public void run() {
        playBar();
    }
}
