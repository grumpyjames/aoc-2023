package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Thirteen extends SolutionTemplate<Integer, Integer> {
    static int score(Grid g, Function<Grid, Integer> boundaries, EqualityChecker ec, Function<Integer, Integer> scorer) {
        int width = boundaries.apply(g);
        int bound = width - 1;
        int score = 0;
        for (int i = 0; i < bound; i++) {
            boolean reflection = true;
            int smallerSide = Math.min(i, bound - (i + 1));
            for (int j = 0; j <= smallerSide; j++) {
                if (!ec.equal(g, i - j, i + j + 1)) {
                    reflection = false;
                    break;
                }
            }
            if (reflection) {
                score += scorer.apply(i + 1);
            } else {
                score += 0;
            }
        }

        return score;
    }


    static int score(Grid grid) {
        int columnScore = score(grid, Grid::columnCount, Grid::equalColumns, Function.identity());
        int rowScore = score(grid, Grid::rowCount, Grid::equalRows, s -> s * 100);

        return rowScore + columnScore;
    }


    @Override
    Solution<Integer> partOne() {
        return new Solution<Integer>() {
            private final List<String> builder = new ArrayList<>();
            final List<Grid> g = new ArrayList<>();

            @Override
            public Integer result() {
                int summary = 0;

                for (Grid grid : g) {
                    int sum = score(grid);

                    if (sum == 0) {
                        System.out.println("Found no reflections in: ");
                        System.out.println();
                        grid.printTo(System.out);
                        System.out.println();
                    }
                    summary += sum;
                }

                return summary;
            }

            @Override
            public void accept(String s) {
                if (s.isBlank()) {
                    g.add(new Grid(new ArrayList<>(builder)));
                    builder.clear();
                } else {
                    builder.add(s);
                }
            }

            @Override
            public void processingComplete() {
                g.add(new Grid(new ArrayList<>(builder)));
            }
        };
    }

    @Override
    Solution<Integer> partTwo() {
        return null;
    }

    public interface EqualityChecker {
        boolean equal(Grid g, int i, int j);
    }
}
