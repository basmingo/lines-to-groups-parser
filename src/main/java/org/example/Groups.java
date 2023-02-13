package org.example;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Groups {
    private static final String REGEXP_NUMBERS_FILTER = "\"\\d*\"";
    private final Pattern pattern;
    private AtomicInteger lastGroupIndex;
    private final List<Map<Long, AtomicInteger>> numbersToIndexMap;
    private final Map<Integer, List<AtomicInteger>> groupNeighbors;
    private final Map<AtomicInteger, List<List<Long>>> groups;
    private long uniqueNumber;
    private final List<Long> tempList;

    public Groups() {
        groupNeighbors = new HashMap<>();
        numbersToIndexMap = new ArrayList<>();
        numbersToIndexMap.add(new HashMap<>());
        tempList = new CopyOnWriteArrayList<>();
        lastGroupIndex = new AtomicInteger(0);
        groups = new HashMap<>();
        pattern = Pattern.compile(REGEXP_NUMBERS_FILTER);
        uniqueNumber = -1;
    }

    public void add(String line) {
        int columnPointer = 0;
        Matcher matcher = pattern.matcher(line);
        List<AtomicInteger> overlappingGroupIndexes = new ArrayList<>();
        tempList.clear();
        Map<Integer, Set<AtomicInteger>> cacheMap = new HashMap<>();

        while (matcher.find()) {
            Long currentNumber = Validator.getLong(matcher.group());
            tempList.add(Objects.requireNonNullElseGet(currentNumber, () -> --uniqueNumber));

            if (numbersToIndexMap.size() > columnPointer
                    && currentNumber != null) {
                if (numbersToIndexMap.get(columnPointer).containsKey(currentNumber)) {
                    AtomicInteger currentNumberIndex = numbersToIndexMap.get(columnPointer).get(currentNumber);
                    cacheMap.computeIfAbsent(currentNumberIndex.get(), x -> new HashSet<>()).add(currentNumberIndex);
                    overlappingGroupIndexes.add(currentNumberIndex);

                    if (groupNeighbors.get(currentNumberIndex.get()) != null) {
                        overlappingGroupIndexes.addAll(groupNeighbors.get(currentNumberIndex.get()));
                    }
                }
            }
            columnPointer++;
        }

        AtomicInteger index;
        if (overlappingGroupIndexes.isEmpty()) {
            lastGroupIndex = new AtomicInteger(lastGroupIndex.get());
            lastGroupIndex.incrementAndGet();
            index = lastGroupIndex;

        } else if (overlappingGroupIndexes.size() > 1) {
            List<AtomicInteger> id = new ArrayList<>();

            for (AtomicInteger groupIndex : overlappingGroupIndexes) {
                if (cacheMap.containsKey(groupIndex.get())) {
                    id.addAll(cacheMap.get(groupIndex.get()));
                }
            }

            overlappingGroupIndexes.addAll(id);
            rearrange(overlappingGroupIndexes);
            index = overlappingGroupIndexes.stream().findFirst().orElseThrow();
        } else {
            index = overlappingGroupIndexes.stream().findFirst().orElseThrow();
        }

        IntStream.range(0, tempList.size())
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

    private void rearrange(List<AtomicInteger> indexesToRearrange) {
        int key = indexesToRearrange.get(0).get();
        IntStream.range(1, indexesToRearrange.size())
                .forEach(i -> indexesToRearrange.get(i).set(key));

        List<AtomicInteger> value = indexesToRearrange.subList(1, indexesToRearrange.size());
        groupNeighbors.put(key, value);
    }

    public long getCount() {
        Map<Integer, Set<List<Long>>> preResult = new HashMap<>();
        groups.forEach((key, value) -> value
                .forEach(value1 -> preResult
                        .computeIfAbsent(key.get(), x -> new HashSet<>()).addAll(value)));

        Map<Integer, Set<List<Long>>> result = new HashMap<>();
        AtomicInteger a = new AtomicInteger();
        preResult.forEach((key, value) -> result.put(a.incrementAndGet(), value));

        List<Map.Entry<Integer, Set<List<Long>>>> entries = new ArrayList<>(result.entrySet());
        entries.sort(Comparator.comparing(entry -> -entry.getValue().size()));

        long resultSize = preResult.entrySet()
                .stream()
                .filter(x -> x.getValue()
                        .stream()
                        .filter(Objects::nonNull)
                        .count() > 1)
                .count();

        System.out.println("groups with longer than one element - " + resultSize);
        for (var i : entries) {
            StringBuilder sb = new StringBuilder();
            System.out.print("Group name - ");
            System.out.println(i.getKey());
            for (var j : i.getValue()) {
                sb.append(j).append("\n");
            }
            System.out.println(sb);
        }

        return resultSize;
    }
}