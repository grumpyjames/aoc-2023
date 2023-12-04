package net.digihippo.aoc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CommonParserSolutionTemplate<T, P> {
    abstract P parse(String line);

    abstract T partOne(List<P> parsed);

    abstract T partTwo(List<P> parsed);

    T examplePartOne(String input) {
        return example(input, this::partOne);
    }

    T puzzlePartOne(InputStream puzzleInput)
    {
        return puzzle(puzzleInput, this::partOne);
    }

    T examplePartTwo(String example)
    {
        return example(example, this::partTwo);
    }

    T puzzlePartTwo(InputStream puzzleInput)
    {
        return puzzle(puzzleInput, this::partTwo);
    }

    private <X> X example(String input, Function<List<P>, X> f) {
        String[] lines = input.split("\n");
        List<P> parsed = new ArrayList<>();
        for (String line : lines) {
            P p = parse(line);
            parsed.add(p);
        }
        return f.apply(parsed);
    }

    private <X> X puzzle(
            InputStream puzzleInput,
            Function<List<P>, X> f) {
        final List<P> parsed = new ArrayList<>();
        try {
            Lines.processLines(puzzleInput, s -> parsed.add(this.parse(s)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return f.apply(parsed);
    }
}
