package net.digihippo.aoc;

import java.util.*;

public class Seventeen extends GridSolution<Integer> {
    record Thing(TwoDPoint twoDPoint, Grid.Offset offset, int count) {
        public boolean allows(Grid.Offset o) {
            if (count < 3) {
                return !o.oppositeOf(offset);
            } else {
                return o != offset;
            }
        }

        public Thing travelTo(TwoDPoint dest, Grid.Offset o) {
            if (o != offset) {
                return new Thing(dest, o, 1);
            } else {
                return new Thing(dest, o, count + 1);
            }
        }
    }

    record AltBuffer(List<Grid.Offset> offsets) {

        public boolean allows(Grid.Offset o) {
            // bleurgh.
            if (offsets.isEmpty()) {
                return true;
            }

            if (offsets.size() < 4) {
                return o == offsets.getLast();
            }

            final Set<Grid.Offset> seen = last(4);

            if (seen.size() == 1) {
                if (offsets.size() < 10) {
                    return seen.contains(o);
                }

                Set<Grid.Offset> lastTen = last(10);
                return lastTen.size() > 1 || !lastTen.contains(o);
            } else {
                return o == offsets.getLast();
            }
        }

        private Set<Grid.Offset> last(int k) {
            final Set<Grid.Offset> seen = new HashSet<>();
            for (int i = 1; i < k + 1; i++) {
                Grid.Offset offset = offsets.get(offsets.size() - i);
                seen.add(offset);
            }
            return seen;
        }

        public AltBuffer add(Grid.Offset o) {
            final List<Grid.Offset> newOffs = new ArrayList<>(offsets);
            newOffs.add(o);

            return new AltBuffer(newOffs);
        }
    }

    record Another(TwoDPoint twoDPoint, AltBuffer ab) {
        public boolean allows(Grid.Offset o) {
            return ab.allows(o);
        }

        public Another travelTo(TwoDPoint dest, Grid.Offset o) {
            return new Another(dest, ab.add(o));
        }
    }

    @Override
    Integer partOne(Grid g) {
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
                if (t.allows(o)) {
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

    @Override
    Integer partTwo(Grid g) {
        final Map<Another, Integer> minDistances = new HashMap<>();

        TwoDPoint origin = new TwoDPoint(0, 0);
        Another thing = new Another(origin, new AltBuffer(new ArrayList<>()));
        minDistances.put(thing, 0);

        final Queue<Another> points = new ArrayDeque<>();
        points.add(thing);

        while (!points.isEmpty()) {
            Another t = points.poll();
            int distance = minDistances.get(t);

            g.visitOrthogonalNeighboursOf(t.twoDPoint.x(), t.twoDPoint.y(), (o, x, y, content) -> {
                if (t.allows(o)) {
                    TwoDPoint trial = new TwoDPoint(x, y);
                    int newDistance = distance + Converters.fromAsciiChar(content);
                    Another newAnother = t.travelTo(trial, o);

                    Integer bestDistance = minDistances.get(newAnother);
                    if (bestDistance == null) {
                        minDistances.put(newAnother, newDistance);
                        points.add(newAnother);
                    } else if (newDistance < bestDistance) {
                        minDistances.put(newAnother, newDistance);
                        points.add(newAnother);
                    }
                }
            });

        }

        int best = Integer.MAX_VALUE;
        for (Map.Entry<Another, Integer> entry : minDistances.entrySet()) {
            if (entry.getKey().twoDPoint.x() == g.columnCount() - 1 && entry.getKey().twoDPoint.y() == g.rowCount() - 1) {
                best = Math.min(entry.getValue(), best);
            }
        }
        return best;
    }
}
