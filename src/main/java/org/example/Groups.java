package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Groups {
    private final List<Map<Integer, Integer>> columnMap;
    private final List<SortedSet<List<Integer>>> groupList;
    private static final String REGEXP_NUMBERS_FILTER = "\"\\d*\"";
    private final Pattern pattern;
    private Integer groupSize;

    public Groups() {
        columnMap = new ArrayList<>();
        columnMap.add(new HashMap<>());
        groupList = new ArrayList<>();
        groupList.add(new TreeSet<>(Comparator.comparingInt(List::size)));

        pattern = Pattern.compile(REGEXP_NUMBERS_FILTER);
        groupSize = 0;
    }

    public void add(String line) {
        Matcher matcher = pattern.matcher(line);
        boolean isMatching = false;
        List<Integer> n = new ArrayList<>();

        int columnPointer = 0;
        while (matcher.find()) {
            getInteger(matcher.group());
            Integer i = getInteger(matcher.group()).orElse(null);
            n.add(i);
            if (columnMap.size() > columnPointer && columnMap.get(columnPointer).containsKey(i)) {
                isMatching = true;
            }
            columnPointer++;
        }

        if (!isMatching) {
            groupSize++;
        }

        for (int i = 0; i < n.size(); i++) {
            if (columnMap.size() > i) {
                columnMap.get(i).putIfAbsent(n.get(i), groupSize);
            } else {
                columnMap.add(new HashMap<>());
                columnMap.get(i).putIfAbsent(n.get(i), groupSize);
            }
        }
        System.out.println(columnMap);
    }

    public Optional<Integer> getInteger(String inputString) {
        if (inputString.equals("\"\"")) {
            return Optional.empty();
        }
        return Optional.of(Integer
                .parseInt(inputString
                        .replaceAll("\"", "")));
    }

    public int getCount() {
        return groupSize;
    }
}
