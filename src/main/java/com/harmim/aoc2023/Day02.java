package com.harmim.aoc2023;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Day02 implements Day {
    private record Rgb(int r, int g, int b) {
    }

    private final Map<Integer, Set<Rgb>> games;

    public Day02(String input) {
        games = new HashMap<>();

        for (String line : input.split("\n")) {
            line = line.replaceFirst("Game ", "");

            Set<Rgb> set = new HashSet<>();
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

                set.add(new Rgb(red, green, blue));
            }
        }
    }

    @Override
    public String part1() {
        final Rgb maxRgb = new Rgb(12, 13, 14);
        int sum = 0;

        for (Map.Entry<Integer, Set<Rgb>> set : games.entrySet()) {
            sum += set.getKey();

            for (Rgb cubes : set.getValue()) {
                if (cubes.r > maxRgb.r || cubes.g > maxRgb.g || cubes.b > maxRgb.b) {
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

        for (Map.Entry<Integer, Set<Rgb>> set : games.entrySet()) {
            int maxRed = 0, maxGreen = 0, maxBlue = 0;

            for (Rgb cubes : set.getValue()) {
                if (cubes.r > maxRed) {
                    maxRed = cubes.r;
                }
                if (cubes.g > maxGreen) {
                    maxGreen = cubes.g;
                }
                if (cubes.b > maxBlue) {
                    maxBlue = cubes.b;
                }
            }

            sum += maxRed * maxGreen * maxBlue;
        }

        return Integer.toString(sum);
    }
}
