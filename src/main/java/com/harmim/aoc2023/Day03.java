package com.harmim.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class Day03 implements Day {
    private record Item(Type type, Character item) {
        private enum Type {
            DIGIT,
            SYMBOL,
            BLANK,
        }
    }

    private record Index(int x, int y) {
    }

    private final Item[][] schema;

    public Day03(String input) {
        String[] lines = input.split("\n");
        if (lines.length == 0) {
            schema = new Item[0][0];
            return;
        }

        schema = new Item[lines.length][lines[0].length()];

        for (int x = 0; x < lines.length; x++) {
            for (int y = 0; y < lines[0].length(); y++) {
                char c = lines[x].charAt(y);

                Item.Type type = Item.Type.SYMBOL;
                if (Character.isDigit(c)) {
                    type = Item.Type.DIGIT;
                } else if (c == '.') {
                    type = Item.Type.BLANK;
                }
                schema[x][y] = new Item(type, c);
            }
        }
    }

    @Override
    public String part1() {
        int sum = 0;

        for (int x = 0; x < schema.length; x++) {
            if (schema[x].length == 0) {
                continue;
            }

            boolean numberStart = false, adjacentToSymbol = false;
            StringBuilder number = new StringBuilder();

            for (int y = 0; y < schema[x].length; y++) {
                Item item = schema[x][y];

                if (item.type == Item.Type.DIGIT) {
                    numberStart = true;
                    number.append(item.item);
                    adjacentToSymbol = adjacentToSymbol || !getNeighbors(new Index(x, y), Item.Type.SYMBOL).isEmpty();
                }

                if (numberStart && (y + 1 == schema[x].length || item.type != Item.Type.DIGIT)) {
                    sum += adjacentToSymbol ? Integer.parseInt(number.toString()) : 0;
                    numberStart = false;
                    adjacentToSymbol = false;
                    number.setLength(0);
                }
            }
        }

        return Integer.toString(sum);
    }

    @Override
    public String part2() {
        final char gear = '*';
        final int gearNeighbors = 2;
        int sum = 0;

        for (int x = 0; x < schema.length; x++) {
            if (schema[x].length == 0) {
                continue;
            }

            for (int y = 0; y < schema[x].length; y++) {
                if (schema[x][y].item != gear) {
                    continue;
                }

                List<Index> neighborDigits = getNeighbors(new Index(x, y), Item.Type.DIGIT);
                if (neighborDigits.size() < gearNeighbors) {
                    continue;
                }

                List<Index> covered = new ArrayList<>();
                AtomicInteger neighborsCount = new AtomicInteger();
                int product = neighborDigits.stream().map(neighbor -> {
                    if (covered.contains(neighbor)) {
                        return 1;
                    }

                    neighborsCount.getAndIncrement();
                    if (neighborsCount.get() > gearNeighbors) {
                        return 1;
                    }

                    StringBuilder number = new StringBuilder(schema[neighbor.x][neighbor.y].item.toString());
                    covered.add(neighbor);

                    for (int yy = neighbor.y - 1; yy >= 0; yy--) {
                        Item item = schema[neighbor.x][yy];
                        if (item.type != Item.Type.DIGIT) {
                            break;
                        }
                        number.insert(0, item.item);
                        covered.add(new Index(neighbor.x, yy));
                    }

                    for (int yy = neighbor.y + 1; yy < schema[neighbor.x].length; yy++) {
                        Item item = schema[neighbor.x][yy];
                        if (item.type != Item.Type.DIGIT) {
                            break;
                        }
                        number.append(item.item);
                        covered.add(new Index(neighbor.x, yy));
                    }

                    return Integer.parseInt(number.toString());
                }).reduce(1, Math::multiplyExact);

                sum += neighborsCount.get() == gearNeighbors ? product : 0;
            }
        }

        return Integer.toString(sum);
    }

    private List<Index> getNeighbors(Index index, Item.Type type) {
        final Index[] neighborIndices = new Index[]{
                new Index(index.x - 1, index.y), // left
                new Index(index.x + 1, index.y), // right
                new Index(index.x, index.y - 1), // up
                new Index(index.x, index.y + 1), // down
                new Index(index.x - 1, index.y - 1), // left-up
                new Index(index.x - 1, index.y + 1), // left-down
                new Index(index.x + 1, index.y - 1), // right-up
                new Index(index.x + 1, index.y + 1) // right-down
        };

        return Arrays.stream(neighborIndices).filter(neighborIndex ->
                neighborIndex.x >= 0
                        && neighborIndex.y >= 0
                        && neighborIndex.x < schema.length
                        && neighborIndex.y < schema[0].length
                        && schema[neighborIndex.x][neighborIndex.y].type == type
        ).toList();
    }
}
