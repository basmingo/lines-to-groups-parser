package org.example;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GroupsTest {
    private final BufferedReader reader;

    private Groups groups;

    public GroupsTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("src/test/resources/baseFunctionalTest.txt"));
        this.groups = new Groups();
    }

    @Test
    public void basicParsingTest() throws IOException {
        groups = new Groups();
        String line;
        while((line = reader.readLine()) != null) {
            groups.add(line);
        }

        assertEquals(3, groups.getCountAndWriteToFile());
    }

    @Test
    public void parseStringTest() {
        assertNotNull(Validator.getLong("\"100\""));
        assertNull(Validator.getLong("\"\""));
    }
}
