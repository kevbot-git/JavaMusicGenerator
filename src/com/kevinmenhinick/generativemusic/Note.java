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
    
    public Note(int note, int velocity) {
        this.note = note;
        this.velocity = velocity;
    }

    public int getNote() {
        return note;
    }

    public int getVelocity() {
        return velocity;
    }
}
