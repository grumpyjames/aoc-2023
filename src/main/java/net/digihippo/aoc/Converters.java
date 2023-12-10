package net.digihippo.aoc;

public final class Converters {
    static int fromAsciiByte(byte b)
    {
        return b - '0';
    }

    static int fromAsciiChar(char b)
    {
        return b - '0';
    }

    private Converters() {}

    public static boolean isNumeric(char c) {
        return '0' <= c && c <= '9';
    }
}
