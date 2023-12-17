package net.digihippo.aoc;

import java.util.*;
import java.util.function.BiFunction;

public class Seventeen extends GridSolution<Integer> {
    record Thing(TwoDPoint twoDPoint, Grid.Offset offset, int count) {
        public boolean allows(Grid.Offset o) {
            if (count < 3) {
                return !o.oppositeOf(offset);
            } else {
                return o != offset && !o.oppositeOf(offset);
            }
        }

        public boolean allowsUltra(Grid.Offset o) {
            if (offset == null) {
                return true;
            }

            if (count < 4) {
                return offset == o;
            }
            // =>:   4 <= count

            if (count < 10) {
                return !offset.oppositeOf(o);
            }
            // =>:  10 <= count

            if (count == 10) {
                return offset != o && !offset.oppositeOf(o);
            }

            throw new IllegalStateException("wtf: " + offset + " , " + count + ", " + o);
        }

        public Thing travelTo(TwoDPoint dest, Grid.Offset o) {
            if (o != offset) {
                return new Thing(dest, o, 1);
            } else {
                return new Thing(dest, o, count + 1);
            }
        }
    }

    @Override
    Integer partOne(Grid g) {
        return compute(g, Thing::allows);
    }

    @Override
    Integer partTwo(Grid g) {
        return compute(g, Thing::allowsUltra);
    }

    private static int compute(Grid g, BiFunction<Thing, Grid.Offset, Boolean> allows) {
        final Map<Thing, Integer> minDistances = new HashMap<>();
        TwoDPoint origin = new TwoDPoint(0, 0);
        Thing thing = new Thing(origin, null, 0);
        minDistances.put(thing, 0);

        final Queue<Thing> points = new ArrayDeque<>();
        points.add(thing);


        while (!points.isEmpty()) {
            Thing t = points.poll();
            int distance = minDistances.get(t);

            g.visitOrthogonalNeighboursOf(t.twoDPoint.x(), t.twoDPoint.y(), (o, x, y, content) -> {
                boolean allowed = allows.apply(t, o);
                if (allowed) {
                    TwoDPoint trial = new TwoDPoint(x, y);
                    int newDistance = distance + Converters.fromAsciiChar(content);
                    Thing newThing = t.travelTo(trial, o);

                    Integer bestDistance = minDistances.get(newThing);
                    if (bestDistance == null) {
                        minDistances.put(newThing, newDistance);
                        points.add(newThing);
                    } else if (newDistance < bestDistance) {
                        minDistances.put(newThing, newDistance);
                        points.add(newThing);
                    }
                }
            });

        }

        int best = Integer.MAX_VALUE;
        for (Map.Entry<Thing, Integer> entry : minDistances.entrySet()) {
            if (entry.getKey().twoDPoint.x() == g.columnCount() - 1 && entry.getKey().twoDPoint.y() == g.rowCount() - 1) {
                best = Math.min(entry.getValue(), best);
            }
        }
        return best;
    }
}
