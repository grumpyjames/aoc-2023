package net.digihippo.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nineteen extends SolutionTemplate<Integer, Long> {
    sealed interface Rule permits Expr, Dest {
        boolean matches(Map<Character, Integer> variable);

        String destination();

        Rule invert();

        void applyTo(Map<Character, InclusiveRange> ranges);
    }
    record Workflow(String name, List<Rule> rules) {
        public String route(Map<Character, Integer> variable) {
            for (Rule rule : rules) {
                if (rule.matches(variable)) {
                    return rule.destination();
                }
            }

            throw new IllegalStateException();
        }
    }

    enum Relation {
        Lt, Gt, Lte, Gte;

        public boolean accepts(int variableValue, int number) {
            switch (this) {

                case Lt -> {
                    return variableValue < number;
                }
                case Gt -> {
                    return variableValue > number;
                }
                case Lte -> {
                    return variableValue <= number;
                }
                case Gte -> {
                    return variableValue >= number;
                }
            }

            throw new IllegalStateException();
        }

        public InclusiveRange applyTo(Wide wide, int number) {
            return toRange(number).intersect(wide);
        }

        private Wide toRange(int number) {
            switch (this) {
                case Lt -> {
                    return new Wide(1, number - 1);
                }
                case Gt -> {
                    return new Wide(number + 1, 4000);
                }
                case Lte -> {
                    return new Wide(1, number);
                }
                case Gte -> {
                    return new Wide(number, 4000);
                }
            }

            throw new IllegalStateException();
        }

        public Relation not() {
            switch (this) {

                case Lt -> {
                    return Gte;
                }
                case Gt -> {
                    return Lte;
                }
                case Lte -> {
                    return Gt;
                }
                case Gte -> {
                    return Lt;
                }
            }
            throw new IllegalStateException();
        }
    }

    Relation parse(final String str) {
        switch (str) {
            case "<" -> {
                return Relation.Lt;
            }
            case ">" -> {
                return Relation.Gt;
            }
        }
        throw new IllegalStateException();
    }

    record Expr(char variableName, Relation relation, int number, String destination) implements Rule {
        @Override
        public boolean matches(Map<Character, Integer> variables) {
            int variableValue = variables.get(variableName);
            return relation.accepts(variableValue, number);
        }

        @Override
        public Rule invert() {
            return new Expr(variableName, relation.not(), number, destination);
        }

        @Override
        public void applyTo(Map<Character, InclusiveRange> ranges) {
            InclusiveRange inclusiveRange = ranges.get(variableName);
            if (inclusiveRange instanceof Wide wide) {
                ranges.put(variableName, relation.applyTo(wide, number));
            }
        }
    }

    record Dest(String destination) implements Rule {
        @Override
        public boolean matches(Map<Character, Integer> variable) {
            return true;
        }

        @Override
        public Rule invert() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void applyTo(Map<Character, InclusiveRange> ranges) {
            // no constraints here.
        }
    }

    @Override
    Solution<Integer> partOne() {
        return new Solution<>() {
            final Map<String, Workflow> flows = new HashMap<>();
            final List<Map<Character, Integer>> variables = new ArrayList<>();

            @Override
            public Integer result() {
                int result = 0;

                for (Map<Character, Integer> variable : variables) {
                    String location = "in";
                    while (!location.equals("A") && !location.equals("R")) {
                        Workflow w = flows.get(location);
                        location = w.route(variable);
                    }
                    if (location.equals("A")) {
                        result += variable.values().stream().mapToInt(Integer::intValue).sum();
                    }

                }

                return result;
            }

            boolean secondHalf = false;

            @Override
            public void accept(String s) {
                if (s.isBlank()) {
                    secondHalf = true;
                    return;
                }

                if (secondHalf) {
                    variables.add(parseVariables(s));
                } else {
                    Workflow workflow = parseWorkflow(s);
                    flows.put(workflow.name, workflow);
                }
            }
        };
    }

    sealed interface InclusiveRange permits Wide, Empty {
        long width();
    }
    record Empty() implements InclusiveRange {
        @Override
        public long width() {
            return 0;
        }
    }
    record Wide(int low, int high) implements InclusiveRange {
        public InclusiveRange intersect(Wide wide) {
            if (low > wide.high) {
                return new Empty();
            }

            if (wide.low > high) {
                return new Empty();
            }

            if (low >= wide.low && high <= wide.high) {
                return this;
            }

            if (wide.low >= low && wide.high <= high) {
                return wide;
            }

            if (high <= wide.high) {
                return new Wide(wide.low, high);
            }

            return new Wide(low, wide.high);
        }

        @Override
        public long width() {
            return (high - low) + 1;
        }
    }

    record Thing(List<Rule> soFar, Rule r) {
        public List<Rule> completeHere() {
            ArrayList<Rule> rules = new ArrayList<>(soFar);
            rules.add(r);

            return rules;
        }

        public Thing append(List<Rule> rules, Rule rule) {
            final List<Rule> newList = new ArrayList<>(soFar);
            newList.add(r);
            newList.addAll(rules);

            return new Thing(newList, rule);
        }
    }


    @Override
    Solution<Long> partTwo() {
        return new Solution<>() {
            final Map<String, Workflow> flows = new HashMap<>();
            final List<Map<Character, Integer>> variables = new ArrayList<>();

            @Override
            public Long result() {
                final List<List<Rule>> pathsToSuccess = new ArrayList<>();

                Workflow w = flows.get("in");
                Queue<Thing> rules = new ArrayDeque<>();
                for (int i = 0; i < w.rules.size(); i++) {
                    Rule rule = w.rules.get(i);
                    List<Rule> previous = w.rules.subList(0, i);

                    rules.add(new Thing(previous.stream().map(Rule::invert).toList(), rule));
                }

                while (!rules.isEmpty()) {
                    Thing thing = rules.poll();
                    String destination = thing.r.destination();
                    if (destination.equals("A")) {
                        pathsToSuccess.add(thing.completeHere());
                    } else if (!destination.equals("R")) {
                        Workflow workflow = flows.get(destination);
                        for (int i = 0; i < workflow.rules.size(); i++) {
                            Rule rule = workflow.rules.get(i);
                            List<Rule> previous = workflow.rules.subList(0, i);
                            List<Rule> inverted = previous.stream().map(Rule::invert).toList();
                            rules.add(thing.append(inverted, rule));
                        }
                    }
                }

                long result = 0;
                for (List<Rule> rule : pathsToSuccess) {
                    Map<Character, InclusiveRange> ranges = initialRanges();
                    for (Rule r : rule) {
                        r.applyTo(ranges);
                    }
                    long[] array = ranges.values().stream().mapToLong(InclusiveRange::width).toArray();
                    long product = 1;
                    for (long l : array) {
                        product *= l;
                    }

                    result += product;
                }

                return result;
            }

            boolean secondHalf = false;

            @Override
            public void accept(String s) {
                if (s.isBlank()) {
                    secondHalf = true;
                    return;
                }

                if (secondHalf) {
                    variables.add(parseVariables(s));
                } else {
                    Workflow workflow = parseWorkflow(s);
                    flows.put(workflow.name, workflow);
                }
            }
        };
    }

    private static Map<Character, InclusiveRange> initialRanges() {
        final Map<Character, InclusiveRange> ranges = new HashMap<>();
        ranges.put('x', new Wide(1, 4000));
        ranges.put('m', new Wide(1, 4000));
        ranges.put('a', new Wide(1, 4000));
        ranges.put('s', new Wide(1, 4000));

        return ranges;
    }

    private Map<Character, Integer> parseVariables(String str) {
        // {x=787,m=2655,a=1222,s=2876}

        final Map<Character, Integer> result = new HashMap<>();
        String[] variableArr = str.substring(1, str.length() - 1).split(",");
        for (String s : variableArr) {
            String[] split = s.split("=");
            result.put(split[0].charAt(0), Integer.parseInt(split[1]));
        }

        return result;
    }

    private Workflow parseWorkflow(String s) {
        String[] parts = s.split("\\{");
        String name = parts[0];
        String rules = parts[1];


        Pattern p = Pattern.compile("([xmas])([<>])([0-9]+):([ARa-z]+)");
        String[] ruleArr = rules.split(",");
        final List<Rule> rulesList = new ArrayList<>();
        for (String ruleStr : ruleArr) {
            Matcher matcher = p.matcher(ruleStr);
            if (matcher.matches()) {
                String variable = matcher.group(1);
                Relation relation = parse(matcher.group(2));
                int number = Integer.parseInt(matcher.group(3));
                String destination = matcher.group(4);
                rulesList.add(new Expr(variable.charAt(0), relation, number, destination));
            } else {
                rulesList.add(new Dest(ruleStr.replaceAll("}", "")));
            }
        }

        return new Workflow(name, rulesList);
    }
}
