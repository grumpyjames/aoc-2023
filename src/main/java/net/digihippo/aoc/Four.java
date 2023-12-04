package net.digihippo.aoc;

import java.util.*;

public class Four extends CommonParserSolutionTemplate<Integer, Four.Card> {
    record Card(int id, Set<Integer> winning, Set<Integer> mine) {}

    int index = 1;

    @Override
    Four.Card parse(String line) {
        String[] split = line.split(":")[1].split("\\|");

        Set<Integer> winning = parseInts(split[0]);
        Set<Integer> mine = parseInts(split[1]);

        return new Card(index++, winning, mine);
    }

    private Set<Integer> parseInts(String winning) {
        String[] ws = winning.split(" ");
        Set<Integer> wins = new HashSet<>();
        for (String w : ws) {
            if (!w.isBlank())
                wins.add(Integer.parseInt(w));
        }

        return wins;
    }

    @Override
    Integer partOne(List<Four.Card> parsed) {
        int result = 0;

        for (Card card : parsed) {
            int size = score(card);
            result += (1 << (size - 1));
        }

        return result;
    }

    private int score(Card card) {
        Set<Integer> winning = card.winning;
        Set<Integer> mine = card.mine;

        Set<Integer> overlap = new HashSet<>();
        for (Integer integer : mine) {
            if (winning.contains(integer)) {
                overlap.add(integer);
            }
        }

        return overlap.size();
    }

    @Override
    Integer partTwo(List<Four.Card> parsed) {
        final Map<Integer, Integer> result = new HashMap<>();

        for (Card card : parsed) {
            result.put(card.id, 1);
        }

        for (int i = 0; i < parsed.size(); i++) {
            Card card = parsed.get(i);
            int score = score(card);

            Integer integer = result.get(card.id);
            int copies = integer == null ? 1 : integer;
            for (int j = i + 1; j < Math.min(i + 1 + score, parsed.size()); j++) {
                int index = j + 1;
                Integer before = result.get(index);
                result.put(index, before + copies);
            }
        }

        return result.values().stream().mapToInt(Integer::intValue).sum();
    }
}
