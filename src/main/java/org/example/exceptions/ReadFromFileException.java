package org.example.exceptions;

public class ReadFromFileException extends RuntimeException{
    public ReadFromFileException(String s) {
        super(s);
    }
}
