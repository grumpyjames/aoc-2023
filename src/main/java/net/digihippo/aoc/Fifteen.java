package net.digihippo.aoc;

import java.util.*;

public class Fifteen extends SolutionTemplate<Integer, Integer> {
    static int hash(String s) {
        int value = 0;
        for (char c : s.toCharArray()) {
            value += c;
            value *= 17;
            value %= 256;
        }

        return value;
    }

    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
            private final List<String> parts = new ArrayList<>();

            @Override
            public Integer result() {
                return parts.stream().mapToInt(Fifteen::hash).sum();
            }

            @Override
            public void accept(String s) {
                String[] spl = s.split(",");
                parts.addAll(Arrays.asList(spl));
            }
        };
    }

    record Lens(String label, int focalLength) {}

    @Override
    Solution<Integer> partTwo() {
        return new Solution<>() {
            @SuppressWarnings("unchecked")
            private final List<Lens>[] boxes = new List[256];

            private final List<String> parts = new ArrayList<>();


            @Override
            public Integer result() {
                for (String part : parts) {

                    if (part.endsWith("-")) {
                        final String label = part.substring(0, part.length() - 1);
                        int boxIndex = hash(label);
                        final List<Lens> box = ensureBoxExists(boxIndex);
                        box.removeIf(l -> l.label.equals(label));

                    } else {
                        final String[] split = part.split("=");
                        final String label = split[0];
                        final int focalLength = Integer.parseInt(split[1]);
                        int boxIndex = hash(label);
                        final List<Lens> box = ensureBoxExists(boxIndex);

                        int match = -1;
                        for (int i = 0; i < box.size(); i++) {
                            Lens lens = box.get(i);
                            if (lens.label.equals(label)) {
                                match = i;
                                break;
                            }
                        }
                        if (match != -1) {
                            box.remove(match);
                            box.add(match, new Lens(label, focalLength));
                        } else {
                            box.add(new Lens(label, focalLength));
                        }
                    }
                }

                int result = 0;
                for (int i = 0; i < boxes.length; i++) {
                    List<Lens> box = boxes[i];
                    if (box != null) {
                        for (int j = 0; j < box.size(); j++) {
                            Lens lens = box.get(j);
                            result += ((i + 1) * (j + 1) * lens.focalLength);
                        }
                    }
                }
                return result;
            }

            private List<Lens> ensureBoxExists(int boxIndex) {
                if (boxes[boxIndex] == null) {
                    boxes[boxIndex] = new ArrayList<>();
                }

                return boxes[boxIndex];
            }

            @Override
            public void accept(String s) {
                String[] spl = s.split(",");
                parts.addAll(Arrays.asList(spl));
            }
        };
    }
}
