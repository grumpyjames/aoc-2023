package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Twelve extends CommonParserSolutionTemplate<Long, Twelve.Thing> {
    @Override
    Thing parse(String line) {
        String[] parts = line.split(" ");
        return new Thing(parts[0], Arrays.stream(parts[1].split(",")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    @Override
    Long partOne(List<Thing> parsed) {
        // ok - let's go for something like this.
        // pick the biggest one - that's the hardest to fit. pin where that can be, then work your way out?

        long result = 0;
        for (Thing thing : parsed) {
            result += thing.ways();
        }

        return result;
    }

    @Override
    Long partTwo(List<Thing> parsed) {
        return null;
    }

    private static final class MutableCharSequence implements CharSequence
    {
        private char[] array;


        void set(char[] array) {
            this.array = array;
        }

        @Override
        public int length() {
            return array.length;
        }

        @Override
        public char charAt(int index) {
            return array[index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            throw new UnsupportedOperationException();
        }
    }

    record Thing(String arrangement, List<Integer> working) {
        public long ways() {

            long result = 0;

            final String regex = regexStr(working);

            final Pattern p = Pattern.compile(regex);

            final char[] springs = arrangement.toCharArray();
            final List<Integer> unknown = new ArrayList<>();
            for (int i = 0; i < springs.length; i++) {
                char spring = springs[i];
                if (spring == '?') {
                    unknown.add(i);
                }
            }

            int combinations = 1 << unknown.size();
            final MutableCharSequence mcs = new MutableCharSequence();
            mcs.set(springs);
            for (int i = 0; i < combinations; i++) {
                // treat i as a bitmask
                for (int j = 0; j < unknown.size(); j++) {
                    char s = (i & 1 << j) > 0 ? '#' : '.';
                    springs[unknown.get(j)] = s;
                }

                boolean matches = p.matcher(mcs).matches();
                if (matches) {
                    result++;
                }
            }

            return result;
        }

        private String regexStr(List<Integer> working) {
            final StringBuilder sb = new StringBuilder();

            sb.append("\\.*");


            for (int count : working) {
                sb.append("#".repeat(Math.max(0, count)));
                sb.append("[\\.]+");
            }

            sb.delete(sb.length() - 5, sb.length());
            sb.append("\\.*");

            return sb.toString();
        }

    }
}
