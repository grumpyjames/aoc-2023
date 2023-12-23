package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;

public class EighteenAlt extends CommonParserSolutionTemplate<Long, EighteenAlt.Digger> {
    @Override
    Digger parse(String line) {
        String[] parts = line.split(" ");
        return new Digger(mapToOffset(parts[0].charAt(0)), Integer.parseInt(parts[1]), Rgb.parse(parts[2]));
    }

    private Grid.Offset mapToOffset(char c) {
        return switch (c) {
            case 'U' -> Grid.Offset.Up;
            case 'D' -> Grid.Offset.Down;
            case 'L' -> Grid.Offset.Left;
            case 'R' -> Grid.Offset.Right;
            default -> throw new IllegalStateException();
        };
    }

    record Rgb(int distance, Grid.Offset offset) {

        public static Rgb parse(String part) {
            int distance = Integer.parseInt(part.substring(2, part.length() - 2), 16);
            char c = part.charAt(7);
            switch (c) {
                case '0' -> {
                    return new Rgb(distance, Grid.Offset.Right);
                }
                case '1' -> {
                    return new Rgb(distance, Grid.Offset.Down);
                }
                case '2' -> {
                    return new Rgb(distance, Grid.Offset.Left);
                }
                case '3' -> {
                    return new Rgb(distance, Grid.Offset.Up);
                }
            }
            throw new IllegalStateException();
        }

        public TwoDPoint applyTo(TwoDPoint origin) {
            return offset.move(origin, distance);
        }
    }

    @Override
    Long partOne(List<Digger> parsed) {
        final List<TwoDPoint> vertices = new ArrayList<>();

        TwoDPoint origin = new TwoDPoint(0, 0);
        vertices.add(origin);

        long perimeter = 0;
        for (Digger digger : parsed) {
            TwoDPoint next = digger.simpleDig(origin);
            vertices.add(next);

            origin = next;

            perimeter += digger.simplePerimeter();
        }

        return area(vertices) + perimeter / 2 + 1;
    }

    private Long area(List<TwoDPoint> vertices) {
        int numberOfVertices = vertices.size();
        long sum1 = 0L;
        long sum2 = 0L;

        for (int i = 0; i < numberOfVertices - 1; i++) {
            sum1 += ((long) vertices.get(i).x() * vertices.get(i + 1).y());
            sum2 += ((long) vertices.get(i).y() * vertices.get(i + 1).x());
        }

        sum1 += (long) vertices.get(numberOfVertices - 1).x() * vertices.getFirst().y();
        sum2 += (long) vertices.getFirst().x() * vertices.get(numberOfVertices-1).y();

        return Math.abs(sum1 - sum2) / 2;
    }

    @Override
    Long partTwo(List<Digger> parsed) {
        final List<TwoDPoint> vertices = new ArrayList<>();

        TwoDPoint origin = new TwoDPoint(0, 0);
        vertices.add(origin);

        long perimeter = 0;
        for (Digger digger : parsed) {
            TwoDPoint next = digger.applyTo(origin);
            vertices.add(next);

            origin = next;
            perimeter += digger.rgbPerimeter();
        }

        return area(vertices) + perimeter / 2 + 1;
    }

    record Digger(Grid.Offset c, int distance, Rgb rgb) {
        public TwoDPoint applyTo(TwoDPoint origin) {
            return rgb.applyTo(origin);
        }

        public TwoDPoint simpleDig(TwoDPoint origin) {
            return c.move(origin, distance);
        }

        public long simplePerimeter() {
            return distance;
        }

        public long rgbPerimeter() {
            return rgb.distance;
        }
    }
}
