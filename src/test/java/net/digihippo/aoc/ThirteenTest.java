package net.digihippo.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThirteenTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
                        
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""";

    ThirteenTest() {
        super(new Thirteen(), EXAMPLE_INPUT, 405, 400, "thirteen.txt");
    }

    @Test
    void smudgeScore() {
        final String example = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.""";
        Grid grid = Grid.fromString(example);
        assertEquals(5, Thirteen.score(grid));
        assertEquals(300, Thirteen.smudgeScore(grid));
    }

    @Test
    void smudgeScoreAgain() {
        final String example = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#""";
        Grid grid = Grid.fromString(example);
        assertEquals(400, Thirteen.score(grid));
        assertEquals(100, Thirteen.smudgeScore(grid));
    }

    @Test
    void findExampleReflection() {
        final String example = """
                #...##..#
                #....#..#
                ..##..###
                #####.##.
                #####.##.
                ..##..###
                #....#..#""";

        assertEquals(400, Thirteen.score(Grid.fromString(example)));
    }

    @Test
    void findAnotherReflection() {
        final String example = """
                #.##..##.
                ..#.##.#.
                ##......#
                ##......#
                ..#.##.#.
                ..##..##.
                #.#.##.#.""";

        assertEquals(5, Thirteen.score(Grid.fromString(example)));
    }


    @Test
    void findReflection() {
        final String example = """
                ####.##....##.###
                ###....####....##
                ..#.#..#..#..#.#.
                ####.########.###
                ###...#....#...##
                ##..##..#...##..#
                ##..#........#..#
                ...#....##....#..
                ..#.##.#..#.##.#.
                .......####......
                ###..#..##..#..##
                ...#...####...#..
                .....###..###....
                """;

        assertEquals(1, Thirteen.score(Grid.fromString(example)));
    }

    @Test
    void findReflectionWithVerticalFold() {
        final String example = """
                ..#.###
                #.#####
                #.#####
                ..#..##
                #......
                ##..###
                ..#....""";
        assertEquals(6, Thirteen.score(Grid.fromString(example)));
    }
}
