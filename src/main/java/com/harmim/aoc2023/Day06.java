package com.harmim.aoc2023;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.LongStream;

class Day06 implements Day {
    private record Race(long time, long distance) {
    }

    private final Set<Race> races;

    private final Race singleRace;

    public Day06(String input) {
        String[] lines = input.split("\n");

        lines[0] = lines[0].replaceFirst("Time: +", "");
        long[] times = Arrays.stream(lines[0].split(" +")).mapToLong(Long::parseLong).toArray();

        lines[1] = lines[1].replaceFirst("Distance: +", "");
        long[] distances = Arrays.stream(lines[1].split(" +")).mapToLong(Long::parseLong).toArray();

        races = new HashSet<>();
        for (int i = 0; i < times.length; i++) {
            races.add(new Race(times[i], distances[i]));
        }

        singleRace = new Race(
                Long.parseLong(lines[0].replaceAll(" ", "")),
                Long.parseLong(lines[1].replaceAll(" ", ""))
        );
    }

    @Override
    public String part1() {
        return Long.toString(races.stream().mapToLong(Day06::getWinsCount).reduce(1, Math::multiplyExact));
    }

    @Override
    public String part2() {
        return Long.toString(getWinsCount(singleRace));
    }

    private static long getWinsCount(Race race) {
        return LongStream.range(1, race.time).filter(time -> time * (race.time - time) > race.distance).count();
    }
}
