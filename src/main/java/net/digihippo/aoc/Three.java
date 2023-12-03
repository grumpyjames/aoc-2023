package net.digihippo.aoc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Three extends SolutionTemplate<Integer, Integer> {
    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
            private final List<String> grid = new ArrayList<>();

            @Override
            public Integer result() {
                final List<Integer> nope = new ArrayList<>();
                final List<Integer> partNumbers = new ArrayList<>();

                for (int i = 0; i < grid.size(); i++) {
                    String row = grid.get(i);
                    byte[] bytes = row.getBytes(StandardCharsets.US_ASCII);
                    int start = -1;
                    int end = -1;


                    for (int j = 0; j < bytes.length; j++) {
                        byte b = bytes[j];
                        if ('0' <= b && b <= '9')
                        {
                            if (start == -1)
                            {
                                start = j;
                            }

                            end = j;
                        }
                        else
                        {
                            if (start != -1) {
                                processGroup(nope, partNumbers, i, row, start, end);

                                start = -1;
                                end = -1;
                            }
                        }
                    }

                    if (start != -1)
                    {
                        processGroup(nope, partNumbers, i, row, start, end);
                    }
                }

                int result = 0;
                for (int partNumber : partNumbers) {
                    result += partNumber;
                }

                return result;
            }

            private void processGroup(List<Integer> nope, List<Integer> partNumbers, int i, String row, int start, int end) {
                boolean noSymbols = true;
                for (int x = start - 1; x <= end + 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (inGrid(x, y + i, grid) && symbolic(x, y + i, grid)) {
                            noSymbols = false;
                        }
                    }
                }

                if (noSymbols) {
                    nope.add(Integer.parseInt(row.substring(start, end + 1)));
                } else {
                    partNumbers.add(Integer.parseInt(row.substring(start, end + 1)));
                }
            }

            @Override
            public void accept(String s) {
                grid.add(s);
            }
        };
    }

    private boolean symbolic(int x, int y, List<String> grid) {
        char c = grid.get(y).charAt(x);
        return c != '.' && (c < '0' || c > '9');
    }

    private boolean gear(int x, int y, List<String> grid) {
        return '*' == grid.get(y).charAt(x);
    }

    private boolean inGrid(int x, int y, List<String> grid) {
        return (0 <= y && y < grid.size()) && (0 <= x && x < grid.get(0).length());
    }


    @Override
    Solution<Integer> partTwo() {
        return new Solution<>() {
            private final List<String> grid = new ArrayList<>();

            @Override
            public Integer result() {
                for (int i = 0; i < grid.size(); i++) {
                    String row = grid.get(i);
                    byte[] bytes = row.getBytes(StandardCharsets.US_ASCII);
                    int start = -1;
                    int end = -1;


                    for (int j = 0; j < bytes.length; j++) {
                        byte b = bytes[j];
                        if ('0' <= b && b <= '9') {
                            if (start == -1) {
                                start = j;
                            }

                            end = j;
                        } else {
                            if (start != -1) {
                                processGroup(i, row, start, end);

                                start = -1;
                                end = -1;
                            }
                        }
                    }

                    if (start != -1) {
                        processGroup(i, row, start, end);
                    }
                }

                int gearRatio = 0;
                for (Map.Entry<TwoDPoint, List<Integer>> entry : gears.entrySet()) {
                    if (entry.getValue().size() == 2) {
                        gearRatio += entry.getValue().get(0) * entry.getValue().get(1);
                    }
                }

                return gearRatio;
            }

            private final Map<TwoDPoint, List<Integer>> gears = new HashMap<>();


            private void processGroup(int i, String row, int start, int end) {
                int part = Integer.parseInt(row.substring(start, end + 1));

                for (int x = start - 1; x <= end + 1; x++) {
                    for (int yOff = -1; yOff <= 1; yOff++) {
                        int y = yOff + i;
                        if (inGrid(x, y, grid) && gear(x, y, grid)) {
                            TwoDPoint gearPoint = new TwoDPoint(x, y);
                            gears.putIfAbsent(gearPoint, new ArrayList<>());
                            gears.get(gearPoint).add(part);
                        }
                    }
                }
            }

            @Override
            public void accept(String s) {
                grid.add(s);
            }
        };
    }
}
