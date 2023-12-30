package com.harmim.aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Day07 implements Day {
    private interface Rule {
        boolean check(Map<Character, Integer> symbols);
    }

    private static final Map<Character, Integer> CARDS_ORDERING = new HashMap<>() {{
        put('2', 0);
        put('3', 1);
        put('4', 2);
        put('5', 3);
        put('6', 4);
        put('7', 5);
        put('8', 6);
        put('9', 7);
        put('T', 8);
        put('J', 9);
        put('Q', 10);
        put('K', 11);
        put('A', 12);
    }};

    private record Hand(Character[] cards, Map<Character, Integer> symbols, int bid) {
    }

    private final List<Hand> hands;

    private boolean jokers = false;

    public Day07(String input) {
        hands = new ArrayList<>();

        for (String line : input.split("\n")) {
            String[] parts = line.split(" ");

            Map<Character, Integer> symbols = new HashMap<>(CARDS_ORDERING);
            symbols.replaceAll((symbol, c) -> (int) parts[0].chars().filter(s -> s == symbol).count());

            hands.add(new Hand(
                    parts[0].chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                    symbols,
                    Integer.parseInt(parts[1])
            ));
        }
    }

    @Override
    public String part1() {
        sortHands();

        return Long.toString(calculateWinning());
    }

    @Override
    public String part2() {
        jokers = true;
        sortHands();
        jokers = false;

        return Long.toString(calculateWinning());
    }

    private boolean isFiveKind(Map<Character, Integer> symbols) {
        return symbols.containsValue(5) || (
                jokers && (
                        symbols.get('J') == 4
                                || (symbols.containsValue(2) && symbols.get('J') == 3)
                                || (symbols.containsValue(3) && symbols.get('J') == 2)
                                || (symbols.containsValue(4) && symbols.get('J') == 1)
                )
        );
    }

    private boolean isFourKind(Map<Character, Integer> symbols) {
        return symbols.containsValue(4) || (
                jokers && (
                        symbols.get('J') == 3
                                || (isTwoPairs(symbols) && symbols.get('J') == 2)
                                || (symbols.containsValue(3) && symbols.get('J') == 1)
                )
        );
    }

    private boolean isFullHouse(Map<Character, Integer> symbols) {
        return (symbols.containsValue(3) && symbols.containsValue(2))
                || (jokers && (symbols.containsValue(3) || isTwoPairs(symbols)) && symbols.get('J') == 1);
    }

    private boolean isThreeKind(Map<Character, Integer> symbols) {
        return symbols.containsValue(3)
                || (jokers && (symbols.get('J') == 2 || (symbols.containsValue(2) && symbols.get('J') == 1)));
    }

    private static boolean isTwoPairs(Map<Character, Integer> symbols) {
        return symbols.values().stream().filter(c -> c == 2).count() == 2;
    }

    private boolean isOnePair(Map<Character, Integer> symbols) {
        return symbols.containsValue(2) || (jokers && symbols.get('J') == 1);
    }

    private int compareCards(Character a, Character b) {
        if (a.equals(b)) {
            return 0;
        }

        Map<Character, Integer> cardsOrdering = new HashMap<>(CARDS_ORDERING);
        if (jokers) {
            cardsOrdering.put('J', -1);
        }

        return cardsOrdering.get(a).compareTo(cardsOrdering.get(b));
    }

    private int compareHands(Character[] a, Character[] b) {
        for (int i = 0; i < a.length; i++) {
            int cmp = compareCards(a[i], b[i]);
            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    private void sortHands() {
        hands.sort((a, b) -> {
            if (a.symbols.equals(b.symbols)) {
                return compareHands(a.cards, b.cards);
            }

            Rule[] rules = new Rule[]{
                    this::isFiveKind,
                    this::isFourKind,
                    this::isFullHouse,
                    this::isThreeKind,
                    Day07::isTwoPairs,
                    this::isOnePair,
            };
            for (Rule rule : rules) {
                if (rule.check(a.symbols) && !rule.check(b.symbols)) {
                    return 1;
                }
                if (!rule.check(a.symbols) && rule.check(b.symbols)) {
                    return -1;
                }
                if (rule.check(a.symbols) && rule.check(b.symbols)) {
                    return compareHands(a.cards, b.cards);
                }
            }

            return compareHands(a.cards, b.cards);
        });
    }

    private long calculateWinning() {
        long winning = 0;
        for (int i = 0; i < hands.size(); i++) {
            winning += (long) hands.get(i).bid * (i + 1);
        }

        return winning;
    }
}
