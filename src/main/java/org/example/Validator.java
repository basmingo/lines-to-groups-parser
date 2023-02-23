package org.example;

public class Validator {
    public static Long[] validateAndGetLongArray(String line) {
        String[] lineArray = line.split(";");
        Long[] result = new Long[lineArray.length];

        for (int i = 0; i < lineArray.length; i++) {
            String element = lineArray[i];
            if (!element.startsWith("\"")
                    || !element.endsWith("\"")
                    || element.indexOf('\"', 1) != element.length() - 1) {

                return new Long[0];
            }
            result[i] = (getLong(element));
        }
        return result;
    }

    private static Long getLong(String element) {
        element = element.replace("\"", "");
        if (element.isEmpty()) {
            return null;
        }
        double parsedElement = Double.parseDouble(element) * 100;
        return (long) parsedElement;
    }
}
