package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Two extends SolutionTemplate<Integer, Integer> {
    record Game(int id, List<List<Roll>> rolls) {}
    record Roll(int count, String colour) {}

    final Pattern piece = Pattern.compile(
            " ([0-9]+) (blue|red|green)([;,]?)");

    private Game parseGame(int id, String s) {
        Matcher matcher = piece.matcher(s.substring(7));

        List<List<Roll>> rollSet = new ArrayList<>();
        List<Roll> accumulating = new ArrayList<>();
        while (matcher.find())
        {
            int cubeCount =
                    Integer.parseInt(matcher.group(1));
            String cubeColour = matcher.group(2);

            accumulating.add(new Roll(cubeCount, cubeColour));

            boolean end = matcher.group(3).equals(";");
            if (end) {
                rollSet.add(accumulating);
                accumulating = new ArrayList<>();
            }
        }

        if (!accumulating.isEmpty()) {
            rollSet.add(accumulating);
        }

        return new Game(id, rollSet);
    }

    @Override
    Solution<Integer> partOne() {


        final List<Game> games = new ArrayList<>();

        return new Solution<>() {
            int id = 1;

            @Override
            public Integer result() {
                int validCount = 0;
                for (Game game : games) {
                    boolean valid = true;
                    for (List<Roll> rollSet : game.rolls) {
                        for (Roll roll : rollSet) {
                            if (roll.colour.equals("red") && roll.count > 12) {
                                valid = false;
                            }

                            if (roll.colour.equals("green") && roll.count > 13) {
                                valid = false;
                            }

                            if (roll.colour.equals("blue") && roll.count > 14) {
                                valid = false;
                            }
                        }
                    }
                    if (valid) {
                        validCount += game.id;
                    }
                }

                return validCount;
            }

            @Override
            public void accept(String s) {
                games.add(parseGame(id++, s));
            }
        };
    }

    @Override
    Solution<Integer> partTwo() {

        final List<Game> games = new ArrayList<>();

        return new Solution<>() {
            private int id = 1;

            @Override
            public Integer result() {
                int result = 0;

                for (Game game : games) {
                    int maxGreen = 0;
                    int maxRed = 0;
                    int maxBlue = 0;

                    for (List<Roll> roll : game.rolls) {
                        for (Roll r : roll) {
                            switch (r.colour) {
                                case "red" -> maxRed = Math.max(maxRed, r.count);
                                case "green" -> maxGreen = Math.max(maxGreen, r.count);
                                case "blue" -> maxBlue = Math.max(maxBlue, r.count);
                            }
                        }
                    }

                    result += (maxGreen * maxRed * maxBlue);
                }

                return result;
            }

            @Override
            public void accept(String s) {

                games.add(parseGame(id++, s));
            }
        };
    }
}
