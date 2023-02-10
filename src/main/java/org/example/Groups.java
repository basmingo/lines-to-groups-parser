package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Groups {
    private List<Map<Long, Integer>> numbersToIndexMap;
    private final List<List<Set<Long>>> indexToNumbersMap;
    private static final String REGEXP_NUMBERS_FILTER = "\"\\d*\"";
    private final Pattern pattern;
    private Integer lastGroupIndex;

    public Groups() {
        numbersToIndexMap = new ArrayList<>();
        indexToNumbersMap = new ArrayList<>();
        pattern = Pattern.compile(REGEXP_NUMBERS_FILTER);

        numbersToIndexMap.add(new HashMap<>());
        indexToNumbersMap.add(new ArrayList<>());
        lastGroupIndex = 0;
    }

    public void add(String line) {
        Matcher matcher = pattern.matcher(line);
        List<Long> n = new ArrayList<>();

        int columnPointer = 0;
        List<Integer> overlappingGroupIndexes = new ArrayList<>();
        while (matcher.find()) {
            Long currentNumber = getLong(matcher.group()).orElse(null);
            n.add(currentNumber);
            if (numbersToIndexMap.get(columnPointer).containsKey(currentNumber)) {
                Integer currentNumberIndex = numbersToIndexMap.get(columnPointer).get(currentNumber);
                overlappingGroupIndexes.add(currentNumberIndex);
            }
        }

        if (overlappingGroupIndexes.size() > 1) {
            rearange(overlappingGroupIndexes);
        } else if (overlappingGroupIndexes.isEmpty()) {
            lastGroupIndex++;
            indexToNumbersMap.add(new ArrayList<>());
        }

        IntStream.range(0, n.size())
//                .parallel()
                .forEach(i -> {
                    if (numbersToIndexMap.size() <= i) {
                        numbersToIndexMap.add(new HashMap<>());
                    }

                    if (indexToNumbersMap.get(lastGroupIndex).size() <= i) {
                        indexToNumbersMap.get(lastGroupIndex).add(new HashSet<>());
                    }

                    indexToNumbersMap.get(lastGroupIndex).get(i).add(n.get(i));
                    numbersToIndexMap.get(i).putIfAbsent(n.get(i), lastGroupIndex);
                });
    }

    private void rearange(List<Integer> indexesToRearange) {
        List<Map<Long, Integer>> newMap = new ArrayList<>();

        for (int i = 1; i < indexesToRearange.size(); i++) {
            List<Set<Long>> sets = indexToNumbersMap.get(indexesToRearange.get(i));
            indexToNumbersMap.get(indexesToRearange.get(0)).addAll(sets);
            indexToNumbersMap.remove(indexesToRearange.get(i));
        }

        for (int i = 1; i < indexToNumbersMap.size(); i++) {
            for (int j = 0; j < indexToNumbersMap.get(i).size(); j++) {
                if (newMap.size() <= j) {
                    newMap.add(new HashMap<>());
                }
                for (Long k : indexToNumbersMap.get(i).get(j)) {
                    newMap.get(j).put(k, i);
                }
            }
        }

        numbersToIndexMap = newMap;
    }

    public Optional<Long> getLong(String inputString) {
        if (inputString.equals("\"\"")) {
            return Optional.empty();
        }
        return Optional.of(Long
                .parseLong(inputString
                        .replaceAll("\"", "")));
    }

    public int getCount() {
        return indexToNumbersMap.size();
    }
}
