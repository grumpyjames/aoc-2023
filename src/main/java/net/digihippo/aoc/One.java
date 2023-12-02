package net.digihippo.aoc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class One extends SolutionTemplate<Integer, Integer> {
    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
            private final List<Integer> nums = new ArrayList<>();

            @Override
            public Integer result() {
                return nums.stream().mapToInt(Integer::intValue).sum();
            }

            @Override
            public void accept(String s) {
                int first = -1;
                int last = -1;
                for (byte b : s.getBytes(StandardCharsets.US_ASCII)) {
                    if ('0' <= b && b <= '9') {
                        int num = b - (byte) '0';
                        if (first == -1) {
                            first = num;
                        }

                        last = num;
                    }
                }

                nums.add((10 * first) + last);
            }
        };
    }

    @Override
    Solution<Integer> partTwo() {
        return new Solution<>() {
            private final List<Integer> nums = new ArrayList<>();
            private final Pattern p =
                    Pattern.compile(
                            "(one|two|three|four|five|six|seven|eight|nine|" +
                                    "[0-9])"
                    );

            private final Pattern q =
                    Pattern.compile(
                            "(eno|owt|eerht|ruof|evif|xis|neves|thgie|enin|" +
                                    "[0-9])"
                    );

            @Override
            public Integer result() {
                return nums.stream().mapToInt(Integer::intValue).sum();
            }

            @Override
            public void accept(String s) {
                int result = parse(s);
                nums.add(result);
            }

            private int parse(String s) {
                Matcher matcher = p.matcher(s);

                int first = -1;
                int last = -1;
                if (matcher.find()) {
                    first = toInt(matcher.group());
                    last = first;
                }

                StringBuilder reverse = new StringBuilder(s).reverse();

                Matcher m2 = q.matcher(reverse.toString());
                if (m2.find()) {
                    last = toInt(new StringBuilder(m2.group()).reverse().toString());
                }

                int result = (first * 10) + last;
                System.out.println(first + " " + last + " " + result + " : " + s);
                return result;
            }

            private Integer toInt(String group) {
                return switch (group) {
                    case "one" -> 1;
                    case "two" -> 2;
                    case "three" -> 3;
                    case "four" -> 4;
                    case "five" -> 5;
                    case "six" -> 6;
                    case "seven" -> 7;
                    case "eight" -> 8;
                    case "nine" -> 9;
                    default -> group.charAt(0) - '0';
                };
            }
        };
    }
}
