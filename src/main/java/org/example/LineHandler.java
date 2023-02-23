package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class LineHandler {
    private final List<Map<Long, Group>> numbersToGroup;
    private final Map<Group, List<String>> groups;
    private int lastIndex;

    public LineHandler() {
        numbersToGroup = new ArrayList<>();
        numbersToGroup.add(new HashMap<>());
        groups = new HashMap<>();
        lastIndex = 0;
    }

    public void add(String line) {
        Long[] lineArr = Validator.validateAndGetLongArray(line);
        if (lineArr.length != 0) {
            parseLine(lineArr, line);
        }
    }

    public void parseLine(Long[] lineArray, String line) {
        int columnPointer = 0;
        List<Long> tempList = new ArrayList<>();
        Group group = null;

        for (var number : lineArray) {
            tempList.add(number);
            if (numbersToGroup.size() > columnPointer &&
                    number != null &&
                    numbersToGroup.get(columnPointer).containsKey(number)) {

                Group firstEnterGroup = numbersToGroup.get(columnPointer).get(number);
                if (group != null) {
                    Group.merge(firstEnterGroup, group);
                } else {
                    group = firstEnterGroup;
                }
            }
            columnPointer++;
        }

        group = group == null ? new Group(++lastIndex) : group;
        for (int i = 0; i < tempList.size(); i++) {
            if (numbersToGroup.size() <= i) {
                numbersToGroup.add(new HashMap<>());
            }
            numbersToGroup.get(i).putIfAbsent(tempList.get(i), group);
        }
        groups.putIfAbsent(group, new ArrayList<>());
        groups.get(group).add(line);
    }

    public long getCountAndWriteToFile() {
        Map<Integer, Set<String>> preResult = groups
                .entrySet()
                .stream()
                .flatMap(e -> e
                        .getValue()
                        .stream()
                        .map(v -> new AbstractMap.SimpleEntry<>(e
                                .getKey()
                                .getHead(), v)))

                .collect(Collectors.groupingBy(
                        AbstractMap.SimpleEntry::getKey,
                        Collectors.mapping(AbstractMap.SimpleEntry::getValue, Collectors.toSet())
                ));

        List<Map.Entry<Integer, Set<String>>> entries = new ArrayList<>(preResult.entrySet());
        entries.sort(Comparator
                .comparing(entry -> -entry
                        .getValue()
                        .size()));

        long resultSize = preResult
                .entrySet()
                .stream()
                .filter(x -> x.getValue()
                        .stream()
                        .filter(Objects::nonNull)
                        .count() > 1)
                .count();

        System.out.println("Groups more than 1 - " + resultSize);
        Main.writeResultToFile(entries, resultSize);
        return resultSize;
    }
}
