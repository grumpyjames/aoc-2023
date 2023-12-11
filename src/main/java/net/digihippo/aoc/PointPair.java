package net.digihippo.aoc;

record PointPair(TwoDPoint first, TwoDPoint second) {
    public static PointPair ofPair(final TwoDPoint one, final TwoDPoint two) {
        if (one.x() < two.x()) {
            return new PointPair(one, two);
        } else if (two.x() < one.x()) {
            return new PointPair(two, one);
        } else if (one.y() < two.y()) {
            return new PointPair(one, two);
        } else if (two.y() < one.y()) {
            return new PointPair(two, one);
        } else {
            throw new IllegalStateException("Can't pair a point with itself: " + one);
        }
    }

    record Edges(PointPair top, PointPair left, PointPair right, PointPair bottom) {}

    static Edges computeEdges(TwoDPoint topLeft) {
        final TwoDPoint bottomLeft = topLeft.plus(new TwoDPoint(0, 1));
        final TwoDPoint topRight = topLeft.plus(new TwoDPoint(1, 0));
        final TwoDPoint bottomRight = topLeft.plus(new TwoDPoint(1, 1));

        return new Edges(
            PointPair.ofPair(topLeft, topRight),
            PointPair.ofPair(topLeft, bottomLeft),
            PointPair.ofPair(topRight, bottomRight),
            PointPair.ofPair(bottomLeft, bottomRight)
        );
    }
}
