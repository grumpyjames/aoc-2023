package net.digihippo.aoc;

import static org.junit.jupiter.api.Assertions.*;

class TwentyTwoTest extends CommonParserTestTemplate<Integer, TwentyTwo.Brick> {

    private static final String EXAMPLE_INPUT = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;

    protected TwentyTwoTest() {
        super(new TwentyTwo(), EXAMPLE_INPUT, 5, 5, "twentytwo.txt");
    }
}