package com.kevinmenhinick.generativemusic;

public class BeatActionRunner implements Runnable {
    
    private BeatAction action;
    
    public BeatActionRunner(BeatAction action) {
        this.action = action;
    }
    
    @Override
    public void run() {
        action.onBeat();
    }
}
