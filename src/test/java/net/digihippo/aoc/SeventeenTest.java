package net.digihippo.aoc;

public class SeventeenTest extends TestTemplate<Integer, Integer> {
    private static final String EXAMPLE_INPUT = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533""";

    SeventeenTest() {
        super(new Seventeen(), EXAMPLE_INPUT, 102, 71, "seventeen.txt");
    }
}
