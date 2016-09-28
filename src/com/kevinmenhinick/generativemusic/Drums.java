package com.kevinmenhinick.generativemusic;

import java.util.Random;

public class Drums extends Synth {
    
    public static final int STICK_HIT = 31;
    public static final int KICK_1 = 35;
    public static final int KICK_2 = 36;
    public static final int SNARE_RIM = 37;
    public static final int SNARE_1 = 38;
    public static final int SNARE_2 = 40;
    public static final int HAT_CLOSED = 42;
    public static final int HAT_OPEN = 46;
    public static final int RIDE = 51;
    public static final int CLAP = 39;
    public static final int TOM_1 = 41;
    public static final int TOM_2 = 43;
    
    public Drums() {
        super(9, 0);
    }
    
    public static int randomBassHit() {
        return randomFrom(new int[] {KICK_1, KICK_2});
    }
    
    
    public static int randomAccentHit() {
        return randomFrom(new int[] {SNARE_1, SNARE_2, CLAP});
    }
    
    public static int randomTickHit() {
        return randomFrom(new int[] {HAT_CLOSED, RIDE});
    }
    
    private static int randomFrom(int[] selection) {
        return selection[Math.abs(new Random().nextInt()) % selection.length];
    }
}
