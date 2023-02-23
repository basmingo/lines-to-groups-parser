package org.example;

import org.example.exceptions.PrintToFileException;
import org.example.exceptions.ReadFromFileException;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        double startTime = System.currentTimeMillis();
        LineHandler groups = new LineHandler();
        try (InputStream in = new FileInputStream(args[0])) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), 8192);
            String line;
            Set<String> uniqueRows = new HashSet<>();
            while ((line = bufferedReader.readLine()) != null) {
                uniqueRows.add(line);
            }

            for (var row : uniqueRows) {
                groups.add(row);
            }

            groups.getCountAndWriteToFile();
            double endTime = System.currentTimeMillis();
            double resultTime = (endTime - startTime) / 1000;
            System.out.println("General time of execution - " + resultTime + " seconds");

        } catch (IOException e) {
            throw new ReadFromFileException("No such file in specified location");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ReadFromFileException("Parsing file have not been specified");
        }
    }

    public static void writeResultToFile(List<Map.Entry<Integer, Set<String>>> entries, long resultSize) {
        String jarLocation = Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

        File jarFile = new File(jarLocation);
        File fileLocation = new File(jarFile.getParentFile(), "result.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLocation))) {
            bufferedWriter.write("groups with longer than one element - " + resultSize + "\n");
            for (var i : entries) {
                StringBuilder sb = new StringBuilder();
                bufferedWriter.write("Group name - ");
                bufferedWriter.write(i.getKey() + "\n");

                for (var j : i.getValue()) {
                    sb.append(j).append("\n");
                }
                bufferedWriter.write(sb + "\n");
            }
        } catch (IOException e) {
            throw new PrintToFileException("Something went wrong while writing to file");
        }
    }
}