package net.digihippo.aoc;

import java.util.*;

public class TwentyThree extends GridSolution<Integer> {

    private static final class PathSoFar
    {
        private TwoDPoint head = null;
        private final Set<TwoDPoint> visited = new HashSet<>();
        private final int maxY;
        private final TwoDPoint start;
        private boolean chance = true;
        private boolean complete = false;

        public PathSoFar(int maxY, TwoDPoint start) {
            this.maxY = maxY;
            this.start = start;
            this.visited.add(start);
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
            PathSoFar pathSoFar = new PathSoFar(maxY, start);

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

        public Hike toHike() {
            return new Hike(start, head, visited.size());
        }
    }

    @Override
    Integer partOne(Grid g) {
        return longestHike(g, (len, here, x, y, o, content) -> acceptable(o, content));
    }
    record DecisionPoint(TwoDPoint decisionPoint, TwoDPoint pathToSuccess) {}

    private static final class VertexPath
    {
        private final Set<TwoDPoint> visited;
        private final TwoDPoint location;
        private final int length;

        private VertexPath(TwoDPoint start, int length, Set<TwoDPoint> visited) {
            this.location = start;
            this.length = length;
            this.visited = visited;
            this.visited.add(start);
        }

        public boolean accepts(Hike hike) {
            return !visited.contains(hike.alternateVertex(location));
        }

        public VertexPath walk(Hike hike) {
            final TwoDPoint dest = hike.alternateVertex(location);
            final Set<TwoDPoint> visited = new HashSet<>(this.visited);

            return new VertexPath(dest, length + hike.length, visited);
        }

        public boolean isAt(TwoDPoint finish) {
            return finish.equals(location);
        }

        public int length() {
            return length - visited.size() + 1;
        }

        @Override
        public String toString() {
            return "@" + location + " visited: " + visited.size();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VertexPath that = (VertexPath) o;
            return length == that.length && Objects.equals(location, that.location) && Objects.equals(visited, that.visited);
        }

        @Override
        public int hashCode() {
            return Objects.hash(visited, location, length);
        }
    }

    int longestPath(
            final Set<TwoDPoint> visited,
            final Map<TwoDPoint, Set<Neighbour>> edgeMap,
            final TwoDPoint from,
            final TwoDPoint to,
            final int distanceSoFar) {
        if (from.equals(to)) {
            return distanceSoFar;
        }

        visited.add(from);
        int max = 0;

        for (Neighbour neighbour : edgeMap.get(from)) {
            final TwoDPoint destination = neighbour.dest;
            if (!visited.contains(destination)) {
                max = Math.max(max, longestPath(visited, edgeMap, destination, to, neighbour.distance - 1 + distanceSoFar));
            }
        }

        visited.remove(from);

        return max;
    }

    record Neighbour(TwoDPoint dest, int distance) {}

    @Override
    Integer partTwo(Grid g) {
        Set<Hike> hikes = new HashSet<>(hikes(g));

        final Map<TwoDPoint, Set<Neighbour>> byVertex = new HashMap<>();
        hikes.forEach(h -> {
            byVertex.computeIfAbsent(h.from, twoDPoint -> new HashSet<>()).add(new Neighbour(h.to, h.length));
            byVertex.computeIfAbsent(h.to, twoDPoint -> new HashSet<>()).add(new Neighbour(h.from, h.length));
        });

        TwoDPoint start = new TwoDPoint(1, 0);
        TwoDPoint finish = new TwoDPoint(g.columnCount() - 2, g.rowCount() - 1);
        VertexPath vp = new VertexPath(start, 0, new HashSet<>());

//        List<VertexPath> vps = new ArrayList<>();
//        vps.add(vp);

        HashSet<TwoDPoint> visited = new HashSet<>();
        return longestPath(visited, byVertex, start, finish, 0);

//        int longest = -1;
//        while (!vps.isEmpty()) {
//            final Set<VertexPath> newPaths = new HashSet<>();
//            final Iterator<VertexPath> iterator = vps.iterator();
//            while (iterator.hasNext()) {
//                VertexPath here = iterator.next();
//                iterator.remove();
//
//                if (here.isAt(finish)) {
//                    if (longest < here.length()) {
//                        longest = here.length();
//                        System.out.println(Instant.now() + ": " + longest);
//                    }
//                } else {
//                    Set<Hike> fromHere = byVertex.get(here.location);
//                    for (Hike hike : fromHere) {
//                        if (here.accepts(hike)) {
//                            newPaths.add(here.walk(hike));
//                        }
//                    }
//                }
//            }
//
//            vps.addAll(newPaths);
//        }

        // 3962 ? too low still.
//        return longest;
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

    record Hike(TwoDPoint from, TwoDPoint to, int length) {
        public TwoDPoint alternateVertex(TwoDPoint location) {
            return from.equals(location) ? to : from;
        }
    }

    private List<Hike> hikes(Grid g) {
        final List<Hike> result = new ArrayList<>();
        final Set<TwoDPoint> vertices = new HashSet<>();

        TwoDPoint start = new TwoDPoint(1, 0);

        final List<PathSoFar> paths = new ArrayList<>();
        final PathSoFar initial = new PathSoFar(g.rowCount() - 1, start);
        initial.append(start);
        paths.add(initial);

        while (!paths.isEmpty()) {
            final List<PathSoFar> newPaths = new ArrayList<>();

            for (Iterator<PathSoFar> iterator = paths.iterator(); iterator.hasNext();) {
                PathSoFar path = iterator.next();
                TwoDPoint here = path.last();

                final List<TwoDPoint> next = new ArrayList<>();
                g.visitOrthogonalNeighboursOf(here.x(), here.y(), (o, x, y, content) -> {
                    TwoDPoint neighbour = new TwoDPoint(x, y);
                    if (!path.hasVisited(neighbour)) {
                        if (content != '#') {
                            next.add(neighbour);
                        }
                    }
                });

                if (next.isEmpty()) {
                    result.add(path.toHike());
                    iterator.remove();
                } else {
                    if (next.size() > 1) {
                        result.add(path.toHike());
                        iterator.remove();
                        if (vertices.add(here)) {
                            for (TwoDPoint twoDPoint : next) {
                                final PathSoFar newPath = new PathSoFar(path.maxY, here);
                                newPath.append(twoDPoint);
                                newPaths.add(newPath);
                            }
                        }
                    } else {
                        path.append(next.getFirst());
                    }
                }
            }

            for (Iterator<PathSoFar> iterator = newPaths.iterator(); iterator.hasNext(); ) {
                PathSoFar newPath = iterator.next();
                if (newPath.complete) {
                    result.add(newPath.toHike());
                    iterator.remove();
                }
            }
            paths.addAll(newPaths);
        }

        return result;
    }

    private static int longestHike(Grid g, Worthy worthy) {
        TwoDPoint start = new TwoDPoint(1, 0);

        final List<PathSoFar> paths = new ArrayList<>();
        final PathSoFar initial = new PathSoFar(g.rowCount() - 1, start);
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
