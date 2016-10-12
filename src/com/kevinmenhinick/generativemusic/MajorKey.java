package com.kevinmenhinick.generativemusic;

public class MajorKey extends Key {
    
    public MinorKey relativeMinor() {
        return new MinorKey();
    }
    
}
