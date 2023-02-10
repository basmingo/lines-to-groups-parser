package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Validator validator = new Validator();
        Groups groups = new Groups();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/lng-4.txt"));
        String line;
        while((line = bufferedReader.readLine()) != null) {
//            System.out.println(line);
            if (validator.isValid(line)) {
                groups.add(line);
            }
        }

    }
}