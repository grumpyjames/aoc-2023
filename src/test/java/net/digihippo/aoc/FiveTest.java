package net.digihippo.aoc;

class FiveTest extends CommonParserTestTemplate<Integer, Five.Parsed> {
    private static final String EXAMPLE_INPUT = """
            """;

    public FiveTest() {
        super(new Five(), EXAMPLE_INPUT, 2, 3, "five.txt");
    }
}