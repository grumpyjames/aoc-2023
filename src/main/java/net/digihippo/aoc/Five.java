package net.digihippo.aoc;

import java.util.List;

public class Five extends CommonParserSolutionTemplate<Integer, Five.Parsed> {
    @Override
    Parsed parse(String line) {
        return new Parsed();
    }

    @Override
    Integer partOne(List<Parsed> parsed) {
        return 34;
    }

    @Override
    Integer partTwo(List<Parsed> parsed) {
        return 34;
    }

    public record Parsed() {}
}
