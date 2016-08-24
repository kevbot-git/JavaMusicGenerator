package com.kevinmenhinick.generativemusic.exception;

public class GeneratorException extends Exception {
    
    public GeneratorException() {
        this("Unspecified error");
    }
    
    public GeneratorException(String message) {
        super("Generator: " + message);
    }
}
