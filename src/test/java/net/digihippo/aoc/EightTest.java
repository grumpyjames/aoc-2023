package net.digihippo.aoc;

class EightTest extends TestTemplate<Integer, Long> {


    private static final String EXAMPLE_INPUT = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)""";
    private static final String EXAMPLE_TWO_INPUT = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)""";

    EightTest() {
        super(new Eight(), EXAMPLE_INPUT, EXAMPLE_TWO_INPUT, 2, 6L, "eight.txt");
    }

}