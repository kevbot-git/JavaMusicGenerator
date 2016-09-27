package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generator implements Runnable {
    
    private List<Track> syncedTracks;
    private Thread thread;
    private boolean playing;
    private int tempo;
    private BeatTracker beat;
    
    public Generator() throws GeneratorException {
        beat = new BeatTracker(4);
        syncedTracks = Collections.synchronizedList(new ArrayList<Track>());
        syncedTracks.add(new Track(new Drums(), beat));
        tempo = 120;
    }
    
    public void start() {
        if(!playing) {
            thread = new Thread(this);
            playing = true;
            thread.start();
            for(Track t: syncedTracks)
                t.start();
        }
    }
    
    public void stop() {
        playing = false;
        for(Track t: syncedTracks)
            t.stop();
    }
    
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
                synchronized (beat) {
                    beat.notifyAll();
                }
            } catch(InterruptedException e) { }
            System.out.println("Notified...");
            beat.nextBeat();
        } while(playing);
    }
    
    public boolean isPlaying() {
        return playing;
    }
}