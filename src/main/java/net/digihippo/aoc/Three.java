package net.digihippo.aoc;

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
                final Grid g = new Grid(grid);

                final List<Integer> nope = new ArrayList<>();
                final List<Integer> partNumbers = new ArrayList<>();

                g.visit(new Grid.RowVisitor() {
                    int start = -1;
                    int end = -1;

                    @Override
                    public void rowStarted(int y) {

                    }

                    @Override
                    public void rowDone(int y) {
                        maybeRunNeighbourAnalysis(y);
                    }

                    private void maybeRunNeighbourAnalysis(int y) {
                        if (start != -1) {
                            processGroup(nope, partNumbers, g, y, start, end);

                            start = -1;
                            end = -1;
                        }
                    }

                    @Override
                    public void onCell(int x, int y, char content) {
                        if ('0' <= content && content <= '9')
                        {
                            if (start == -1)
                            {
                                start = x;
                            }

                            end = x;
                        }
                        else
                        {
                            maybeRunNeighbourAnalysis(y);
                        }
                    }
                });

                int result = 0;
                for (int partNumber : partNumbers) {
                    result += partNumber;
                }

                return result;
            }

            private void processGroup(List<Integer> nope, List<Integer> partNumbers, Grid g, int y, int start, int end) {
                SymbolVisitor v = new SymbolVisitor();
                g.visitNeighboursOfXRange(start, end, y, v);

                if (v.noSymbols) {
                    nope.add(Integer.parseInt(g.substring(start, end + 1, y)));
                } else {
                    partNumbers.add(Integer.parseInt(g.substring(start, end + 1, y)));
                }
            }

            @Override
            public void accept(String s) {
                grid.add(s);
            }
        };
    }

    private boolean symbolic(char c) {
        return c != '.' && (c < '0' || c > '9');
    }

    @Override
    Solution<Integer> partTwo() {
        return new Solution<>() {
            private final List<String> grid = new ArrayList<>();

            @Override
            public Integer result() {
                final Grid g = new Grid(grid);

                g.visit(new Grid.RowVisitor() {
                    int start = -1;
                    int end = -1;

                    @Override
                    public void rowStarted(int y) {

                    }

                    @Override
                    public void rowDone(int y) {
                        if (start != -1) {
                            processGroup(g, y, start, end);

                            start = -1;
                            end = -1;
                        }
                    }

                    @Override
                    public void onCell(int x, int y, char b) {
                        if ('0' <= b && b <= '9') {
                            if (start == -1) {
                                start = x;
                            }

                            end = x;
                        } else {
                            if (start != -1) {
                                processGroup(g, y, start, end);

                                start = -1;
                                end = -1;
                            }
                        }
                    }
                });

                int gearRatio = 0;
                for (Map.Entry<TwoDPoint, List<Integer>> entry : gears.entrySet()) {
                    if (entry.getValue().size() == 2) {
                        gearRatio += entry.getValue().get(0) * entry.getValue().get(1);
                    }
                }

                return gearRatio;
            }

            private final Map<TwoDPoint, List<Integer>> gears = new HashMap<>();


            private void processGroup(Grid g, int y, int start, int end) {
                int part = Integer.parseInt(g.substring(start, end + 1, y));

                g.visitNeighboursOfXRange(start, end, y, (x, y1, content) -> {
                    if (content == '*') {
                        TwoDPoint gearPoint = new TwoDPoint(x, y1);
                        gears.putIfAbsent(gearPoint, new ArrayList<>());
                        gears.get(gearPoint).add(part);
                    }
                });
            }

            @Override
            public void accept(String s) {
                grid.add(s);
            }
        };
    }

    private class SymbolVisitor implements Grid.Visitor {
        boolean noSymbols = true;

        @Override
        public void onCell(int x, int y, char content) {
            if (symbolic(content)) {
                noSymbols = false;
            }
        }
    }
}
