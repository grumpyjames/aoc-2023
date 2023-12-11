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
}
