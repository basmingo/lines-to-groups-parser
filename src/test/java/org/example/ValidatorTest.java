package org.example;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatorTest {
    private final BufferedReader reader;

    public ValidatorTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("src/test/resources/baseValidatorTest.txt"));
    }

    @Test
    void basicValidatorTest() throws IOException {
        int countOfGoodLines = 0;
        String line;
        while((line = reader.readLine()) != null) {
            if (Validator.validateAndGetLongArray(line).length != 0) {
                countOfGoodLines++;
            }
        }
        assertEquals(7, countOfGoodLines);
    }
}
