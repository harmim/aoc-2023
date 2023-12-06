package com.harmim.aoc2023;

class Day01 implements Day {
    private final String[] lines;

    public Day01(String input) {
        lines = input.split("\n");
    }

    @Override
    public String part1() {
        int result = 0;

        for (String line : lines) {
            StringBuilder number = new StringBuilder();

            for (Character c : line.toCharArray()) {
                if (Character.isDigit(c)) {
                    number.append(c);
                    break;
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    number.append(c);
                    break;
                }
            }

            result += Integer.parseInt(number.isEmpty() ? "0" : number.toString());
        }

        return Integer.toString(result);
    }

    @Override
    public String part2() {
        int result = 0;

        for (String line : lines) {
            StringBuilder number = new StringBuilder();

            StringBuilder numbersParts = new StringBuilder();
            for (Character c : line.toCharArray()) {
                numbersParts.append(c);
                numbersParts = new StringBuilder(replaceWordDigits(numbersParts.toString()));

                char ch = numbersParts.charAt(numbersParts.length() - 1);
                if (Character.isDigit(ch)) {
                    number.append(ch);
                    break;
                }
            }

            numbersParts.setLength(0);
            for (int i = line.length() - 1; i >= 0; i--) {
                numbersParts.insert(0, line.charAt(i));
                numbersParts = new StringBuilder(replaceWordDigits(numbersParts.toString()));

                char ch = numbersParts.charAt(0);
                if (Character.isDigit(ch)) {
                    number.append(ch);
                    break;
                }
            }

            result += Integer.parseInt(number.isEmpty() ? "0" : number.toString());
        }

        return Integer.toString(result);
    }

    private static String replaceWordDigits(String s) {
        return s.replaceAll("one", "1")
                .replaceAll("two", "2")
                .replaceAll("three", "3")
                .replaceAll("four", "4")
                .replaceAll("five", "5")
                .replaceAll("six", "6")
                .replaceAll("seven", "7")
                .replaceAll("eight", "8")
                .replaceAll("nine", "9");
    }
}
