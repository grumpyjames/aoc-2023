package net.digihippo.aoc;

import java.util.*;
import java.util.function.Function;

public class Seven extends CommonParserSolutionTemplate<Integer, Seven.Hand> {
    @Override
    Hand parse(String line) {
        String[] parts = line.split(" ");

        final Map<Character, Integer> counts = new HashMap<>();
        final char[] arr = parts[0].toCharArray();
        int jokers = 0;
        for (char card : arr) {
            counts.compute(
                    card,
                    (character, integer) -> {
                        if (integer == null) {
                            return 1;
                        }
                        return integer + 1;
                    }
            );
            if (card == 'J') {
                jokers++;
            }
        }

        Map<Character, Integer> jokered = new HashMap<>();
        char best = 'x';
        int max = 0;
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            if (entry.getKey() != 'J') {
                jokered.put(entry.getKey(), entry.getValue());
                if (entry.getValue() > max) {
                    best = entry.getKey();
                    max = entry.getValue();
                }
            }
        }
        Integer integer = jokered.get(best);
        if (integer == null) { // all jokers!
            jokered = counts;
        } else {
            jokered.put(best, integer + jokers);
        }

        return new Hand(counts, parts[0], jokered, Integer.parseInt(parts[1]));
    }

    @Override
    Integer partOne(List<Hand> parsed) {
        parsed.sort(new HandComparator(11, Hand::rank));
        Collections.reverse(parsed);

        int result = 0;
        for (int i = 0; i < parsed.size(); i++) {
            Hand h = parsed.get(i);
            result += h.bid * (i + 1);
        }

        return result;
    }

    @Override
    Integer partTwo(List<Hand> parsed) {
        parsed.sort(new HandComparator(0, Hand::jokerRank));
        Collections.reverse(parsed);

        int result = 0;
        for (int i = 0; i < parsed.size(); i++) {
            Hand h = parsed.get(i);
            result += h.bid * (i + 1);
        }

        return result;
    }

    private static int score(char c, int jokerScore) {
        if (Converters.isNumeric(c)) {
            return Converters.fromAsciiChar(c);
        }

        switch (c) {
            case 'A' -> {
                return 14;
            }
            case 'K' -> {
                return 13;
            }
            case 'Q' -> {
                return 12;
            }
            case 'J' -> {
                return jokerScore;
            }
            case 'T' -> {
                return 10;
            }
        }

        throw new UnsupportedOperationException("Unsupported card: " + c);
    }

    private static final class HandComparator implements Comparator<Hand>
    {
        private final int jokerScore;
        private final Function<Hand, Integer> ranker;

        public HandComparator(int jokerScore, Function<Hand, Integer> ranker) {
            this.jokerScore = jokerScore;
            this.ranker = ranker;
        }

        @Override
        public int compare(Hand o1, Hand o2) {
            int o2Rank = ranker.apply(o2);
            int o1Rank = ranker.apply(o1);
            if (o2Rank > o1Rank) {
                return 1;
            } else if (o1Rank > o2Rank) {
                return -1;
            }

            for (int i = 0; i < 5; i++) {
                int diff = score(o2.line.charAt(i), jokerScore) - score(o1.line.charAt(i), jokerScore);

                if (diff != 0) {
                    return Integer.signum(diff);
                }
            }

            return 0;
        }
    }

    record Hand(
            Map<Character, Integer> counts,
            String line,
            Map<Character, Integer> jokered,
            int bid) {

        public int rank() {
            int size = counts.size();
            if (size == 1) {
                return 10; // five of a kind
            } else if (size == 2 && counts.containsValue(1)) {
                return 9; // four of a kind
            } else if (size == 2) {
                return 8; // full house
            } else if (size == 3 && counts.containsValue(3)) {
                return 7; // three of a kind
            } else if (size == 3) {
                return 6; // two pair
            } else if (size == 4) {
                return 5; // pair
            }

            return 4; // high card
        }

        public int jokerRank() {
            int size = jokered.size();
            if (size == 1) {
                return 10; // five of a kind
            } else if (size == 2 && jokered.containsValue(1)) {
                return 9; // four of a kind
            } else if (size == 2) {
                return 8; // full house
            } else if (size == 3 && jokered.containsValue(3)) {
                return 7; // three of a kind
            } else if (size == 3) {
                return 6; // two pair
            } else if (size == 4) {
                return 5; // pair
            }

            return 4; // high card
        }
    }
}
