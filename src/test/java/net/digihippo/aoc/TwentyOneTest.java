package net.digihippo.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TwentyOneTest extends TestTemplate<Long, Long> {
    private static final String EXAMPLE_INPUT = """
            ...........
            .....###.#.
            .###.##..#.
            ..#.#...#..
            ....#.#....
            .##..S####.
            .##..#...#.
            .......##..
            .##.#.####.
            .##..##.##.
            ...........
            """;

    public TwentyOneTest() {
        super(new TwentyOne(), EXAMPLE_INPUT, 16L, 533997411669553L, "twentyone.txt");
    }

    @Test
    void secondDifferences() {
        assertEquals(
                new TwentyOne.QuadraticResult(15397, -15299, 3795),
                TwentyOne.secondDifferences(3893, 34785, 96471));
    }
}