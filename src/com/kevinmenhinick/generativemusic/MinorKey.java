package com.kevinmenhinick.generativemusic;

public class MinorKey extends Key {
    
    public MinorKey(int rootNote) {
        super(rootNote);
    }
    
    public MajorKey relativeMajor() {
        return new MajorKey((getRoot() + 3) % 12);
    }
    
    @Override
    public String of() {
        return super.of() + "min";
    }

    @Override
    public Key relative() {
        return relativeMajor();
    }
}
