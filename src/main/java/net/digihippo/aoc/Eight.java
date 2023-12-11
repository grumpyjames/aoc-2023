package net.digihippo.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Eight extends SolutionTemplate<Integer, Long> {
    enum Direction {
        Left,
        Right
    }

    record Location(String name, String toTheLeft, String toTheRight) {}

    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
            private boolean first = true;
            private final List<Direction> directions = new ArrayList<>();
            private final Map<String, Location> locations = new HashMap<>();

            @Override
            public Integer result() {
                int steps = 0;
                String location = "AAA";
                while (!location.equals("ZZZ")) {
                    Location here = locations.get(location);

                    Direction direction = directions.get(steps % directions.size());

                    if (direction == Direction.Left) {
                        location = here.toTheLeft;
                    } else {
                        location = here.toTheRight;
                    }
                    ++steps;
                }

                return steps;
            }

            private final Pattern p = Pattern.compile("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)");

            @Override
            public void accept(String s) {
                if (first) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c == 'L') {
                            directions.add(Direction.Left);
                        } else {
                            directions.add(Direction.Right);
                        }
                    }
                    first = false;
                } else if (!s.isBlank()) {
                    Matcher matcher = p.matcher(s);
                    if (matcher.find()) {
                        Location l = new Location(matcher.group(1), matcher.group(2), matcher.group(3));
                        locations.put(l.name, l);
                    } else {
                        throw new IllegalStateException(s);
                    }

                }
            }
        };
    }

    @Override
    Solution<Long> partTwo() {
        return new Solution<>() {
            private boolean first = true;
            private final List<Direction> directions = new ArrayList<>();
            private final Map<String, Location> locations = new HashMap<>();

            @Override
            public Long result() {
                final List<Integer> periods = new ArrayList<>(); // let's assume they're all periodic
                List<String> ls = locations.keySet().stream().filter(l -> l.endsWith("A")).toList();

                for (String l : ls) {
                    int steps = 0;
                    String h = l;

                    while (!h.endsWith("Z")) {
                        Location here = locations.get(h);
                        Direction direction = directions.get(steps % directions.size());

                        if (direction == Direction.Left) {
                            h = here.toTheLeft;
                        } else {
                            h = here.toTheRight;
                        }

                        ++steps;
                    }

                    periods.add(steps);
                }

                return Prime.lowestCommonMultiple(periods);
            }

            private final Pattern p = Pattern.compile("([A-Z|1-9]+) = \\(([A-Z|1-9]+), ([A-Z|1-9]+)\\)");

            @Override
            public void accept(String s) {
                if (first) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c == 'L') {
                            directions.add(Direction.Left);
                        } else {
                            directions.add(Direction.Right);
                        }
                    }
                    first = false;
                } else if (!s.isBlank()) {
                    Matcher matcher = p.matcher(s);
                    if (matcher.find()) {
                        Location l = new Location(matcher.group(1), matcher.group(2), matcher.group(3));
                        locations.put(l.name, l);
                    } else {
                        throw new IllegalStateException(s);
                    }
                }
            }
        };
    }
}
