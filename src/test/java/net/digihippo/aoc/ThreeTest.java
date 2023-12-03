package net.digihippo.aoc;

class ThreeTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;

    public ThreeTest() {
        super(new Three(), EXAMPLE_INPUT, 4361, 31, "three.txt");
    }
}