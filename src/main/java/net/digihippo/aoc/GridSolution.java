package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;

public abstract class GridSolution<T> extends SolutionTemplate<T, T> {
    abstract T partOne(Grid g);
    abstract T partTwo(Grid g);

    @Override
    Solution<T> partOne() {
        return new Solution<>() {
            private final List<String> rows = new ArrayList<>();

            @Override
            public T result() {
                return partOne(new Grid(rows));
            }

            @Override
            public void accept(String s) {
                rows.add(s);
            }
        };
    }

    @Override
    Solution<T> partTwo() {
        return new Solution<>() {
            private final List<String> rows = new ArrayList<>();

            @Override
            public T result() {
                return partTwo(new Grid(rows));
            }

            @Override
            public void accept(String s) {
                rows.add(s);
            }
        };
    }
}
