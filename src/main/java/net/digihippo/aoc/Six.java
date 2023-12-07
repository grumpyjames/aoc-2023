package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;

public class Six extends SolutionTemplate<Long, Long> {

    @Override
    Solution<Long> partOne() {
        return new Solution<>() {
            boolean first = true;

            private final List<Long> times = new ArrayList<>();
            private final List<Long> distances = new ArrayList<>();

            @Override
            public Long result() {
                final List<Long> ways = new ArrayList<>();
                for (int i = 0; i < times.size(); i++) {
                    final long time = times.get(i);
                    final long distance = distances.get(i);
                    long waysToWin = 0;
                    for (int j = 0; j < time; j++) {
                        // j is the point at which we stop holding the button.
                        long travelled = (time - j) * (j);
                        if (travelled > distance)
                        {
                            waysToWin++;
                        }
                    }
                    ways.add(waysToWin);
                }


                long result = 1;
                for (Long way : ways) {
                    result *= way;
                }
                return result;
            }


            @Override
            public void accept(String s) {
                if (first) {
                    String[] timeStrs = s.split("\\s+");
                    for (int i = 1; i < timeStrs.length; i++) {
                        String timeStr = timeStrs[i];
                        times.add(Long.parseLong(timeStr));
                    }
                    first = false;
                } else {
                    String[] distanceStrs = s.split("\\s+");
                    for (int i = 1; i < distanceStrs.length; i++) {
                        String distanceStr = distanceStrs[i];
                        distances.add(Long.parseLong(distanceStr));
                    }
                }
            }
        };
    }

    @Override
    Solution<Long> partTwo() {
        return new Solution<>() {
            private long time;
            private long distance;

            boolean first = true;

            @Override
            public Long result() {
                long waysToWin = 0;
                for (int j = 0; j < time; j++) {
                    // j is the point at which we stop holding the button.
                    long travelled = (time - j) * (j);
                    if (travelled > distance)
                    {
                        waysToWin++;
                    }
                }

                return waysToWin;
            }

            @Override
            public void accept(String s) {
                if (first) {
                    String[] timeStrs = s.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < timeStrs.length; i++) {
                        String timeStr = timeStrs[i];
                        sb.append(timeStr);
                    }
                    time = Long.parseLong(sb.toString());
                    first = false;
                } else {
                    String[] distanceStrs = s.split("\\s+");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < distanceStrs.length; i++) {
                        String distanceStr = distanceStrs[i];

                        sb.append(distanceStr);
                    }
                    distance = Long.parseLong(sb.toString());
                }
            }
        };
    }
}
