package net.digihippo.aoc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void examples() {
        assertGood("???.### 1,1,3", 1L);
        assertGood(".??..??...?##. 1,1,3", 4L);
        assertGood("?#?#?#?#?#?#?#? 1,3,1,6", 1L);
        assertGood("????.######..#####. 1,6,5", 4L);
        assertGood("?###???????? 3,2,1", 10L);
    }

    @Test
    void charCopy() {
        char[] copy = Twelve.copy(".....?...".toCharArray(), 5, '#');
        assertEquals("#...", new String(copy));
    }

    private static void assertGood(String line, long result) {
        Twelve.Thing thing = new Twelve().parse(line);
        assertEquals(result, Twelve.arrangements(new HashMap<>(), thing.springs(), 0, thing.working(), 0));
    }
}