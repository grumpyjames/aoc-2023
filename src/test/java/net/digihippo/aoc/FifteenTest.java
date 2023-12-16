package net.digihippo.aoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FifteenTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7""";
    public FifteenTest() {
        super(new Fifteen(), EXAMPLE_INPUT, 1320, 145, "fifteen.txt");
    }

    @Test
    void hashHash() {
        assertEquals(52, Fifteen.hash("HASH"));
    }
}