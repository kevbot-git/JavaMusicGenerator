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
    private int instrument;
    
    public Synth(int instrument) {
        this.instrument = instrument;
    }
    
    public Synth() {
        this(0);
    }
    
    public void playNote(Note note) {
        try {
            channels[0].noteOn(note.getNote(), note.getVelocity());
            Thread.sleep(note.getMillis());
            channels[0].noteOff(note.getNote());
        } catch(InterruptedException e) { }
    }
    
    public void open() throws GeneratorException {
        try {
            midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();
            
            instruments = midiSynth.getDefaultSoundbank()
                    .getInstruments();
            channels = midiSynth.getChannels();
            
            midiSynth.loadInstrument(instruments[instrument]);
            
            channels[0].programChange(instrument);
            
            System.out.println(instruments[instrument].getName());
        } catch(MidiUnavailableException e) {
            throw new GeneratorException();
        }
    }
}
