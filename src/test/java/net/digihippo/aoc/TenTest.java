package net.digihippo.aoc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TenTest extends TestTemplate<Integer, Integer> {

    private static final String EXAMPLE_INPUT = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF""";

    public TenTest() {
        super(new Ten(), EXAMPLE_INPUT, 4, 1, "ten.txt");
    }

    @Test
    void partsEnclosedButSomeNot() {
        final String trickierExample = """
                ...........
                .S-------7.
                .|F-----7|.
                .||.....||.
                .||.....||.
                .|L-7.F-J|.
                .|..|.|..|.
                .L--J.L--J.
                ...........""";

        assertEquals(4, solution.examplePartTwo(trickierExample, solution.partTwo()));
    }

    @Test
    void fiendish() {
        final String trickierExample = """
                .F----7F7F7F7F-7....
                .|F--7||||||||FJ....
                .||.FJ||||||||L7....
                FJL7L7LJLJ||LJ.L-7..
                L--J.L7...LJS7F-7L7.
                ....F-J..F7FJ|L7L7L7
                ....L7.F7||L7|.L7L7|
                .....|FJLJ|FJ|F7|.LJ
                ....FJL-7.||.||||...
                ....L---J.LJ.LJLJ...""";

        assertEquals(8, solution.examplePartTwo(trickierExample, solution.partTwo()));
    }
}