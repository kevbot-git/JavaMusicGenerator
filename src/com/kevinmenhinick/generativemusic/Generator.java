package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.sound.midi.MidiDevice;

public class Generator implements Runnable {
    
    private List<Track> syncedTracks;
    private Thread thread;
    private boolean playing;
    private Key key;
    private Beat beat;
    private BeatAction beatAction;
    private MidiDevice.Info device;
    
    public Generator(MidiDevice.Info device) throws GeneratorException {
        this.device = device;
        Random r = new Random();
        key = (r.nextBoolean())? new MajorKey(Math.abs(r.nextInt())): new MinorKey(Math.abs(r.nextInt()));
        beat = new Beat(randRange(r, 110, 250), 4);
        Synth.nextChannel = 0;
        syncedTracks = Collections.synchronizedList(new ArrayList<Track>());
        
        // Drone track
        syncedTracks.add(new Track(new Synth(device), beat) {
            int total = 0;
            @Override
            public void generateOnBeat(Beat beat) {
                getInstrument().playNote(new Note(key.getRoot() + 36, 127), beat.length(9, 10));
            }
        });
        
        // Chord track
        syncedTracks.add(new Track(new Synth(device), beat) {
            int total = 0;
            @Override
            public void generateOnBeat(Beat beat) {
                if(total++ % 8 == 0) {
                    // Possibly change key
                    if(r.nextInt() % 10 == 0) {
                        if(r.nextInt() % 3 == 0)
                            key = key.modulate((Math.abs(r.nextInt()) % 6) * 2 - 5);
                        else
                            key = key.relative();
                        if(beatAction != null)
                            beatAction.onChordChange(key.of());
                    }
                    
                    // Play chord
                    if(key instanceof MajorKey) {
                        getInstrument().playChord(Chord.createMajor(new Note(key.getRoot() + 48 + ((r.nextInt() % 2 == 0)? 0: 7), 127)), beat.length(2, 1));
                    }
                    else {
                        getInstrument().playChord(Chord.createMinor(new Note(key.getRoot() + 48 + ((r.nextInt() % 2 == 0)? 0: 7), 127)), beat.length(2, 1));
                    }
                }
                
            }
        });
        
        //Melody track
        syncedTracks.add(new Track(new Synth(device), beat) {
            int total = 0;
            int length = (int) Math.pow(2, ((Math.abs(r.nextInt()) % 4) + 1));
            @Override
            public void generateOnBeat(Beat beat) {
                if(++total == length) {
                    int rootNote = (key instanceof MajorKey)? (Scale.MAJOR[Math.abs(r.nextInt()) % Scale.MAJOR.length]):
                            (Scale.MINOR_NATURAL[Math.abs(r.nextInt()) % Scale.MINOR_NATURAL.length]);
                    getInstrument().playNote(new Note(rootNote + 48 + key.getRoot(), 127), beat.length(length * 2 - 1, 2));
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
    
    public void setBeatAction(BeatAction beatAction) {
        this.beatAction = beatAction;
    }
    
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(60000 / beat.tempo());
                if(beatAction != null)
                    (new Thread(new BeatActionRunner(beatAction))).start();
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
    
    public String getKey() {
        return key.of();
    }
    
    public int getTempo() {
        return this.beat.tempo();
    }
}