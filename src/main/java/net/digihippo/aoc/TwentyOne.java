package net.digihippo.aoc;

import java.util.HashSet;
import java.util.Set;

public class TwentyOne extends GridSolution<Long> {
    @Override
    Long partOne(Grid g) {
        final TwoDPoint replaced = g.replaceOnce('S', '.');

        int stepLimit = g.columnCount() < 20 ? 6 : 64;

        Set<TwoDPoint> possible = new HashSet<>();
        final Set<TwoDPoint> visited = new HashSet<>();

        Set<TwoDPoint> ends = new HashSet<>();

        possible.add(replaced);

        for (int stepCount = 0; stepCount < stepLimit; stepCount++) {
            final Set<TwoDPoint> newPossible = new HashSet<>();
            final int nextStepCount = stepCount + 1;
            final boolean divisor = nextStepCount % 2 == 0;

            for (TwoDPoint twoDPoint: possible) {
                visited.add(twoDPoint);
                g.visitOrthogonalNeighboursOf(twoDPoint.x(), twoDPoint.y(), (o, x, y, content) -> {
                    if (content != '#') {
                        TwoDPoint next = new TwoDPoint(x, y);
                        if (divisor && visited.contains(next)) {
                            // we're a backtrack _and_ we can repeat ourselves enough to get back here.
                            ends.add(next);
                        } else {
                            newPossible.add(next);
                        }
                    }
                });
            }

            possible = newPossible;
        }
        ends.addAll(possible);

        return (long) ends.size();
    }

    record QuadraticResult(int a, int b, int c) {
        public long computeForX(int x) {
            return a * (long) x * (long) x + ((long) b * x) + c;
        }
    }

    static QuadraticResult secondDifferences(int first, int second, int third) {
        // assumes: the second difference is fixed!
        // x, v
        // 1, first
        // 2, second
        // 3, third
        // second differences methods finds a, b, c such that ax^2 + bx + c = {first, second, third} for x in {1, 2, 3} (and onwards, obvs)

        final int diffOne = second - first;
        final int diffTwo = third - second;

        final int secondDifference = diffTwo - diffOne;

        final int a = secondDifference / 2;

        final int[] axsquared = new int[]{a, 2 * 2 * a, 3 * 3 * a};
        final int[] bxplusc = new int[]{first - axsquared[0], second - axsquared[1]};

        final int b = (bxplusc[1] - bxplusc[0]);

        // first = ax^2 + bx + c
        // => first - ax^2 - bx = c
        final int c = first - axsquared[0] - b;

        return new QuadraticResult(a, b, c);
    }

    @Override
    Long partTwo(Grid g) {
        final TwoDPoint replaced = g.replaceOnce('S', '.');

        Set<TwoDPoint> possible = new HashSet<>();
        possible.add(replaced);

        final int sillyStepCount = 26501365;
        final int thing = sillyStepCount % g.columnCount();

        final int stepLimit;
        if (sillyStepCount % g.columnCount() == 0) {
            stepLimit = g.columnCount() * 3;
        } else {
            stepLimit = thing + 2 * g.columnCount();
        }

        int[] scores = new int[3];
        int index = 0;
        for (int stepCount = 0; stepCount < stepLimit; stepCount++) {
            final Set<TwoDPoint> newPossible = new HashSet<>();
            for (TwoDPoint twoDPoint: possible) {
                g.visitOrthogonalRepeatedNeighboursOf(twoDPoint.x(), twoDPoint.y(), (o, x, y, content) -> {
                    if (content != '#') {
                        TwoDPoint next = new TwoDPoint(x, y);
                        newPossible.add(next);
                    }
                });
            }

            possible = newPossible;
            int currentStep = stepCount + 1;

            if ((currentStep - thing) % g.columnCount() == 0) {
                scores[index++] = possible.size();
            }
        }

        final int x = (sillyStepCount - thing) / g.columnCount() + 1;
        final QuadraticResult quadraticResult = secondDifferences(scores[0], scores[1], scores[2]);

        return quadraticResult.computeForX(x);
    }
}
