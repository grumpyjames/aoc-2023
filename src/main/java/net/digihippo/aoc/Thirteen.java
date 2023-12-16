package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Thirteen extends SolutionTemplate<Integer, Integer> {
    enum Type {
        Row,
        Column
    }

    record Reflection(Type t, int location) {
        public int score() {
            if (t == Type.Column) {
                return location;
            } else {
                return location * 100;
            }
        }
    }

    static List<Reflection> score(Grid g, Function<Grid, Integer> boundaries, EqualityChecker ec, Type type) {
        final List<Reflection> result = new ArrayList<>();
        int width = boundaries.apply(g);
        int bound = width - 1;
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
                result.add(new Reflection(type, i + 1));
            }
        }

        return result;
    }


    static int score(Grid grid) {
        final List<Reflection> reflections = computeReflections(grid);

        return reflections.stream().mapToInt(Reflection::score).sum();
    }

    private static List<Reflection> computeReflections(Grid grid) {
        final List<Reflection> reflections = new ArrayList<>();

        reflections.addAll(score(grid, Grid::columnCount, Grid::equalColumns, Type.Column));
        reflections.addAll(score(grid, Grid::rowCount, Grid::equalRows, Type.Row));
        return reflections;
    }


    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
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
        return new Solution<>() {
            @Override
            public Integer result() {
                int summary = 0;
                for (Grid grid : grids) {
                    summary += smudgeScore(grid);
                }

                return summary;
            }

            private final List<String> builder = new ArrayList<>();
            final List<Grid> grids = new ArrayList<>();

            @Override
            public void accept(String s) {
                if (s.isBlank()) {
                    grids.add(new Grid(new ArrayList<>(builder)));
                    builder.clear();
                } else {
                    builder.add(s);
                }
            }

            @Override
            public void processingComplete() {
                grids.add(new Grid(new ArrayList<>(builder)));
            }
        };
    }

    static int smudgeScore(Grid grid) {
        Set<Reflection> originalScore = new HashSet<>(computeReflections(grid));

        for (int x = 0; x < grid.columnCount(); x++) {
            for (int y = 0; y < grid.rowCount(); y++) {
                try {
                    grid.swap(x, y, '.', '#');
                    Set<Reflection> newScore = new HashSet<>(computeReflections(grid));
                    if (!newScore.equals(originalScore) && !newScore.isEmpty()) {
                        for (Reflection reflection : originalScore) {
                            newScore.remove(reflection);
                        }

                        return newScore.stream().mapToInt(Reflection::score).sum();
                    }
                } finally {
                    grid.swap(x, y, '.', '#');
                }
            }
        }

        throw new IllegalStateException("No smudge found");
    }

    public interface EqualityChecker {
        boolean equal(Grid g, int i, int j);
    }
}
