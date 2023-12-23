package net.digihippo.aoc;

import java.util.*;

public class TwentyThree extends GridSolution<Integer> {

    private static final class PathSoFar
    {
        private TwoDPoint head = null;
        private final Set<TwoDPoint> visited = new HashSet<>();
        private final int maxY;
        private boolean chance = true;
        private boolean complete = false;

        public PathSoFar(int maxY) {
            this.maxY = maxY;
        }

        void append(TwoDPoint point) {
            head = point;
            visited.add(point);
            if (point.y() == maxY) {
                complete = true;
            }
        }

        public boolean hasAChance() {
            return chance;
        }

        public TwoDPoint last() {
            return head;
        }

        public boolean hasVisited(TwoDPoint neighbour) {
            return visited.contains(neighbour);
        }

        public void deadEnd() {
            chance = false;
        }

        public PathSoFar copy() {
            PathSoFar pathSoFar = new PathSoFar(maxY);

            pathSoFar.head = this.head;
            pathSoFar.visited.addAll(this.visited);

            return pathSoFar;
        }

        public int length() {
            return this.visited.size();
        }

        @Override
        public String toString() {
            return System.identityHashCode(this) + " : len " + length() + " @ " + head;
        }

        public boolean worthContinuing() {
            return hasAChance() && !complete;
        }
    }

    @Override
    Integer partOne(Grid g) {
        return longestHike(g, (len, here, x, y, o, content) -> acceptable(o, content));
    }

    @Override
    Integer partTwo(Grid g) {
        final int accessible = g.count(c -> c != '#');

        final TwoDPoint finish = new TwoDPoint(g.columnCount() - 2, g.rowCount() - 1);

        TwoDPoint prev = null;
        TwoDPoint point = finish;
        // track back until we find the first split.
        final Set<TwoDPoint> visited = new HashSet<>();
        final List<TwoDPoint> points = new ArrayList<>();
        while (true) {
            visited.add(point);
            g.visitOrthogonalNeighboursOf(point.x(), point.y(), (o, x, y, content) -> {
                TwoDPoint neigh = new TwoDPoint(x, y);
                if (content != '#' && !visited.contains(neigh)) {
                    points.add(neigh);
                }
            });

            if (points.size() > 1) {
                break;
            } else {
                prev = point;
                point = points.getFirst();
                points.clear();
            }
        }

        final TwoDPoint decisionPoint = point;
        final TwoDPoint pathToSuccess = prev;
        return longestHike(g, (visitedSize, here, x, y, o, content) -> {
            if (accessible - visitedSize < finish.manhattanDistance(here)) {
                return false;
            }


            boolean atDecisionPoint = decisionPoint.x() == here.x() && decisionPoint.y() == here.y();
            if (atDecisionPoint) {
                return pathToSuccess.x() == x && pathToSuccess.y() == y;
            }

            return content != '#';
        });
    }

    interface Worthy {
        boolean worthy(
                int visitedSize,
                TwoDPoint here,
                int x,
                int y,
                Grid.Offset o,
                char content);
    }

    private static int longestHike(Grid g, Worthy worthy) {
        TwoDPoint start = new TwoDPoint(1, 0);

        final List<PathSoFar> paths = new ArrayList<>();
        final PathSoFar initial = new PathSoFar(g.rowCount() - 1);
        initial.append(start);
        paths.add(initial);

        while (paths.stream().anyMatch(PathSoFar::worthContinuing)) {
            final List<PathSoFar> newPaths = new ArrayList<>();
            if (paths.getFirst().length() % 100 == 0) {
                System.out.println("Looking at paths of length " + paths.getFirst().length());
            }

            for (Iterator<PathSoFar> iterator = paths.iterator(); iterator.hasNext(); ) {
                PathSoFar path = iterator.next();
                if (!path.hasAChance()) {
                    iterator.remove();
                    continue;
                } else if (path.complete) {
                    continue;
                }

                TwoDPoint here = path.last();
                final List<TwoDPoint> next = new ArrayList<>();
                g.visitOrthogonalNeighboursOf(here.x(), here.y(), (o, x, y, content) -> {
                    TwoDPoint neighbour = new TwoDPoint(x, y);
                    if (!path.hasVisited(neighbour)) {
                        if (worthy.worthy(path.length(), here, x, y, o, content)) {
                            next.add(neighbour);
                        }
                    }
                });

                if (next.isEmpty()) {
                    path.deadEnd();
                } else {
                    for (int i = 1; i < next.size(); i++) {
                        final PathSoFar newPath = path.copy();
                        newPath.append(next.get(i));
                        newPaths.add(newPath);
                    }
                    path.append(next.getFirst());
                }
            }

            paths.addAll(newPaths);
        }

        return paths.stream().filter(p -> p.complete).mapToInt(PathSoFar::length).max().orElseThrow() - 1;
    }

    private static boolean acceptable(final Grid.Offset o, final char content) {
        return content == '.' || downhill(o, content);
    }

    private static boolean downhill(Grid.Offset o, char content) {
        return
                o == Grid.Offset.Down && content == 'v' ||
                o == Grid.Offset.Up && content == '^' ||
                o == Grid.Offset.Left && content == '<' ||
                o == Grid.Offset.Right && content == '>';
    }
}
