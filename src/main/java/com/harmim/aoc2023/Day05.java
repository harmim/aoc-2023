package com.harmim.aoc2023;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class Day05 implements Day {
    private record Range(long low, long high) {
        public boolean contains(long number) {
            return number >= low && number <= high;
        }

        public long mapToAnotherRange(long number, Range range) {
            return number - low + range.low;
        }
    }

    private record Mapping(Range source, Range destination) {
    }

    private final List<Long> seeds;

    private final List<Set<Mapping>> mappings;

    public Day05(String input) {
        List<Long> _seeds = new ArrayList<>();
        mappings = new ArrayList<>();

        for (String group : input.split("\n\n")) {
            boolean isMapping = false;
            Set<Mapping> mapping = new HashSet<>();

            for (String line : group.split("\n")) {
                if (line.startsWith("seeds:")) {
                    line = line.replaceFirst("seeds: ", "");
                    _seeds = Arrays.stream(line.split(" "))
                            .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                } else if (!line.endsWith("map:")) {
                    isMapping = true;
                    long[] values = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
                    mapping.add(new Mapping(
                            new Range(values[1], values[1] + values[2] - 1),
                            new Range(values[0], values[0] + values[2] - 1)
                    ));
                }
            }

            if (isMapping) {
                mappings.add(mapping);
            }
        }

        seeds = _seeds;
    }

    @Override
    public String part1() {
        return Long.toString(seeds.stream().mapToLong(this::mapSeed).min().orElseThrow());
    }

    @Override
    public String part2() {
        Set<Range> seedRanges = new HashSet<>();
        for (int i = 1; i < seeds.size(); i += 2) {
            seedRanges.add(new Range(seeds.get(i - 1), seeds.get(i - 1) + seeds.get(i) - 1));
        }

        return Long.toString(
                seedRanges.parallelStream().mapToLong(range ->
                        LongStream.rangeClosed(range.low, range.high).boxed().parallel()
                                .mapToLong(this::mapSeed).min().orElseThrow()
                ).min().orElseThrow()
        );
    }

    private long mapSeed(long seed) {
        long value = seed;

        for (Set<Mapping> mapping : mappings) {
            long v = value;
            value = mapping.stream().filter(map -> map.source.contains(v))
                    .findAny().map(map -> map.source.mapToAnotherRange(v, map.destination)).orElse(value);
        }

        return value;
    }
}
