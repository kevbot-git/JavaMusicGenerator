package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class Synth {
    
    private Synthesizer midiSynth;
    private Receiver receiver;
    private MidiDevice receiverDevice;
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
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, 0, note.getNote(), note.getVelocity());
            receiver.send(msg, -1);
            Thread.sleep(millis);
            msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, 0, note.getNote(), note.getVelocity());
            receiver.send(msg, -1);
        } catch(InterruptedException e) { } catch (InvalidMidiDataException ex) {
            Logger.getLogger(Synth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void playChord(Chord chord, int millis, int strumTime) {
        try {
            Iterator<Note> it = chord.getIterator();
            while(it.hasNext()) {
                Note n = it.next();
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_ON, 0, n.getNote(), n.getVelocity());
                receiver.send(msg, -1);
                if(strumTime > 0) Thread.sleep(strumTime);
            }
            Thread.sleep(millis);
            it = chord.getIterator();
            while(it.hasNext()) {
                ShortMessage msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_OFF, 0, it.next().getNote(), 0);
                receiver.send(msg, -1);
            }
        } catch(InterruptedException e) {
            
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(Synth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void playChord(Chord chord, int millis) {
        playChord(chord, millis, 0);
    }
    
    public void open() throws GeneratorException {
        try {
            boolean first = true;
            for (MidiDevice.Info info: MidiSystem.getMidiDeviceInfo()) {
                if (info.getName().equals("LoopBe Internal MIDI")) {
                    if (!first) {
                        receiverDevice = MidiSystem.getMidiDevice(info);
                        receiverDevice.open();
                        receiver = receiverDevice.getReceiver();
                    } else {
                        first = false;
                    }
                }
            }
            
            if (receiverDevice == null)
                throw new GeneratorException("Driver not found. Have you installed LoopBe yet?");
            
            midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();
            
            instruments = midiSynth.getDefaultSoundbank()
                    .getInstruments();
            channels = midiSynth.getChannels();
            
            midiSynth.loadInstrument(instruments[instrument]);
            
            channels[channel].programChange(instrument);
        } catch(MidiUnavailableException e) {
            System.out.println(e);
            throw new GeneratorException();
        }
    }
}
