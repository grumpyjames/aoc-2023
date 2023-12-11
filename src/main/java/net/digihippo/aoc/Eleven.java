package net.digihippo.aoc;

import java.util.*;

public class Eleven extends SolutionTemplate<Long, Long> {
    private record PointPair(TwoDPoint first, TwoDPoint second) {
        private static PointPair ofPair(final TwoDPoint one, final TwoDPoint two) {
            if (one.x() < two.x()) {
                return new PointPair(one, two);
            } else if (two.x() < one.x()) {
                return new PointPair(two, one);
            } else if (one.y() < two.y()) {
                return new PointPair(one, two);
            } else if (two.y() < one.y()) {
                return new PointPair(two, one);
            } else {
                throw new IllegalStateException("Can't pair a point with itself: " + one);
            }
        }
    }

    private static Long doThing(List<String> rows, long expansionFactor) {
        Grid g = new Grid(rows);

        List<Integer> columnIndexes = g.findEmptyColumns('.');
        List<Integer> rowIndexes = g.findEmptyRows('.');
        // expand the universe

        Map<PointPair, Long> distances = new HashMap<>();
        List<TwoDPoint> galaxies = g.find(c -> c != '.');

        for (int i = 0; i < galaxies.size(); i++) {
            TwoDPoint galaxy = galaxies.get(i);
            for (int j = 0; j < galaxies.size(); j++) {
                if (j != i) {
                    TwoDPoint other = galaxies.get(j);

                    PointPair p = PointPair.ofPair(galaxy, other);
                    if (!distances.containsKey(p)) {
                        long distance = 0L;
                        distance += Math.abs(p.first.x() - p.second.x());
                        distance += Math.abs(p.first.y() - p.second.y());

                        // now account for expansion
                        final int xStart = Math.min(p.first.x(), p.second.x());
                        final int xEnd = Math.max(p.first.x(), p.second.x());
                        for (int k = xStart; k < xEnd; k++) {
                            if (columnIndexes.contains(k)) {
                                distance += expansionFactor;
                            }
                        }

                        final int yStart = Math.min(p.first.y(), p.second.y());
                        final int yEnd = Math.max(p.first.y(), p.second.y());
                        for (int k = yStart; k < yEnd; k++) {
                            if (rowIndexes.contains(k)) {
                                distance += expansionFactor;
                            }
                        }

                        distances.put(p, distance);
                    }

                }
            }
        }

        // compute distances
        return distances.values().stream().mapToLong(Long::longValue).sum();
    }


    @Override
    Solution<Long> partOne() {
        return new Solution<>() {
            final List<String> rows = new ArrayList<>();

            @Override
            public Long result() {
                return doThing(rows, 1);
            }

            @Override
            public void accept(String s) {
                rows.add(s);
            }
        };
    }

    @Override
    Solution<Long> partTwo() {
        return new Solution<>() {
            private final List<String> rows = new ArrayList<>();

            @Override
            public Long result() {
                // it's _really_ not clear to me why this is not 1_000_000
                return doThing(rows, 999_999);
            }

            @Override
            public void accept(String s) {
                rows.add(s);
            }
        };
    }
}
