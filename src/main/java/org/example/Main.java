package org.example;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        double startTime = System.currentTimeMillis();
        Groups groups = new Groups();
        InputStream in = (new FileInputStream(args[0]));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), 8192);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            if (Validator.isValid(line)) {
                groups.add(line);
            }
        }

        groups.getCountAndWriteToFile();
        double endTime = System.currentTimeMillis();
        double resultTime = (endTime - startTime) / 1000;
        System.out.println("General time of execution - " + resultTime + " seconds");
    }
}