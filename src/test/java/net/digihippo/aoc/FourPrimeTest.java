package net.digihippo.aoc;

class FourPrimeTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            """;

    public FourPrimeTest() {
        super(new FourPrime(), EXAMPLE_INPUT, 4, 5, "four.txt");
    }
}