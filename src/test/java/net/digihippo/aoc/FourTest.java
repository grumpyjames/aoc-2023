package net.digihippo.aoc;

public class FourTest extends CommonParserTestTemplate<Integer, Object> {
    private static final String EXAMPLE_INPUT = """
            """;

    public FourTest() {
        super(new Four(), EXAMPLE_INPUT, 32, 34, "four.txt");
    }
}
