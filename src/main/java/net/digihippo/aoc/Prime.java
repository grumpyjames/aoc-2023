package net.digihippo.aoc;

import java.util.*;

public class Prime {
    private final Set<Integer> primes = new TreeSet<>();
    {
        primes.add(2);
    }
    private int maxProbePrime = 2;

    public static long lowestCommonMultiple(List<Integer> periods) {
        Prime prime = new Prime();
        Map<Integer, Integer> commonFactors = null;
        List<Map<Integer, Integer>> allFactors = new ArrayList<>();

        for (Integer period : periods) {
            Map<Integer, Integer> factorize = prime.factorize(period);
            if (commonFactors == null) {
                commonFactors = new HashMap<>(factorize);
            } else {
                Iterator<Map.Entry<Integer, Integer>> iterator = commonFactors.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = iterator.next();
                    boolean commonFactor = factorize.containsKey(entry.getKey());
                    if (commonFactor) {
                        entry.setValue(Math.min(entry.getValue(), factorize.get(entry.getKey())));
                    } else {
                        iterator.remove();
                    }
                }
            }

            allFactors.add(factorize);
        }

        long product = 1;
        for (Map<Integer, Integer> allFactor : allFactors) {
            for (Map.Entry<Integer, Integer> entry : allFactor.entrySet()) {
                int power = entry.getValue() - commonFactors.getOrDefault(entry.getKey(), 0);
                product *= (Math.pow(entry.getKey(), power));
            }
        }

        assert commonFactors != null;
        for (Map.Entry<Integer, Integer> entry : commonFactors.entrySet()) {
            product *= (Math.pow(entry.getKey(), entry.getValue()));
        }

        return product;
    }

    Map<Integer, Integer> factorize(int target) {

        final Map<Integer, Integer> result = new HashMap<>();
        if (prime(target)) {
            return Map.of(target, 1);
        }

        int left = target;
        for (int prime : primes) {
            while (left % prime == 0) {
                result.compute(prime, (key, value) -> {
                    if (value == null) {
                        return 1;
                    }

                    return value + 1;
                });
                left /= prime;
            }
        }

        if (left != 1) {
            result.put(left, 1);
        }

        return result;
    }

    private boolean prime(int candidate) {
        if (primes.contains(candidate)) {
            return true;
        }

        if (candidate < maxProbePrime) {
            return false;
        }

        for (int i = 2; i < Math.sqrt(candidate); i++) {
            boolean anyPrimeFactor = false;
            for (Integer prime : primes) {
                if (i % prime == 0) {
                    anyPrimeFactor = true;
                    break;
                }
            }
            if (!anyPrimeFactor) {
                primes.add(i);
                maxProbePrime = i;
            }
        }

        boolean anyPrimeFactor = false;
        for (Integer prime : primes) {
            if (candidate % prime != 0) {
                anyPrimeFactor = true;
                break;
            }
        }

        boolean isPrime = !anyPrimeFactor;
        if (isPrime) {
            primes.add(candidate);
        }

        return isPrime;
    }
}
