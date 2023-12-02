package net.digihippo.aoc;

public class Two extends SolutionTemplate<Integer, Integer> {
    @Override
    Solution<Integer> partOne() {
        return new Solution<Integer>() {
            @Override
            public Integer result() {
                return 2;
            }

            @Override
            public void accept(String s) {

            }
        };
    }

    @Override
    Solution<Integer> partTwo() {
        return new Solution<Integer>() {
            @Override
            public Integer result() {
                return 4;
            }

            @Override
            public void accept(String s) {

            }
        };
    }
}
