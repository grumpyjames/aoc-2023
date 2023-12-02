package net.digihippo.aoc;

public class TwoTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            """;

    public TwoTest() {
        super(
                new Two(),
                EXAMPLE_INPUT,
                3,
                4,
                "two.txt"
        );
    }
}
