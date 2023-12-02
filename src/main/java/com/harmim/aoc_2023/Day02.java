package com.harmim.aoc_2023;

import com.harmim.aoc_2023.common.Triple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class Day02 implements Day {
    private final HashMap<Integer, HashSet<Triple<Integer, Integer, Integer>>> games;

    public Day02(String input) {
        games = new HashMap<>();

        for (String line : input.split("\n")) {
            line = line.replaceFirst("Game ", "");

            HashSet<Triple<Integer, Integer, Integer>> set = new HashSet<>();
            games.put(Integer.parseInt(line.substring(0, line.indexOf(':'))), set);

            line = line.replaceFirst("\\d+: ", "");

            for (String setStr : line.split("; ")) {
                int red = 0, green = 0, blue = 0;

                for (String cubes : setStr.split(", ")) {
                    int value = Integer.parseInt(cubes.substring(0, cubes.indexOf(' ')));

                    if (cubes.endsWith("red")) {
                        red = value;
                    } else if (cubes.endsWith("green")) {
                        green = value;
                    } else if (cubes.endsWith("blue")) {
                        blue = value;
                    }
                }

                set.add(new Triple<>(red, green, blue));
            }
        }
    }

    @Override
    public String part1() {
        final Triple<Integer, Integer, Integer> maxRgb = new Triple<>(12, 13, 14);
        int sum = 0;

        for (Map.Entry<Integer, HashSet<Triple<Integer, Integer, Integer>>> set : games.entrySet()) {
            sum += set.getKey();

            for (Triple<Integer, Integer, Integer> cubes : set.getValue()) {
                if (cubes.a() > maxRgb.a() || cubes.b() > maxRgb.b() || cubes.c() > maxRgb.c()) {
                    sum -= set.getKey();
                    break;
                }
            }
        }

        return Integer.toString(sum);
    }

    @Override
    public String part2() {
        int sum = 0;

        for (Map.Entry<Integer, HashSet<Triple<Integer, Integer, Integer>>> set : games.entrySet()) {
            int maxRed = 0, maxGreen = 0, maxBlue = 0;

            for (Triple<Integer, Integer, Integer> cubes : set.getValue()) {
                if (cubes.a() > maxRed) {
                    maxRed = cubes.a();
                }
                if (cubes.b() > maxGreen) {
                    maxGreen = cubes.b();
                }
                if (cubes.c() > maxBlue) {
                    maxBlue = cubes.c();
                }
            }

            sum += maxRed * maxGreen * maxBlue;
        }

        return Integer.toString(sum);
    }
}
