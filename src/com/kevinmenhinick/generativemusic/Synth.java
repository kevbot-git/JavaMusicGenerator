package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.Iterator;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Synth {
    
    private Synthesizer midiSynth;
    private Instrument[] instruments;
    private MidiChannel[] channels;
    private int instrument;
    private int channel;
    
    public Synth(int channel, int instrument) {
        this.channel = channel;
        this.instrument = instrument;
    }
    
    public Synth() {
        this(0, 0);
    }
    
    public void playNote(Note note, int millis) {
        try {
            channels[channel].noteOn(note.getNote(), note.getVelocity());
            Thread.sleep(millis);
            channels[channel].noteOff(note.getNote());
        } catch(InterruptedException e) { }
    }
    
    public void playChord(Chord chord, int millis, int strumTime) {
        try {
            Iterator<Note> it = chord.getIterator();
            while(it.hasNext()) {
                Note n = it.next();
                channels[channel].noteOn(n.getNote(), n.getVelocity());
                if(strumTime > 0) Thread.sleep(strumTime);
            }
            Thread.sleep(millis);
            it = chord.getIterator();
            while(it.hasNext()) {
                channels[channel].noteOff(it.next().getNote());
            }
        } catch(InterruptedException e) { }
    }
    
    public void playChord(Chord chord, int millis) {
        playChord(chord, millis, 0);
    }
    
    public void open() throws GeneratorException {
        try {
            midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();
            
            instruments = midiSynth.getDefaultSoundbank()
                    .getInstruments();
            channels = midiSynth.getChannels();
            
            midiSynth.loadInstrument(instruments[instrument]);
            
            channels[channel].programChange(instrument);
        } catch(MidiUnavailableException e) {
            throw new GeneratorException();
        }
    }
}
