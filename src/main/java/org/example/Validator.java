package org.example;

public class Validator {
    private final static String REGEXP_VALIDATION = "^(\"\\d*\";)*\"\\d*\"$";

    public static boolean isValid(String line) {
        return line.matches(REGEXP_VALIDATION);
    }

    public static Long getLong(String inputString) {
        if (inputString.equals("\"\"")) {
            return null;
        }
        return Long.parseLong(inputString.replaceAll("\"", ""));
    }
}
