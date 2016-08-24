/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kevinmenhinick.generativemusic;

import javax.sound.midi.MidiChannel;

public class Note {
    
    private int note;
    private int velocity;
    private int millis;
    
    public Note(int note, int velocity, int millis) {
        this.note = note;
        this.velocity = velocity;
        this.millis = millis;
    }

    public int getNote() {
        return note;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getMillis() {
        return millis;
    }
}
