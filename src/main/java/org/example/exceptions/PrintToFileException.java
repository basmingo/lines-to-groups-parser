package org.example.exceptions;

public class PrintToFileException extends RuntimeException {
    public PrintToFileException(String s) {
        super(s);
    }
}
