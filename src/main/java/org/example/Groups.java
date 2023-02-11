package org.example;

import java.util.*;
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
    private final Map<Integer, Set<AtomicInteger>> groupNeighbors;

    public Groups() {
        numbersToIndexMap = new ArrayList<>();
        pattern = Pattern.compile(REGEXP_NUMBERS_FILTER);
        groupNeighbors = new HashMap<>();

        numbersToIndexMap.add(new HashMap<>());
        lastGroupIndex = new AtomicInteger(0);
    }

    public void add(String line) {
        Matcher matcher = pattern.matcher(line);
        List<Long> n = new ArrayList<>();

        int columnPointer = 0;
        LinkedHashSet<AtomicInteger> overlappingGroupIndexes = new LinkedHashSet<>();

        while (matcher.find()) {
            Long currentNumber = getLong(matcher.group());
            n.add(currentNumber);
            if (numbersToIndexMap.size() > columnPointer && numbersToIndexMap.get(columnPointer).containsKey(currentNumber) && currentNumber != null) {
                AtomicInteger currentNumberIndex = numbersToIndexMap.get(columnPointer).get(currentNumber);
                overlappingGroupIndexes.add(currentNumberIndex);

                if (groupNeighbors.get(currentNumberIndex.get()) != null) {
                    overlappingGroupIndexes.addAll(groupNeighbors.get(currentNumberIndex.get()));
                }
            }
            columnPointer++;
        }
        AtomicInteger index;
        if (overlappingGroupIndexes.size() > 1) {
            rearange(overlappingGroupIndexes);
            index = overlappingGroupIndexes.stream().findFirst().get();
        } else if (overlappingGroupIndexes.isEmpty()) {
            lastGroupIndex = new AtomicInteger(lastGroupIndex.get());
            lastGroupIndex.incrementAndGet();
            index = lastGroupIndex;
        } else {
            index = overlappingGroupIndexes.stream().findFirst().get();
        }

        IntStream.range(0, n.size())
                .forEach(i -> {
                    if (numbersToIndexMap.size() <= i) {
                        numbersToIndexMap.add(new HashMap<>());
                    }
                    numbersToIndexMap.get(i).putIfAbsent(n.get(i), index);
                });
    }

    private void rearange(LinkedHashSet<AtomicInteger> indexesToRearrange) {
        int firstElement = indexesToRearrange
                .stream()
                .findFirst()
                .get()
                .get();

        Set<AtomicInteger> mod = indexesToRearrange
                .stream()
                .skip(1)
                .collect(Collectors.toSet());

        mod.forEach(x -> x.set(firstElement));
        groupNeighbors.put(firstElement, mod);
    }

    public Long getLong(String inputString) {
        if (inputString.equals("\"\"")) {
            return null;
        }
        return Long.parseLong(inputString.replaceAll("\"", ""));
    }

    public int getCount() {
        AtomicInteger count = new AtomicInteger();
        Set<Integer> i2 = new HashSet<>();
        for (Map<Long, AtomicInteger> i : numbersToIndexMap) {
            i.forEach((x, y) -> {
                if (!i2.contains(y.get())) {
                    i2.add(y.get());
                    count.getAndIncrement();
                }
            });
        }

        System.out.println(numbersToIndexMap);
        System.out.println(i2);
        return count.get();
    }
}
