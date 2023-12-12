package net.digihippo.aoc;

class TwelveTest extends CommonParserTestTemplate<Long, Twelve.Thing> {
    private static final String EXAMPLE_INPUT = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1""";


    public TwelveTest() {
        super(new Twelve(), EXAMPLE_INPUT, 21L, 525152L, "twelve.txt");
    }

}