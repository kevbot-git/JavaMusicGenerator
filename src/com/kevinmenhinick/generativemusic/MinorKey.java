package com.kevinmenhinick.generativemusic;

public class MinorKey extends Key {
    
    public MajorKey relativeMajor() {
        return new MajorKey();
    }
    
    @Override
    public String of() {
        return super.of() + "min";
    }
}
