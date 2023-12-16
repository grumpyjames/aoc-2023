package net.digihippo.aoc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Fourteen extends GridSolution<Integer> {

    private static final char ROUND_ROCK = 'O';
    private static final char EMPTY = '.';

    @Override
    Integer partOne(Grid g) {
        northCycle(g);

        Scorer score = new Scorer(g.rowCount());
        g.visit(score);

        return score.score;
    }

    private static void northCycle(Grid g) {
        g.visit((x, y, content) -> {
            if (content == ROUND_ROCK) {
                // roll it north as far as we can.
                int probeY = y - 1;
                while (g.contains(x, probeY) && g.at(x, probeY) == EMPTY) {
                    g.set(x, probeY, ROUND_ROCK);
                    g.set(x, probeY + 1, EMPTY);
                    probeY--;
                }
            }
        });
    }

    private static void eastCycle(Grid g) {
        // rolls roll right. Visit right to left.
        for (int x = g.columnCount() - 1; x >= 0; x--) {
            for (int y = 0; y < g.rowCount(); y++) {
                char content = g.at(x, y);
                if (content == ROUND_ROCK) {
                    // roll it east as far as we can.
                    int probeX = x + 1;
                    while (g.contains(probeX, y) && g.at(probeX, y) == EMPTY) {
                        g.set(probeX, y, ROUND_ROCK);
                        g.set(probeX - 1, y, EMPTY);
                        probeX++;
                    }
                }

            }
        }
    }

    private static void westCycle(Grid g) {
        // rolls roll left. Visit left to right
        // => can just use visit
        g.visit((x, y, content) -> {
            if (content == ROUND_ROCK) {
                // roll it west as far as we can.
                int probeX = x - 1;
                while (g.contains(probeX, y) && g.at(probeX, y) == EMPTY) {
                    g.set(probeX, y, ROUND_ROCK);
                    g.set(probeX + 1, y, EMPTY);
                    probeX--;
                }
            }
        });
    }

    private static void southCycle(Grid g) {
        // rolls roll down, Visit bottom to top
        for (int y = g.columnCount() - 1; y >= 0; y--) {
            for (int x = 0; x < g.rowCount(); x++) {
                char content = g.at(x, y);
                if (content == ROUND_ROCK) {
                    int probeY = y + 1;
                    while (g.contains(x, probeY) && g.at(x, probeY) == EMPTY) {
                        g.set(x, probeY, ROUND_ROCK);
                        g.set(x, probeY - 1, EMPTY);
                        probeY++;
                    }
                }
            }
        }
    }

    private static final class Buffer
    {
        private final int[] observations = new int[16];
        private int index = 0;

        void observe(int observation) {
            observations[index] = observation;
            index += 1;
            index = index % 16;
        }

        public Integer pattern() {
            final java.util.Set<Integer> differences = new HashSet<>();
            for (int i = 0; i < 15; i++) {
                int current = observations[((index - (i + 1)) + 16) % 16];
                int previous = observations[((index - (i + 2)) + 16) % 16];
                differences.add(current - previous);
            }
            return differences.size() == 1 ? differences.iterator().next() : null;
        }
    }

    @Override
    Integer partTwo(Grid g) {
        final Map<Integer, Buffer> seen = new HashMap<>();

        int cycleCount = 1;
        while (true) {
            cycle(g);
            int score = computeScore(g);
            seen.computeIfAbsent(score, integer -> new Buffer()).observe(cycleCount);
            Buffer buffer = seen.get(score);

            Integer pattern = buffer.pattern();
            if (pattern != null) {
                int bigNumber = 1_000_000_000;
                int cyclesRequired = (bigNumber - cycleCount) % pattern;
                for (int i = 0; i < cyclesRequired; i++) {
                    cycle(g);
                }

                return computeScore(g);
            }

            cycleCount++;
        }
    }

    private static int computeScore(Grid g) {
        Scorer scorer = new Scorer(g.rowCount());
        g.visit(scorer);
        return scorer.score;
    }

    private void cycle(Grid g) {
        northCycle(g);
        westCycle(g);
        southCycle(g);
        eastCycle(g);

    }

    private static class Scorer implements Grid.Visitor {
        int score = 0;
        private final int height;

        private Scorer(int height) {
            this.height = height;
        }

        @Override
        public void onCell(int x, int y, char content) {
            if (content == ROUND_ROCK) {
                score += (height - y);
            }
        }
    }
}
