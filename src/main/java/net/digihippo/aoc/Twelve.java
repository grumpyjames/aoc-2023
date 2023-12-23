package net.digihippo.aoc;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Twelve extends CommonParserSolutionTemplate<Long, Twelve.Thing> {
    @Override
    Thing parse(String line) {
        String[] parts = line.split(" ");
        String part = parts[0];
        return new Thing(
                part.toCharArray(),
                Arrays.stream(parts[1].split(",")).map(Integer::parseInt).collect(Collectors.toList())
        );
    }

    @Override
    Long partOne(List<Thing> parsed) {
        // ok - let's go for something like this.
        // pick the biggest one - that's the hardest to fit. pin where that can be, then work your way out?
        long result = 0;
        for (Thing thing : parsed) {
            result += arrangements(new HashMap<>(), thing.springs, 0, thing.working, 0);
        }

        return result;
    }

    @Override
    Long partTwo(List<Thing> parsed) {
        long result = 0;
        HashMap<StateKey, Long> answers = new HashMap<>();
        for (int j = 0; j < parsed.size(); j++) {
            Thing thing = parsed.get(j);
            StringBuilder stringBuilder = new StringBuilder(new String(thing.springs));
            List<Integer> groups = new ArrayList<>(thing.working);
            for (int i = 0; i < 4; i++) {
                stringBuilder.append('?');
                stringBuilder.append(new String(thing.springs));
                groups.addAll(thing.working);
            }

            Thing longer = new Thing(stringBuilder.toString().toCharArray(), groups);
            result += arrangements(answers, longer.springs, 0, longer.working, 0);
            System.out.println(j + ": " + Instant.now());
        }

        return result;
    }

    private static final class StateKey
    {
        private final char[] charArray;
        private final int offset;

        private final List<Integer> workingGroups;
        private final int workingGroupIndex;

        private StateKey(char[] charArray, int offset, List<Integer> workingGroups, int workingGroupIndex) {
            this.charArray = charArray;
            this.offset = offset;
            this.workingGroups = workingGroups;
            this.workingGroupIndex = workingGroupIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateKey stateKey = (StateKey) o;
            return offset == stateKey.offset && workingGroupIndex == stateKey.workingGroupIndex && Arrays.equals(charArray, stateKey.charArray) && Objects.equals(workingGroups, stateKey.workingGroups);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(offset, workingGroups, workingGroupIndex);
            result = 31 * result + Arrays.hashCode(charArray);
            return result;
        }
    }

    static long arrangements(
            Map<StateKey, Long> answers,
            char[] springs,
            int charIndex,
            List<Integer> working,
            int workingGroupIndex) {
        StateKey stateKey = new StateKey(springs, charIndex, working, workingGroupIndex);
        Long answer = answers.get(stateKey);
        if (answer != null) {
            return answer;
        }

        long oof = actuallyComputeIt(answers, springs, charIndex, working, workingGroupIndex);
        answers.put(stateKey, oof);

        return oof;
    }

    private static long actuallyComputeIt(Map<StateKey, Long> answers, char[] springs, int charIndex, List<Integer> working, int workingGroupIndex) {
        if (charIndex >= springs.length) { // no more material to work with
            return workingGroupIndex == working.size() ? 1 : 0;
        }

        if (workingGroupIndex >= working.size()) { // no more groups
            boolean anySharp = false;
            for (int i = charIndex; i < springs.length; i++) {
                if (springs[i] == '#') {
                    anySharp = true;
                    break;
                }
            }
            if (anySharp) {
                return 0;
            }
        }

        char c = springs[charIndex];
        if (c == '.') {
            return arrangements(answers, springs, charIndex + 1, working, workingGroupIndex);
        }
        if (c == '#') {
            // case 1: we're not in a group
            Integer groupSize = working.get(workingGroupIndex);
            boolean ok = true;
            for (int i = charIndex; i < charIndex + groupSize; i++) {
                if (charEquals(springs, '.', i)) {
                    ok = false;
                    break;
                }
            }

            ok &= !charEquals(springs, '#', charIndex + groupSize);

            if (ok) {
                return arrangements(
                        answers,
                        springs,
                        charIndex + groupSize + 1,
                        working,
                        workingGroupIndex + 1);
            } else {
                return 0;
            }
        }
        if (c == '?') {
            return arrangements(answers, copy(springs, charIndex, '.'), 0, working, workingGroupIndex) +
                    arrangements(answers, copy(springs, charIndex, '#'), 0, working, workingGroupIndex);
        }

        throw new UnsupportedOperationException();
    }

    static boolean charEquals(char[] arr, char needle, int offset)
    {
        return offset < arr.length && arr[offset] == needle;
    }

    static char[] copy(char[] src, int srcStart, char first)
    {
        final char[] result = new char[src.length - srcStart];
        System.arraycopy(src, srcStart, result, 0, src.length - srcStart);
        result[0] = first;
        return result;
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

    record Thing(char[] springs, List<Integer> working) {
        public long ways() {

            long result = 0;

            final String regex = regexStr(working);

            final Pattern p = Pattern.compile(regex);
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
