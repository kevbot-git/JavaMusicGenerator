package com.kevinmenhinick.generativemusic;

public class MajorKey extends Key {
    
    public MajorKey(int rootNote) {
        super(rootNote);
    }
    
    public MinorKey relativeMinor() {
        return new MinorKey((getRoot() + 9) % 12);
    }

    @Override
    public Key relative() {
        return relativeMinor();
    }
    
}
