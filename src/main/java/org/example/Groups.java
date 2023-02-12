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
    private final Map<Integer, List<AtomicInteger>> groupNeighbors;

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
        List<AtomicInteger> overlappingGroupIndexes = new ArrayList<>();

        while (matcher.find()) {
            Long currentNumber = getLong(matcher.group());
            n.add(currentNumber);
            if (numbersToIndexMap.size() > columnPointer && numbersToIndexMap.get(columnPointer).containsKey(currentNumber) && currentNumber != null) {
                AtomicInteger currentNumberIndex = numbersToIndexMap.get(columnPointer).get(currentNumber);
                //Добавление пересечений
                overlappingGroupIndexes.add(currentNumberIndex);


            }
            columnPointer++;
        }

        AtomicInteger index;
        if (overlappingGroupIndexes.isEmpty()) {
            lastGroupIndex = new AtomicInteger(lastGroupIndex.get());
            lastGroupIndex.incrementAndGet();
            index = lastGroupIndex;
//            System.out.println("growing " + n + " index is " + index);

        } else if (overlappingGroupIndexes.size() > 1) {
            List<AtomicInteger> sss = new ArrayList<>();
            for (var s : overlappingGroupIndexes) {
                for (var z : numbersToIndexMap) {
//                    System.out.println(z);
//                    System.out.println(s);
                    if (z.containsValue(s)) {
                        for(var ass : z.values()) {
                            if (ass.get() == s.get()) {
                                sss.add(ass);
                            }
                        }
                    }
                }
            }
//            System.out.println(sss);
            overlappingGroupIndexes.addAll(sss);
//            System.out.println("adding " + n);
            rearange(overlappingGroupIndexes);
            index = overlappingGroupIndexes.stream().findFirst().get();

        } else {
            index = overlappingGroupIndexes.stream().findFirst().get();
//            System.out.println("connecting " + n + " index is " + index);
        }

        for (int i = 0; i < n.size(); i++) {
            if (numbersToIndexMap.size() <= i) {
                numbersToIndexMap.add(new HashMap<>());
            }
            numbersToIndexMap.get(i).putIfAbsent(n.get(i), index);
        }

//        System.out.println("\n");
//        System.out.println(numbersToIndexMap);
    }

    private void rearange(List<AtomicInteger> indexesToRearrange) {
//        System.out.println("start rearrange");
//        System.out.println("adding indexes " + indexesToRearrange);
        List<AtomicInteger> friends = new ArrayList<>();

        //Добавление соседей
        for (var i : indexesToRearrange) {
            if (groupNeighbors.get(i.get()) != null) {
//                System.out.println("found friends for " + i.get());
                friends.addAll(groupNeighbors.get(i.get()));
            }
        }
//        System.out.println("potential friends ");
//        System.out.println(friends);
        indexesToRearrange.addAll(friends);
//        System.out.println("became after adding friends " + indexesToRearrange);


        for (int i = 1; i < indexesToRearrange.size(); i++) {
            indexesToRearrange.get(i).set(indexesToRearrange.get(0).get());
        }
//        System.out.println(indexesToRearrange);
        groupNeighbors.put(indexesToRearrange.get(0).get(), indexesToRearrange.subList(1, indexesToRearrange.size()));
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

//        System.out.println(numbersToIndexMap);
//        System.out.println(i2);
        return count.get();
    }
}
