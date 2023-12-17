package net.digihippo.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Sixteen extends GridSolution<Integer> {
    enum Direction {
        Up,
        Down,
        Left,
        Right
    }
    record Beam(TwoDPoint location, Direction direction) {}

    @Override
    Integer partOne(Grid g) {
        Beam initialBeam = new Beam(new TwoDPoint(0, 0), Direction.Right);
        return energized(g, initialBeam);
    }

    private static int energized(Grid g, Beam initialBeam) {
        final Set<Beam> visited = new HashSet<>();
        final Deque<Beam> beams = new ArrayDeque<>();
        beams.add(initialBeam);

        while (!beams.isEmpty()) {
            Beam beam = beams.poll();
            if (!visited.add(beam)) {
                // been here before, no need to continue
                continue;
            }

            char optics = g.at(beam.location.x(), beam.location.y());
            switch (beam.direction) {
                case Up -> {
                    switch (optics) {
                        case '\\':
                            moveBeam(g, beams, beam.location.x() - 1, beam.location.y(), Direction.Left);
                            break;
                        case '/':
                            moveBeam(g, beams, beam.location.x() + 1, beam.location.y(), Direction.Right);
                            break;
                        case '-':
                            moveBeam(g, beams, beam.location.x() - 1, beam.location.y(), Direction.Left);
                            moveBeam(g, beams, beam.location.x() + 1, beam.location.y(), Direction.Right);
                            break;
                        case '|':
                        case '.':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() - 1, Direction.Up);
                            break;
                    }
                }
                case Down -> {
                    switch (optics) {
                        case '\\':
                            moveBeam(g, beams, beam.location.x() + 1, beam.location.y(), Direction.Right);
                            break;
                        case '/':
                            moveBeam(g, beams, beam.location.x() - 1, beam.location.y(), Direction.Left);
                            break;
                        case '-':
                            moveBeam(g, beams, beam.location.x() - 1, beam.location.y(), Direction.Left);
                            moveBeam(g, beams, beam.location.x() + 1, beam.location.y(), Direction.Right);
                            break;
                        case '|':
                        case '.':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() + 1, Direction.Down);
                            break;
                    }
                }
                case Left -> {
                    switch (optics) {
                        case '\\':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() - 1, Direction.Up);
                            break;
                        case '/':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() + 1, Direction.Down);
                            break;
                        case '|':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() - 1, Direction.Up);
                            moveBeam(g, beams, beam.location.x(), beam.location.y() + 1, Direction.Down);
                            break;
                        case '-':
                        case '.':
                            moveBeam(g, beams, beam.location.x() - 1, beam.location.y(), Direction.Left);
                            break;
                    }
                }
                case Right -> {
                    switch (optics) {
                        case '\\':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() + 1, Direction.Down);
                            break;
                        case '/':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() - 1, Direction.Up);
                            break;
                        case '|':
                            moveBeam(g, beams, beam.location.x(), beam.location.y() - 1, Direction.Up);
                            moveBeam(g, beams, beam.location.x(), beam.location.y() + 1, Direction.Down);
                            break;
                        case '-':
                        case '.':
                            moveBeam(g, beams, beam.location.x() + 1, beam.location.y(), Direction.Right);
                            break;
                    }
                }
            }
        }

        Set<TwoDPoint> locations = visited.stream().map(v -> v.location).collect(Collectors.toSet());
        return locations.size();
    }

    private static void moveBeam(Grid g, Deque<Beam> beams, int newX, int newY, Direction newDirection) {
        if (g.contains(newX, newY)) {
            beams.add(new Beam(new TwoDPoint(newX, newY), newDirection));
        }
    }

    @Override
    Integer partTwo(Grid g) {
        final List<Beam> startPoints = new ArrayList<>();
        for (int i = 0; i < g.columnCount(); i++) {
            startPoints.add(new Beam(new TwoDPoint(i, 0), Direction.Down));
            startPoints.add(new Beam(new TwoDPoint(i, g.rowCount() - 1), Direction.Up));
        }
        for (int i = 0; i < g.rowCount(); i++) {
            startPoints.add(new Beam(new TwoDPoint(0, i), Direction.Right));
            startPoints.add(new Beam(new TwoDPoint(g.columnCount() - 1, i), Direction.Right));
        }

        return startPoints.stream().mapToInt(b -> energized(g, b)).max().orElseThrow();
    }
}
