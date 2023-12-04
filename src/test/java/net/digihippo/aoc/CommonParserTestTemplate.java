package net.digihippo.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class CommonParserTestTemplate<T, P> {
    protected final CommonParserSolutionTemplate<T, P> solution;
    protected final String exampleInput;
    protected final String exampleTwoInput;
    private final T exampleOneAnswer;
    protected final T exampleTwoAnswer;
    private final String puzzleFileName;

    protected CommonParserTestTemplate(
            CommonParserSolutionTemplate<T, P> solution,
            String exampleInput,
            T exampleOneAnswer,
            T exampleTwoAnswer,
            String puzzleFileName) {
        this.solution = solution;
        this.exampleInput = exampleInput;
        this.exampleTwoInput = exampleInput;
        this.exampleOneAnswer = exampleOneAnswer;
        this.exampleTwoAnswer = exampleTwoAnswer;
        this.puzzleFileName = puzzleFileName;
    }

    protected CommonParserTestTemplate(
            CommonParserSolutionTemplate<T, P> solution,
            String exampleInput,
            String exampleTwoInput,
            T exampleOneAnswer,
            T exampleTwoAnswer,
            String puzzleFileName) {
        this.solution = solution;
        this.exampleInput = exampleInput;
        this.exampleTwoInput = exampleTwoInput;
        this.exampleOneAnswer = exampleOneAnswer;
        this.exampleTwoAnswer = exampleTwoAnswer;
        this.puzzleFileName = puzzleFileName;
    }

    @Test
    void exampleOne()
    {
        assertEquals(exampleOneAnswer, solution.examplePartOne(exampleInput));
    }

    @Test
    void realOne() {
        T result = solution.puzzlePartOne(Inputs.puzzleInput(puzzleFileName));
        System.out.println(result);
    }

    @Test
    void exampleTwo() {
        assertEquals(exampleTwoAnswer, solution.examplePartTwo(exampleTwoInput));
    }

    @Test
    void realTwo() {
        System.out.println(solution.puzzlePartTwo(Inputs.puzzleInput(puzzleFileName)));
    }
}
