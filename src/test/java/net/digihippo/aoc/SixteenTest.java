package net.digihippo.aoc;

class SixteenTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....""";
    SixteenTest() {
        super(new Sixteen(), EXAMPLE_INPUT, 46, 51, "sixteen.txt");
    }
}