package com.harmim.aoc2023;

import java.util.*;

class Day04 implements Day {
    private record Card(int number, int wonNumbersCount) {
    }

    private final Map<Integer, Card> cards;

    public Day04(String input) {
        cards = new HashMap<>();

        for (String line : input.split("\n")) {
            line = line.replaceFirst("Card +", "");
            int number = Integer.parseInt(line.substring(0, line.indexOf(':')));
            line = line.replaceFirst("\\d+: +", "");
            String[] parts = line.split(" +\\| +");

            Set<Integer> wonNumbers =
                    new HashSet<>(Arrays.stream(parts[0].split(" +")).map(Integer::parseInt).toList());
            wonNumbers.retainAll(
                    new HashSet<>(Arrays.stream(parts[1].split(" +")).map(Integer::parseInt).toList())
            );
            cards.put(number, new Card(number, wonNumbers.size()));
        }
    }

    @Override
    public String part1() {
        return Integer.toString(cards.values().stream().mapToInt(card ->
                card.wonNumbersCount != 0 ? (int) Math.pow(2, card.wonNumbersCount - 1) : 0
        ).sum());
    }

    @Override
    public String part2() {
        int cardsCount = 0;

        List<Card> oldCards = new ArrayList<>(cards.values()), newCards;
        do {
            newCards = new ArrayList<>();

            for (Card card : oldCards) {
                cardsCount++;

                for (int i = 1; i <= card.wonNumbersCount; i++) {
                    newCards.add(cards.get(card.number + i));
                }
            }

            oldCards = newCards;
        } while (!newCards.isEmpty());

        return Integer.toString(cardsCount);
    }
}
