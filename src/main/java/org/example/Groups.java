package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Groups {
    private static final String REGEXP_NUMBERS_FILTER = "\"\\d*\"";
    private final Pattern pattern;
    private int[] lastGroupIndex;
    private final List<Map<Long, int[]>> numbersToIndexMap;
    private final Map<Integer, List<int[]>> groupNeighbors;
    private final Map<int[], List<List<Long>>> groups;
    private long uniqueNumber;
    List<Long> tempList;
    List<int[]> overlappingGroupIndexes;

    public Groups() {
        pattern = Pattern.compile(REGEXP_NUMBERS_FILTER);
        uniqueNumber = -1;
        numbersToIndexMap = new ArrayList<>();
        groupNeighbors = new HashMap<>();
        groups = new HashMap<>();
        numbersToIndexMap.add(new ConcurrentHashMap<>());
        lastGroupIndex = new int[]{0};
        tempList = new CopyOnWriteArrayList<>();
    }

    public void add(String line) {
        int columnPointer = 0;
        Matcher matcher = pattern.matcher(line);
        overlappingGroupIndexes = new ArrayList<>();
        tempList.clear();

        while (matcher.find()) {
            Long currentNumber = Validator.getLong(matcher.group());
            tempList.add(Objects.requireNonNullElseGet(currentNumber, () -> --uniqueNumber));

            if (numbersToIndexMap.size() > columnPointer
                    && currentNumber != null
                    && numbersToIndexMap.get(columnPointer).containsKey(currentNumber)) {

                int[] currentNumberIndex = numbersToIndexMap.get(columnPointer).get(currentNumber);
                overlappingGroupIndexes.add(currentNumberIndex);

                if (groupNeighbors.get(currentNumberIndex[0]) != null) {
                    overlappingGroupIndexes.addAll(groupNeighbors.get(currentNumberIndex[0]));
                }
            }
            columnPointer++;
        }

        int[] index;
        if (overlappingGroupIndexes.isEmpty()) {
            lastGroupIndex = new int[]{lastGroupIndex[0]};
            lastGroupIndex[0]++;
            index = lastGroupIndex;

        } else if (overlappingGroupIndexes.size() > 1) {
            List<int[]> id = new ArrayList<>();
            overlappingGroupIndexes.forEach(entry ->
                    numbersToIndexMap.parallelStream()
                            .filter(x -> x.containsValue(entry))
                            .flatMap(y -> y.values().parallelStream())
                            .filter(z -> z[0] == entry[0])
                            .forEach(id::add)
            );

            overlappingGroupIndexes.addAll(id);
            rearrange(overlappingGroupIndexes);
            index = overlappingGroupIndexes.stream().findFirst().orElseThrow();
        } else {
            index = overlappingGroupIndexes.stream().findFirst().orElseThrow();
        }

        IntStream.range(0, tempList.size())
//                .parallel()
                .forEach(x -> {
                    if (numbersToIndexMap.size() <= x) {
                        numbersToIndexMap.add(new HashMap<>());
                    }
                    numbersToIndexMap.get(x).putIfAbsent(tempList.get(x), index);
                });

        List<Long> n2 = tempList.stream()
                .map(i -> i < 0 ? null : i)
                .collect(Collectors.toList());

        groups.putIfAbsent(index, new ArrayList<>());
        groups.get(index).add(n2);
    }

    private void rearrange(List<int[]> indexesToRearrange) {
        int key = indexesToRearrange.get(0)[0];
        IntStream.range(1, indexesToRearrange.size())
                .forEach(i -> indexesToRearrange.get(i)[0] = key);

        List<int[]> value = indexesToRearrange.subList(1, indexesToRearrange.size());
        groupNeighbors.put(key, value);
    }

    public long getCount() {
        Map<Integer, List<List<Long>>> result = new HashMap<>();
        groups.forEach((key, value1) -> value1
                .forEach(value -> result
                        .computeIfAbsent(key[0], x -> new ArrayList<>()).add(value)));

        return result.entrySet()
                .parallelStream()
                .filter(x -> x.getValue()
                        .parallelStream()
                        .filter(Objects::nonNull)
                        .count() > 1)
                .count();
    }
}