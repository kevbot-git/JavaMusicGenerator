package com.kevinmenhinick.generativemusic;

public interface BeatAction {
    public void onBeat();
    public void onChordChange(String chord);
}
