package net.digihippo.aoc;

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
}
