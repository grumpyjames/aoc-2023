package net.digihippo.aoc;

class SixTest extends TestTemplate<Long, Long> {

    private static final String EXAMPLE_INPUT = """
            Time:      7  15   30
            Distance:  9  40  200
            """;

    public SixTest() {
        super(new Six(), EXAMPLE_INPUT, 288L, 71503L, "six.txt");
    }
}