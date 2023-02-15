package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultHandler {
    public void printToFile(List<Map.Entry<Integer, Set<List<Long>>>> entries, long resultSize) {
        String jarLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File jarFile = new File(jarLocation);
        File fileLocation = new File(jarFile.getParentFile(), "result.txt");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLocation))){
            bufferedWriter.write("groups with longer than one element - " + resultSize + "\n");
            for (var i : entries) {
                StringBuilder sb = new StringBuilder();
                bufferedWriter.write("Group name - ");
                bufferedWriter.write(i.getKey() + "\n");
                for (var j : i.getValue()) {
                    sb.append(j).append("\n");
                }
                bufferedWriter.write(sb.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
