package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {
        Validator validator = new Validator();
        Groups groups = new Groups();

        InputStream in = (Main.class.getClassLoader().getResourceAsStream("lng-4.txt"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if (validator.isValid(line)) {
                groups.add(line);
            }
        }
        System.out.println(groups.getCount());
    }
}