package net.digihippo.aoc;

import java.util.*;

public class Ten extends GridSolution<Integer> {

    record Connection(TwoDPoint location, Grid.Offset offset, char content) {}

    @Override
    Integer partOne(Grid g) {
        final Map<TwoDPoint, Integer> distances = computeConnectedDistances(g);

        int max = 0;
        for (Map.Entry<TwoDPoint, Integer> entry : distances.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }

        return max;
    }

    private static Map<TwoDPoint, Integer> computeConnectedDistances(Grid g) {
        List<TwoDPoint> startPoints = g.find(c -> c == 'S');

        if (startPoints.size() != 1) {
            throw new IllegalStateException("Multiple start points: " + startPoints);
        }

        TwoDPoint start = startPoints.get(0);

        final List<Connection> connections = computeConnections(g, start, 'S');

        final Set<TwoDPoint> visited = new HashSet<>();
        visited.add(start);

        final Queue<TwoDPoint> toProcess = new ArrayDeque<>();
        connections.forEach(c -> toProcess.add(c.location));

        final Map<TwoDPoint, Integer> distances = new HashMap<>();

        distances.put(start, 0);

        connections.forEach(c -> distances.put(c.location, 1));

        while (!toProcess.isEmpty()) {
            final TwoDPoint poll = toProcess.poll();
            final int distance = distances.get(poll);

            final List<Connection> conn = computeConnections(g, poll, g.at(poll.x(), poll.y()));
            for (Connection connection : conn) {
                if (visited.add(connection.location)) {
                    distances.put(connection.location, distance + 1);
                    toProcess.add(connection.location);
                }
            }
        }
        return distances;
    }

    private static List<Connection> computeConnections(Grid g, TwoDPoint start, char here) {
        final List<Connection> connections = new ArrayList<>();
        g.visitOrthogonalNeighboursOf(start.x(), start.y(), (off, x, y, content) -> {
            switch (off) {
                case Up -> {
                    switch (content) {
                        case '|', '7', 'F' -> {
                            switch (here) {
                                case 'S', '|', 'L', 'J' -> connections.add(new Connection(new TwoDPoint(x, y), off, content));
                            }

                        }
                    }
                }
                case Down -> {
                    switch (content) {
                        case '|', 'L', 'J' -> {
                            switch (here) {
                                case 'S', '|', '7', 'F' -> connections.add(new Connection(new TwoDPoint(x, y), off, content));
                            }
                        }
                    }
                }
                case Left -> {
                    switch (content) {
                        case '-', 'L', 'F' -> {
                            switch (here) {
                                case 'S', '-', '7', 'J' -> connections.add(new Connection(new TwoDPoint(x, y), off, content));
                            }
                        }
                    }
                }
                case Right -> {
                    switch (content) {
                        case '-', '7', 'J' -> {
                            switch (here) {
                                case 'S', '-', 'L', 'F' -> connections.add(new Connection(new TwoDPoint(x, y), off, content));
                            }
                        }
                    }
                }
            }
        });
        return connections;
    }

    @Override
    Integer partTwo(Grid g) {
        final Map<TwoDPoint, Integer> distances = computeConnectedDistances(g);
        final Set<TwoDPoint> outside = new HashSet<>();
        final Set<TwoDPoint> inside = new HashSet<>();
        final Set<TwoDPoint> visited = new HashSet<>();

        g.visitRows(new Grid.RowVisitor() {
            @Override
            public void rowStarted(int y) {

            }

            @Override
            public void rowDone(int y) {

            }

            @Override
            public void onCell(int x, int y, char content) {
                TwoDPoint here = new TwoDPoint(x, y);
                if (!distances.containsKey(here)) {
                    // until proven otherwise.
                    inside.add(here);
                }
            }
        });

        final Queue<TwoDPoint> toProcess = new ArrayDeque<>();
        g.visitBoundary((x, y, content) -> toProcess.add(new TwoDPoint(x, y)));

        while (!toProcess.isEmpty()) {
            TwoDPoint poll = toProcess.poll();
            if (!distances.containsKey(poll)) {
                // must be outside, we started at the boundary.
                outside.add(poll);

                g.visitNeighboursOf(poll.x(), poll.y(), (x, y, content) -> {
                    TwoDPoint location = new TwoDPoint(x, y);
                    if (!distances.containsKey(location)) {
                        if (visited.add(location)) {
                            toProcess.add(location);
                        }
                    }
                });
            }
        }

        for (TwoDPoint twoDPoint : outside) {
            inside.remove(twoDPoint);
        }

        for (TwoDPoint twoDPoint : inside) {
            System.out.println(twoDPoint);
        }

        return inside.size();
    }
}
