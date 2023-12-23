package net.digihippo.aoc;

import java.util.List;

public record TwoDPoint(int x, int y) {
    public TwoDPoint plus(TwoDPoint other) {
        return new TwoDPoint(x + other.x, y + other.y);
    }

    private double size() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private TwoDPoint minus(TwoDPoint point) {
        return new TwoDPoint(this.x - point.x, this.y - point.y);
    }

    public List<TwoDPoint> positiveNeighbours() {
        return List.of(
                new TwoDPoint(x + 1, y),
                new TwoDPoint(x, y + 1),
                new TwoDPoint(x - 1, y),
                new TwoDPoint(x, y - 1)
        );
    }

    public int manhattanDistance(TwoDPoint other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }
}
