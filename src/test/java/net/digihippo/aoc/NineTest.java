package net.digihippo.aoc;

import java.util.List;

class NineTest extends CommonParserTestTemplate<Long, List<Long>> {
    private static final String EXAMPLE_INPUT = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45""";

    public NineTest() {
        super(new Nine(), EXAMPLE_INPUT, 114L, 2L, "nine.txt");
    }

}