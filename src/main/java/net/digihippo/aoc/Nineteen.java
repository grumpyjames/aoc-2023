package net.digihippo.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nineteen extends SolutionTemplate<Integer, Integer> {
    sealed interface Rule permits Expr, Dest {
        boolean matches(Map<Character, Integer> variable);

        String destination();

        Rule invert();
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
        Lt, Gt;

        public boolean accepts(int variableValue, int number) {
            switch (this) {

                case Lt -> {
                    return variableValue < number;
                }
                case Gt -> {
                    return variableValue > number;
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

    record Expr(char variableName, Relation relation, int number, String destination, boolean not) implements Rule {
        @Override
        public boolean matches(Map<Character, Integer> variables) {
            int variableValue = variables.get(variableName);
            return not != relation.accepts(variableValue, number);
        }

        @Override
        public Rule invert() {
            return new Expr(variableName, relation, number, destination, !not);
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
    Solution<Integer> partTwo() {
        return new Solution<>() {
            final Map<String, Workflow> flows = new HashMap<>();
            final List<Map<Character, Integer>> variables = new ArrayList<>();

            @Override
            public Integer result() {
                final List<List<Rule>> pathsToSuccess = new ArrayList<>();
                final List<List<Rule>> pathsToFailure = new ArrayList<>();

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
                    } else if (destination.equals("R")) {
                        pathsToFailure.add(thing.completeHere());
                    } else {
                        Workflow workflow = flows.get(destination);
                        for (int i = 0; i < workflow.rules.size(); i++) {
                            Rule rule = workflow.rules.get(i);
                            List<Rule> previous = workflow.rules.subList(0, i);
                            List<Rule> inverted = previous.stream().map(Rule::invert).toList();
                            rules.add(thing.append(inverted, rule));
                        }
                    }
                }


                return 35;
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
                rulesList.add(new Expr(variable.charAt(0), relation, number, destination, false));
            } else {
                rulesList.add(new Dest(ruleStr.replaceAll("}", "")));
            }
        }

        return new Workflow(name, rulesList);
    }
}
