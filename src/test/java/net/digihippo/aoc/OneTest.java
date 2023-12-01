package net.digihippo.aoc;

public class OneTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            """;

    public OneTest() {
        super(new One(), EXAMPLE_INPUT, 42, 43, "one.txt");
    }
}
