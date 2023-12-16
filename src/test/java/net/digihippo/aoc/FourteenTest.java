package net.digihippo.aoc;

class FourteenTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....""";

    FourteenTest() {
        super(new Fourteen(), EXAMPLE_INPUT, 136, 64, "fourteen.txt");
    }

}