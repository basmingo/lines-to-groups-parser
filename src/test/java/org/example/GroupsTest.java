package org.example;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupsTest {
    private final BufferedReader reader;

    private final Groups groups;

    public GroupsTest() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("src/test/resources/baseFunctionalTest.txt"));
        this.groups = new Groups();
    }

    @Test
    public void basicParsingTest() throws IOException {
        String line;
        while((line = reader.readLine()) != null) {
            groups.add(line);
        }

        assertEquals(5, groups.getCount());
    }

    @Test
    public void parseStringTest() {
        assertTrue(groups.getInteger("\"100\"").isPresent());
        assertTrue(groups.getInteger("\"\"").isEmpty());
    }
}