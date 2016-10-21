package com.kevinmenhinick.generativemusic;

import com.kevinmenhinick.generativemusic.exception.GeneratorException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class Synth {
    
    public static int nextChannel = 0;
    
    private Receiver receiver;
    private MidiDevice receiverDevice;
    private int channel;
    private MidiDevice.Info device;
    
    public Synth(MidiDevice.Info device) {
        this.device = device;
        this.channel = nextChannel++;
    }
    
    public void playNote(Note note, int millis) {
        (new Thread(){
            public void run() {
                try {
                    ShortMessage msg = new ShortMessage();
                    msg.setMessage(ShortMessage.NOTE_ON, channel, note.getNote(), note.getVelocity());
                    receiver.send(msg, -1);
                    Thread.sleep(millis);
                    msg = new ShortMessage();
                    msg.setMessage(ShortMessage.NOTE_OFF, channel, note.getNote(), note.getVelocity());
                    receiver.send(msg, -1);
                } catch(InterruptedException e) { } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(Synth.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    public void playChord(Chord chord, int millis, int strumTime) {
        (new Thread() {
            public void run() {
                try {
                    Iterator<Note> it = chord.getIterator();
                    while(it.hasNext()) {
                        Note n = it.next();
                        ShortMessage msg = new ShortMessage();
                        msg.setMessage(ShortMessage.NOTE_ON, channel, n.getNote(), n.getVelocity());
                        receiver.send(msg, -1);
                        if(strumTime > 0) Thread.sleep(strumTime);
                    }
                    Thread.sleep(millis);
                    it = chord.getIterator();
                    while(it.hasNext()) {
                        ShortMessage msg = new ShortMessage();
                        msg.setMessage(ShortMessage.NOTE_OFF, channel, it.next().getNote(), 0);
                        receiver.send(msg, -1);
                    }
                } catch(InterruptedException e) {

                } catch (InvalidMidiDataException ex) {
                    Logger.getLogger(Synth.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    public void playChord(Chord chord, int millis) {
        playChord(chord, millis, 0);
    }
    
    public void open() throws GeneratorException {
        try {
            receiverDevice = MidiSystem.getMidiDevice(device);
            receiverDevice.open();
            receiver = receiverDevice.getReceiver();
            
            if (receiverDevice == null || receiver == null)
                throw new GeneratorException("Device couldn't be opened");
            
        } catch(MidiUnavailableException e) {
            System.out.println(e);
            throw new GeneratorException("Device couldn't be opened. " + e);
        }
    }
    
    public void close() {
        // Turn all off if necessary
    }
}
