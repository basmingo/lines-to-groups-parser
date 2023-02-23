package org.example;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LineHandlerTest {
    private final BufferedReader reader;
    private LineHandler lineHandler;

    public LineHandlerTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("src/test/resources/baseFunctionalTest.txt"));
        this.lineHandler = new LineHandler();
    }

    @Test
    void basicParsingTest() throws IOException {
        lineHandler = new LineHandler();
        String line;
        while((line = reader.readLine()) != null) {
            lineHandler.add(line);
        }
        assertEquals(3, lineHandler.getCountAndWriteToFile());
    }
}
