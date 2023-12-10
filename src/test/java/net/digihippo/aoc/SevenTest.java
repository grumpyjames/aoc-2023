package net.digihippo.aoc;

class SevenTest extends CommonParserTestTemplate<Integer, Seven.Hand> {

    private static final String EXAMPLE_INPUT = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483""";

    public SevenTest() {
        super(new Seven(), EXAMPLE_INPUT, 6440, 5905, "seven.txt");
    }
}