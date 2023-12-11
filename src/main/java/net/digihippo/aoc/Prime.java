package net.digihippo.aoc;

import java.util.*;

public class Prime {
    private final Set<Integer> primes = new TreeSet<>();
    {
        primes.add(2);
    }
    private int maxProbePrime = 2;
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
