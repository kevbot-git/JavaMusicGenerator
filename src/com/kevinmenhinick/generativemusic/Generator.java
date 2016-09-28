package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generator implements Runnable {
    
    private List<Track> syncedTracks;
    private Thread thread;
    private boolean playing;
    private Beat beat;
    
    public Generator() throws GeneratorException {
        Random r = new Random();
        beat = new Beat(randRange(r, 110, 300), 4);
        syncedTracks = Collections.synchronizedList(new ArrayList<Track>());
        syncedTracks.add(new DrumTrack(new Drums(), beat));
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
                Thread.sleep(60000 / beat.tempo());
                synchronized (beat) {
                    beat.nextBeat();
                    beat.notifyAll();
                    //System.out.println("Notified...");
                }
            } catch(InterruptedException e) { }
            //System.out.println("Notified...");
        } while(playing);
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public static int randRange(Random r, int min, int max) {
        return (Math.abs(r.nextInt()) % (max - min) + min);
    }
}