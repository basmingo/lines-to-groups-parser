package org.example;

import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidatorTest {
    private final BufferedReader reader;
    private final Validator validator;

    public ValidatorTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("src/test/resources/baseValidatorTest.txt"));
        this.validator = new Validator();
    }

    @Test
    public void basicValidatorTest() throws IOException {
        int countOfGoodLines = 0;
        String line;
        while((line = reader.readLine()) != null) {
            if (validator.isValid(line)) {
                countOfGoodLines++;
                System.out.println(line);
            }
        }

        assertEquals(7, countOfGoodLines);
    }
}
