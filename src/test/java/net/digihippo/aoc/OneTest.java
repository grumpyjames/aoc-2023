package net.digihippo.aoc;

public class OneTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    private static final String EXAMPLE_TWO_INPUT = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;

    public OneTest() {
        super(new One(), EXAMPLE_INPUT, EXAMPLE_TWO_INPUT, 142, 281, "one.txt");
    }
}
