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
        System.out.println("Tempo: " + beat.tempo());
        Synth.nextChannel = 0;
        syncedTracks = Collections.synchronizedList(new ArrayList<Track>());
        
        // Drone track
        syncedTracks.add(new Track(new Synth(), beat) {
            int total = 0;
            @Override
            public void generateOnBeat(Beat beat) {
                System.out.println("Total: " + ++total);
                getInstrument().playNote(new Note(36, 127), beat.length(9, 10));
            }
        });
        
        // Chord track
        syncedTracks.add(new Track(new Synth(), beat) {
            int total = 0;
            @Override
            public void generateOnBeat(Beat beat) {
                if(total++ % 8 == 0)
                    getInstrument().playChord(Chord.createMinor(new Note((r.nextInt() % 2 == 0)? 48: 55, 127)), beat.length(2, 1));
            }
        });
        
        //Melody track
        syncedTracks.add(new Track(new Synth(), beat) {
            int total = 0;
            int length = (int) Math.pow(2, ((Math.abs(r.nextInt()) % 4) + 1));
            @Override
            public void generateOnBeat(Beat beat) {
                if(++total == length) {
                    System.out.println(length);
                    getInstrument().playNote(new Note((Scale.MINOR_NATURAL[Math.abs(r.nextInt()) % Scale.MINOR_NATURAL.length]) + 48, 127), beat.length(length * 2 - 1, 2)); //new Note(r.nextInt()
                    length = (int) Math.pow(2, ((Math.abs(r.nextInt()) % 4)));
                    
                    total = 0;
                }
            }
        });
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
                }
            } catch(InterruptedException e) { }
        } while(playing);
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public static int randRange(Random r, int min, int max) {
        return (Math.abs(r.nextInt()) % (max - min) + min);
    }
}