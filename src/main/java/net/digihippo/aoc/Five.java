package net.digihippo.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Five extends SolutionTemplate<Long, Long> {

    enum Phase
    {
        Seeds,
        Maps
    }

    record Key(String from, String to) {}

    record MapRange(long destinationStart, long sourceStart, long rangeSize) {
        public Range applyTo(Range range, List<Range> result) {
            if (range.high < sourceStart) {
                // range is completely below us. We're finished.
                result.add(range);
                return null;
            } else if (range.low < sourceStart && range.high < sourceStart + rangeSize) {
                // we have overlap with some leftover at the low end.
                result.add(new Range(range.low, sourceStart - 1));

                long rangeSize = range.high - sourceStart;

                result.add(new Range(destinationStart, destinationStart + rangeSize - 1));
                return null;
            } else if (sourceStart <= range.low && range.high < sourceStart + rangeSize) {
                // we're fully encompassed
                long offset = range.low - sourceStart;

                result.add(new Range(destinationStart + offset, destinationStart + offset + (range.high - range.low)));
                return null;
            } else if (sourceStart <= range.low && range.low < sourceStart + rangeSize && sourceStart + rangeSize <= range.high) {
                // overlap at the high end with some leftover
                long offset = range.low - sourceStart;
                result.add(new Range(destinationStart + offset, destinationStart + rangeSize - 1));

                return new Range(sourceStart + rangeSize, range.high);
            } else if (sourceStart + rangeSize <= range.low) {
                // we're completely above
                return range;
            } else {
                // we completely encompass map range, with leftover above.
                result.add(new Range(range.low, sourceStart - 1));
                result.add(new Range(destinationStart, destinationStart + rangeSize - 1));

                return new Range(sourceStart + rangeSize, range.high);
            }
        }
    }

    @Override
    Solution<Long> partOne() {

        return new Solution<>() {
            Phase p = Phase.Seeds;

            private final List<Long> seedLocations = new ArrayList<>();
            private String mapFrom;
            private String mapTo;

            private final Map<Key, List<MapRange>> wtf = new HashMap<>();

            private final Pattern mapLine = Pattern.compile("([a-z]+)-to-([a-z]+) map:.*");

            private String searchKey = "seed";

            @Override
            public Long result() {
                final List<Long> locations = new ArrayList<>();
                for (long seedLocation : seedLocations) {
                    long term = seedLocation;
                    while (!searchKey.equals("location")) {
                        Key key = wtf.keySet().stream().filter(k -> k.from.equals(searchKey)).findFirst().orElseThrow();
                        List<MapRange> mapRanges = wtf.get(key);

                        term = find(mapRanges, term);
                        searchKey = key.to;
                    }

                    locations.add(term);
                    searchKey = "seed";
                }

                return locations.stream().mapToLong(Long::longValue).min().orElseThrow();
            }

            @Override
            public void accept(String s) {
                switch (p) {
                    case Seeds -> {
                        String[] locations = s.split(":")[1].split(" ");
                        for (String location : locations) {
                            if (!location.isBlank()) {
                                seedLocations.add(Long.parseLong(location));
                            }
                        }

                        p = Phase.Maps;
                    }
                    case Maps -> {
                        Matcher matcher = mapLine.matcher(s);
                        if (matcher.matches()) {
                            mapFrom = matcher.group(1);
                            mapTo = matcher.group(2);
                        } else if (!s.isBlank() && mapFrom != null) {
                            Key key = new Key(mapFrom, mapTo);
                            wtf.putIfAbsent(key, new ArrayList<>());

                            String[] bits = s.split(" ");

                            wtf.get(key).add(new MapRange(
                                    Long.parseLong(bits[0]),
                                    Long.parseLong(bits[1]),
                                    Long.parseLong(bits[2])
                            ));
                        }
                    }
                }
            }
        };

    }

    // inclusive
    record Range(long low, long high) {}

    private long find(List<MapRange> mapRanges, long term) {
        for (MapRange mapRange : mapRanges) {
            if (mapRange.sourceStart <= term && term < mapRange.sourceStart + mapRange.rangeSize) {
                long offset = term - mapRange.sourceStart;

                return mapRange.destinationStart + offset;
            }
        }

        return term;
    }

    @Override
    Solution<Long> partTwo() {

        return new Solution<>() {
            Phase p = Phase.Seeds;

            private final List<Long> seedLocations = new ArrayList<>();
            private String mapFrom;
            private String mapTo;

            private final Map<Key, List<MapRange>> wtf = new HashMap<>();

            private final Pattern mapLine = Pattern.compile("([a-z]+)-to-([a-z]+) map:.*");

            private String searchKey = "seed";

            @Override
            public Long result() {
                final List<List<Range>> everything = new ArrayList<>();


                wtf.values().forEach(l -> l.sort((o1, o2) -> Long.signum(o1.sourceStart - o2.sourceStart)));

                for (int i = 0; i < seedLocations.size(); i += 2) {
                    Range r = new Range(seedLocations.get(i), seedLocations.get(i) + seedLocations.get(i + 1) - 1);

                    List<Range> ranges = new ArrayList<>();
                    ranges.add(r);
                    while (!searchKey.equals("location")) {
                        Key key = wtf.keySet().stream().filter(k -> k.from.equals(searchKey)).findFirst().orElseThrow();
                        List<MapRange> mapRanges = wtf.get(key);

                        final List<Range> newRanges = new ArrayList<>();

                        for (Range range : ranges) {
                            Range unassigned = range;
                            for (MapRange mapRange : mapRanges) {
                                unassigned = mapRange.applyTo(unassigned, newRanges);
                                if (unassigned == null) {
                                    break;
                                }
                            }
                            if (unassigned != null) {
                                newRanges.add(unassigned);
                            }
                        }


                        ranges = newRanges;
                        searchKey = key.to;
                    }

                    everything.add(ranges);
                    searchKey = "seed";
                }

                return everything.stream().flatMap(Collection::stream).mapToLong(r -> r.low).min().orElseThrow();
            }

            @Override
            public void accept(String s) {
                switch (p) {
                    case Seeds -> {
                        String[] locations = s.split(":")[1].split(" ");
                        for (String location : locations) {
                            if (!location.isBlank()) {
                                seedLocations.add(Long.parseLong(location));
                            }
                        }

                        p = Phase.Maps;
                    }
                    case Maps -> {
                        Matcher matcher = mapLine.matcher(s);
                        if (matcher.matches()) {
                            mapFrom = matcher.group(1);
                            mapTo = matcher.group(2);
                        } else if (!s.isBlank() && mapFrom != null) {
                            Key key = new Key(mapFrom, mapTo);
                            wtf.putIfAbsent(key, new ArrayList<>());

                            String[] bits = s.split(" ");

                            wtf.get(key).add(new MapRange(
                                    Long.parseLong(bits[0]),
                                    Long.parseLong(bits[1]),
                                    Long.parseLong(bits[2])
                            ));
                        }
                    }
                }
            }
        };

    }
}
