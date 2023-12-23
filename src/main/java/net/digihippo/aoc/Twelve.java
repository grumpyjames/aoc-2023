package net.digihippo.aoc;

import java.time.Instant;
import java.util.*;
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
            long mm = arrangements(answers, longer.springs, 0, longer.working, 0);

            result += mm;
            System.out.println(j + ": " + Instant.now() + " " + new String(longer.springs) + " " + longer.working + " => " + mm);
        }

        // 405098552693994 : too high.
        return result;
    }

    private static final class StateKey
    {
        private final char[] charArray;
        private final List<Integer> workingGroups;

        private StateKey(
                char[] ca,
                int offset,
                List<Integer> wg,
                int workingGroupIndex) {
            this.charArray = new char[ca.length - offset];
            System.arraycopy(ca, offset, this.charArray, 0, ca.length - offset);
            this.workingGroups = new ArrayList<>(wg.size() - workingGroupIndex);
            for (int i = workingGroupIndex; i < wg.size(); i++) {
                this.workingGroups.add(wg.get(i));
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StateKey stateKey = (StateKey) o;
            return Arrays.equals(charArray, stateKey.charArray) && workingGroups.equals(stateKey.workingGroups);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(workingGroups);
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
        char c = springs[charIndex];
        if (c == '.') {
            return arrangements(answers, springs, charIndex + 1, working, workingGroupIndex);
        }
        if (c == '#') {
            // case 1: we're not in a group
            Integer groupSize = working.get(workingGroupIndex);
            boolean ok = true;
            for (int i = charIndex; i < charIndex + groupSize; i++) {
                boolean good = charEquals(springs, '#', i) || charEquals(springs, '?', i);
                if (!good) {
                    ok = false;
                    break;
                }
            }

            ok &= !charEquals(springs, '#', charIndex + groupSize);

            if (ok) {
                return arrangements(
                        answers,
                        springs,
                        charIndex + groupSize + 1, // skip over the ?|.
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

    record Thing(char[] springs, List<Integer> working) {

    }
}
