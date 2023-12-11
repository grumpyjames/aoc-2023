package net.digihippo.aoc;

import java.util.*;

public class Ten extends GridSolution<Integer> {

    record Connection(TwoDPoint location, Grid.Offset offset, char content) {}

    @Override
    Integer partOne(Grid g) {
        final Map<TwoDPoint, Integer> distances = computeConnectedDistances(g, computeStartPoint(g));

        int max = 0;
        for (Map.Entry<TwoDPoint, Integer> entry : distances.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }

        return max;
    }

    private static Map<TwoDPoint, Integer> computeConnectedDistances(Grid g, TwoDPoint start) {

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

    private static TwoDPoint computeStartPoint(Grid g) {
        List<TwoDPoint> startPoints = g.find(c -> c == 'S');

        if (startPoints.size() != 1) {
            throw new IllegalStateException("Multiple start points: " + startPoints);
        }

        return startPoints.get(0);
    }

    private static List<Connection> computeConnections(Grid g, TwoDPoint start, char here) {
        final List<Connection> connections = new ArrayList<>();
        g.visitOrthogonalNeighboursOf(start.x(), start.y(), (off, x, y, content) -> {
            final Connection conn = new Connection(new TwoDPoint(x, y), off, content);
            switch (off) {
                case Up -> {
                    switch (content) {
                        case '|', '7', 'F' -> {
                            switch (here) {
                                case 'S', '|', 'L', 'J' -> connections.add(conn);
                            }

                        }
                    }
                }
                case Down -> {
                    switch (content) {
                        case '|', 'L', 'J' -> {
                            switch (here) {
                                case 'S', '|', '7', 'F' -> connections.add(conn);
                            }
                        }
                    }
                }
                case Left -> {
                    switch (content) {
                        case '-', 'L', 'F' -> {
                            switch (here) {
                                case 'S', '-', '7', 'J' -> connections.add(conn);
                            }
                        }
                    }
                }
                case Right -> {
                    switch (content) {
                        case '-', '7', 'J' -> {
                            switch (here) {
                                case 'S', '-', 'L', 'F' -> connections.add(conn);
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
        final TwoDPoint start = computeStartPoint(g);
        final Map<TwoDPoint, Integer> distances = computeConnectedDistances(g, start);

        final List<Connection> connections = computeConnections(g, start, 'S');

        PointPair top = PointPair.ofPair(start, start.plus(new TwoDPoint(1, 0)));
        PointPair left = PointPair.ofPair(start, start.plus(new TwoDPoint(0, 1)));
        PointPair right = PointPair.ofPair(start.plus(new TwoDPoint(1, 0)), start.plus(new TwoDPoint(1, 1)));
        PointPair bottom = PointPair.ofPair(start.plus(new TwoDPoint(0, 1)), start.plus(new TwoDPoint(1, 1)));

        final Set<PointPair> unconnected = new HashSet<>();
        connections.forEach(c -> {
            switch (c.offset) {
                case Up -> unconnected.add(top);
                case Down -> unconnected.add(bottom);
                case Left -> unconnected.add(left);
                case Right -> unconnected.add(right);
            }
        });

        distances.keySet().forEach(topLeft -> {
            char content = g.at(topLeft.x(), topLeft.y());

            TwoDPoint bottomLeft = topLeft.plus(new TwoDPoint(0, 1));
            TwoDPoint topRight = topLeft.plus(new TwoDPoint(1, 0));
            TwoDPoint bottomRight = topLeft.plus(new TwoDPoint(1, 1));

            final Set<PointPair> notConnected = new HashSet<>();
            notConnected.add(PointPair.ofPair(topLeft, bottomLeft));
            notConnected.add(PointPair.ofPair(topLeft, topRight));
            notConnected.add(PointPair.ofPair(topRight, bottomRight));
            notConnected.add(PointPair.ofPair(bottomLeft, bottomRight));

            switch (content) {
                case '|':
                    notConnected.remove(PointPair.ofPair(topLeft, bottomLeft));
                    notConnected.remove(PointPair.ofPair(topRight, bottomRight));
                    break;
                case '-':
                    notConnected.remove(PointPair.ofPair(topLeft, topRight));
                    notConnected.remove(PointPair.ofPair(bottomLeft, bottomRight));
                    break;
                case 'J':
                    notConnected.remove(PointPair.ofPair(topRight, bottomRight));
                    notConnected.remove(PointPair.ofPair(bottomLeft, bottomRight));
                    break;
                case 'F':
                    notConnected.remove(PointPair.ofPair(topLeft, topRight));
                    notConnected.remove(PointPair.ofPair(topLeft, bottomLeft));
                    break;
                case 'L':
                    notConnected.remove(PointPair.ofPair(topLeft, bottomLeft));
                    notConnected.remove(PointPair.ofPair(bottomLeft, bottomRight));
                    break;
                case '7':
                    notConnected.remove(PointPair.ofPair(topLeft, topRight));
                    notConnected.remove(PointPair.ofPair(topRight, bottomRight));
                    break;
                case 'S':
                    // already done, leave it
                    break;
            }

            if (content != 'S') {
                unconnected.addAll(notConnected);
            }
        });

        final Set<TwoDPoint> vertices = new HashSet<>();
        g.visitRows(new Grid.RowVisitor() {
            @Override
            public void rowStarted(int y) {

            }

            @Override
            public void rowDone(int y) {

            }

            @Override
            public void onCell(int x, int y, char content) {
                vertices.add(new TwoDPoint(x, y));
                vertices.add(new TwoDPoint(x + 1, y));
                vertices.add(new TwoDPoint(x + 1, y + 1));
                vertices.add(new TwoDPoint(x, y + 1));
            }
        });

        final Set<TwoDPoint> outside = new HashSet<>();
        final Queue<TwoDPoint> toProcess = new ArrayDeque<>();
        toProcess.add(new TwoDPoint(0, 0));
        outside.add(new TwoDPoint(0, 0));

        while (!toProcess.isEmpty()) {
            TwoDPoint poll = toProcess.poll();

            List<TwoDPoint> twoDPoints = poll.positiveNeighbours();
            for (TwoDPoint neighbour : twoDPoints) {
                PointPair edge = PointPair.ofPair(poll, neighbour);
                boolean connected = !unconnected.contains(edge);
                boolean inGrid = vertices.contains(neighbour);
                if (connected && inGrid) {
                    if (outside.add(neighbour)) {
                        toProcess.add(neighbour);
                    }
                }
            }
        }

        final Set<TwoDPoint> insideCells = new HashSet<>();
        g.visitRows(new Grid.RowVisitor() {
            @Override
            public void rowStarted(int y) {

            }

            @Override
            public void rowDone(int y) {

            }

            @Override
            public void onCell(int x, int y, char content) {
                TwoDPoint topLeft = new TwoDPoint(x, y);
                TwoDPoint bottomLeft = topLeft.plus(new TwoDPoint(0, 1));
                TwoDPoint topRight = topLeft.plus(new TwoDPoint(1, 0));
                TwoDPoint bottomRight = topLeft.plus(new TwoDPoint(1, 1));

                List<TwoDPoint> vertices = List.of(
                        topLeft, bottomLeft, topRight, bottomRight);

                boolean o = vertices.stream().anyMatch(outside::contains);
                if (!o) {
                    insideCells.add(topLeft);
                }
            }
        });

        return insideCells.size();
    }
}
