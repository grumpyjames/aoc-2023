package net.digihippo.aoc;

import java.util.*;

public class Ten extends GridSolution<Integer> {

    record Connection(TwoDPoint location, Grid.Offset offset, char content) {}

    @Override
    Integer partOne(Grid g) {
        TwoDPoint start = computeStartPoint(g);
        final Map<TwoDPoint, Integer> distances =
                computeConnectedDistances(g, start, computeConnections(g, start, 'S'));

        int max = 0;
        for (Map.Entry<TwoDPoint, Integer> entry : distances.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
            }
        }

        return max;
    }

    private static Map<TwoDPoint, Integer> computeConnectedDistances(
            Grid g,
            TwoDPoint start,
            List<Connection> connections) {

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
        final List<Connection> connections = computeConnections(g, start, 'S');
        final Map<TwoDPoint, Integer> distances = computeConnectedDistances(g, start, connections);

        final Set<PointPair> unconnected = computeUnconnectedEdges(g, start, connections, distances);

        final Set<TwoDPoint> vertices = g.vertexSet();
        final Set<TwoDPoint> outside = computeOutsideVertices(unconnected, vertices);
        final Set<TwoDPoint> insideCells = computeInsideCells(g, outside);

        return insideCells.size();
    }

    private static Set<PointPair> computeUnconnectedEdges(
            Grid g,
            TwoDPoint start,
            List<Connection> connections,
            Map<TwoDPoint, Integer> distances) {

        final Set<PointPair> unconnected = new HashSet<>();
        addStartEdges(start, connections, unconnected);

        distances.keySet().forEach(topLeft -> {
            char content = g.at(topLeft.x(), topLeft.y());
            final Set<PointPair> notConnected = new HashSet<>();
            final PointPair.Edges edges = PointPair.computeEdges(topLeft);

            notConnected.add(edges.top());
            notConnected.add(edges.left());
            notConnected.add(edges.right());
            notConnected.add(edges.bottom());

            switch (content) {
                case '|' -> {
                    notConnected.remove(edges.left());
                    notConnected.remove(edges.right());
                }
                case '-' -> {
                    notConnected.remove(edges.top());
                    notConnected.remove(edges.bottom());
                }
                case 'J' -> {
                    notConnected.remove(edges.right());
                    notConnected.remove(edges.bottom());
                }
                case 'F' -> {
                    notConnected.remove(edges.top());
                    notConnected.remove(edges.left());
                }
                case 'L' -> {
                    notConnected.remove(edges.left());
                    notConnected.remove(edges.bottom());
                }
                case '7' -> {
                    notConnected.remove(edges.top());
                    notConnected.remove(edges.right());
                }
                case 'S' ->
                    // managed previously
                    notConnected.clear();
            }

            unconnected.addAll(notConnected);
        });

        return unconnected;
    }

    private static void addStartEdges(TwoDPoint start, List<Connection> connections, Set<PointPair> unconnected) {
        final PointPair.Edges startEdges = PointPair.computeEdges(start);
        connections.forEach(c -> {
            switch (c.offset) {
                case Up -> unconnected.add(startEdges.top());
                case Down -> unconnected.add(startEdges.bottom());
                case Left -> unconnected.add(startEdges.left());
                case Right -> unconnected.add(startEdges.right());
            }
        });
    }

    private static Set<TwoDPoint> computeInsideCells(Grid g, Set<TwoDPoint> outside) {
        final Set<TwoDPoint> insideCells = new HashSet<>();
        g.visit((x, y, content) -> {
            TwoDPoint topLeft = new TwoDPoint(x, y);
            TwoDPoint bottomLeft = topLeft.plus(new TwoDPoint(0, 1));
            TwoDPoint topRight = topLeft.plus(new TwoDPoint(1, 0));
            TwoDPoint bottomRight = topLeft.plus(new TwoDPoint(1, 1));

            List<TwoDPoint> cellVertices = List.of(
                    topLeft, bottomLeft, topRight, bottomRight);

            if (cellVertices.stream().noneMatch(outside::contains)) {
                insideCells.add(topLeft);
            }
        });
        return insideCells;
    }

    private static Set<TwoDPoint> computeOutsideVertices(Set<PointPair> unconnected, Set<TwoDPoint> vertices) {
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
        return outside;
    }
}
