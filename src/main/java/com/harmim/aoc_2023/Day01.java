package com.harmim.aoc_2023;

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

            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (isDigit(c)) {
                    number.append(c);
                    break;
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                char c = line.charAt(i);
                if (isDigit(c)) {
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
            for (int i = 0; i < line.length(); i++) {
                numbersParts.append(line.charAt(i));
                numbersParts = new StringBuilder(replaceWordDigits(numbersParts.toString()));

                char c = numbersParts.charAt(numbersParts.length() - 1);
                if (isDigit(c)) {
                    number.append(c);
                    break;
                }
            }

            numbersParts = new StringBuilder();
            for (int i = line.length() - 1; i >= 0; i--) {
                numbersParts.insert(0, line.charAt(i));
                numbersParts = new StringBuilder(replaceWordDigits(numbersParts.toString()));

                char c = numbersParts.charAt(0);
                if (isDigit(c)) {
                    number.append(c);
                    break;
                }
            }

            result += Integer.parseInt(number.isEmpty() ? "0" : number.toString());
        }

        return Integer.toString(result);
    }

    private boolean isDigit(char c) {
        return c >= '1' && c <= '9';
    }

    private String replaceWordDigits(String s) {
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
