package net.digihippo.aoc;

import java.util.*;

public class Eleven extends GridSolution<Long> {

    private static Long doThing(Grid g, long expansionFactor) {
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
                        distance += Math.abs(p.first().x() - p.second().x());
                        distance += Math.abs(p.first().y() - p.second().y());

                        // now account for expansion
                        final int xStart = Math.min(p.first().x(), p.second().x());
                        final int xEnd = Math.max(p.first().x(), p.second().x());
                        for (int k = xStart; k < xEnd; k++) {
                            if (columnIndexes.contains(k)) {
                                distance += expansionFactor;
                            }
                        }

                        final int yStart = Math.min(p.first().y(), p.second().y());
                        final int yEnd = Math.max(p.first().y(), p.second().y());
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
    Long partOne(Grid g) {
        return doThing(g, 1);
    }

    @Override
    Long partTwo(Grid g) {
        // it's _really_ not clear to me why this is not 1_000_000
        return doThing(g, 999_999);
    }
}
