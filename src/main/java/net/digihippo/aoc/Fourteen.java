package net.digihippo.aoc;

public class Fourteen extends GridSolution<Integer> {

    private static final char ROUND_ROCK = 'O';
    private static final char EMPTY = '.';

    @Override
    Integer partOne(Grid g) {
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

        Scorer score = new Scorer(g.rowCount());
        g.visit(score);

        return score.score;
    }

    @Override
    Integer partTwo(Grid g) {
        return 5;
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
