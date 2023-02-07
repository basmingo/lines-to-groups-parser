package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private final static String REGEXP_VALIDATION = "^(\"\\d*\";)*\"\\d*\"$";

    public boolean isValid(String line) {
        return line.matches(REGEXP_VALIDATION);
    }
}
