package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Nine extends CommonParserSolutionTemplate<Long, List<Long>> {
    @Override
    List<Long> parse(String line) {
        return Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
    }

    @Override
    Long partOne(List<List<Long>> parsed) {
        long answer = 0L;

        for (List<Long> sequence : parsed) {
            List<Long> rightmost = new ArrayList<>();
            rightmost.add(sequence.get(sequence.size() - 1));
            List<Long> input = new ArrayList<>(sequence);

            while (!allZero(input)) {
                if (input.size() == 0) {
                    throw new IllegalStateException();
                }

                final List<Long> differences = new ArrayList<>(sequence.size() - 1);
                for (int i = 0; i < input.size() - 1; i++) {
                    differences.add(input.get(i + 1) - input.get(i));
                }
                rightmost.add(differences.get(differences.size() - 1));
                input = differences;
            }

            answer += rightmost.stream().mapToLong(Long::longValue).sum();
        }

        return answer;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean allZero(List<Long> input) {
        if (input.size() == 0) {
            return true;
        }

        long first = input.get(0);
        if (first != 0) {
            return false;
        }

        for (int i = 1; i < input.size(); i++) {
             if (input.get(i) != first) {
                 return false;
             }

        }

        return true;
    }

    @Override
    Long partTwo(List<List<Long>> parsed) {
        long answer = 0L;

        for (List<Long> sequence : parsed) {
            List<Long> leftMost = new ArrayList<>();
            leftMost.add(sequence.get(0));
            List<Long> input = new ArrayList<>(sequence);

            while (!allZero(input)) {
                if (input.size() == 0) {
                    throw new IllegalStateException();
                }

                final List<Long> differences = new ArrayList<>(sequence.size() - 1);
                for (int i = 0; i < input.size() - 1; i++) {
                    differences.add(input.get(i + 1) - input.get(i));
                }
                leftMost.add(differences.get(0));
                input = differences;
            }

            long result = leftMost.get(leftMost.size() - 1);
            for (int i = leftMost.size() - 2; i >= 0; i--) {
                 long hmm = leftMost.get(i);
                 result = hmm - result;
            }

            answer += result;
        }

        return answer;
    }
}
