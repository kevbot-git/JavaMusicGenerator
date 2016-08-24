package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Synth {
    
    private Synthesizer midiSynth;
    private Instrument[] instruments;
    private MidiChannel[] channels;
    
    public Synth() {
        
    }
    
    public void playNote(int note, int velocity, int millis) {
        try {
            channels[0].noteOn(note, velocity);
            Thread.sleep(millis);
            channels[0].noteOff(note);
        } catch(InterruptedException e) { }
    }
    
    public void open() throws GeneratorException {
        try {
            midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();
            
            instruments = midiSynth.getDefaultSoundbank()
                    .getInstruments();
            channels = midiSynth.getChannels();
            
            midiSynth.loadInstrument(instruments[0]);
        } catch(MidiUnavailableException e) {
            throw new GeneratorException();
        }
    }
}
