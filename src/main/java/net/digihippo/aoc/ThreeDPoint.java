package net.digihippo.aoc;

public record ThreeDPoint(int x, int y, int z) {

    public static ThreeDPoint from(String str) {
        final String[] coords = str.split(",");
        return new ThreeDPoint(
                Integer.parseInt(coords[0]),
                Integer.parseInt(coords[1]),
                Integer.parseInt(coords[2]));
    }

    public ThreeDPoint moveDown() {
        return new ThreeDPoint(x, y, z - 1);
    }
}
