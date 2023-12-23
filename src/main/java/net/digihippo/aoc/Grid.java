package net.digihippo.aoc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Grid {
    private final List<char[]> lines;

    public Grid(List<String> lines) {
        this.lines = lines.stream().map(String::toCharArray).toList();
    }

    public static Grid fromString(String str) {
        String[] split = str.split("\n");
        final List<String> lines = new ArrayList<>(Arrays.asList(split));

        return new Grid(lines);
    }

    boolean contains(int x, int y) {
        return 0 <= x && x < lines.getFirst().length && 0 <= y && y < lines.size();
    }

    public String substring(int xStart, int xEndExclusive, int y) {
        return new String(lines.get(y), xStart, xEndExclusive - xStart);
    }

    public List<Integer> findEmptyColumns(char emptyChar) {
        final List<Integer> result = new ArrayList<>();

        for (int i = 0; i < lines.getFirst().length; i++) {
            boolean empty = true;
            for (char[] line : lines) {
                if (line[i] != emptyChar) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                result.add(i);
            }
        }

        return result;
    }

    public List<Integer> findEmptyRows(char emptyChar) {
        final List<Integer> result = new ArrayList<>();

        for (int j = 0; j < lines.size(); j++) {
            char[] line = lines.get(j);
            boolean empty = true;
            for (int i = 0; i < lines.getFirst().length; i++) {
                if (line[i] != emptyChar) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                result.add(j);
            }
        }

        return result;
    }

    public char at(int x, int y) {
        return lines.get(y)[x];
    }

    Set<TwoDPoint> vertexSet() {
        final Set<TwoDPoint> vertices = new HashSet<>();
        visit((x, y, content) -> {
            vertices.add(new TwoDPoint(x, y));
            vertices.add(new TwoDPoint(x + 1, y));
            vertices.add(new TwoDPoint(x + 1, y + 1));
            vertices.add(new TwoDPoint(x, y + 1));
        });
        return vertices;
    }

    public TwoDPoint replaceOnce(char needle, char replacement) {
        for (int j = 0; j < lines.size(); j++) {
            char[] line = lines.get(j);

            for (int i = 0; i < line.length; i++) {
                char content = line[i];
                if (content == needle) {
                    line[i] = replacement;
                    return new TwoDPoint(i, j);
                }
            }
        }

        throw new IllegalStateException();
    }

    public void set(int x, int y, char c) {
        lines.get(y)[x] = c;
    }

    public int count(CharPredicate cp) {
        final AtomicInteger ai = new AtomicInteger();
        visit(new Visitor() {
            @Override
            public void onCell(int x, int y, char content) {
                if (cp.matches(content)) {
                    ai.incrementAndGet();
                }
            }
        });

        return ai.get();
    }

    interface CharPredicate
    {
        boolean matches(char c);
    }

    List<TwoDPoint> find(CharPredicate charPredicate) {
        final List<TwoDPoint> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i);
            for (int j = 0; j < line.length; j++) {
                if (charPredicate.matches(line[j])) {
                    result.add(new TwoDPoint(j, i));
                }
            }
        }

        return result;
    }

    public int columnCount() {
        return lines.getFirst().length;
    }

    public boolean equalColumns(int i, int j) {
        for (char[] line : lines) {
            if (line[i] != line[j]) {
                return false;
            }
        }

        return true;
    }

    public int rowCount() {
        return lines.size();
    }

    public boolean equalRows(int i, int j) {
        return Arrays.equals(lines.get(i), lines.get(j));
    }

    public void printTo(PrintStream out) {
        for (char[] line : lines) {
            out.println(new String(line));
        }
        out.println();
    }

    public void swap(int x, int y, char one, char other) {
        if (lines.get(y)[x] == one) {
            lines.get(y)[x] = other;
        } else {
            lines.get(y)[x] = one;
        }
    }

    interface RowVisitor extends Visitor
    {
        void rowStarted(int y);
        void rowDone(int y);
    }

    void visit(Visitor v) {
        visit(new RowVisitor() {
            @Override
            public void rowStarted(int y) {

            }

            @Override
            public void rowDone(int y) {

            }

            @Override
            public void onCell(int x, int y, char content) {
                v.onCell(x, y, content);
            }
        });
    }

    // this visits rows from top to bottom and columns from left to right
    void visit(RowVisitor v) {
        for (int j = 0; j < lines.size(); j++) {
            char[] line = lines.get(j);
            v.rowStarted(j);

            for (int i = 0; i < line.length; i++) {
                v.onCell(i, j, line[i]);
            }

            v.rowDone(j);
        }
    }

    public interface Visitor
    {
        void onCell(int x, int y, char content);
    }

    interface OrthogonalVisitor
    {
        void onCell(Offset o, int x, int y, char content);
    }

    enum Offset
    {
        Up(0, -1) {
            @Override
            public boolean oppositeOf(Offset o) {
                return o == Down;
            }
        },
        Down(0, 1) {
            @Override
            public boolean oppositeOf(Offset o) {
                return o == Up;
            }
        },
        Left(-1, 0) {
            @Override
            public boolean oppositeOf(Offset o) {
                return o == Right;
            }
        },
        Right(1, 0) {
            @Override
            public boolean oppositeOf(Offset o) {
                return o == Left;
            }
        };

        private final int xOff;
        private final int yOff;

        Offset(int xOff, int yOff) {
            this.xOff = xOff;
            this.yOff = yOff;
        }

        public abstract boolean oppositeOf(Offset o);
    }

    void visitOrthogonalNeighboursOf(int x, int y, OrthogonalVisitor v) {
        for (Offset value : Offset.values()) {
            int probeX = x + value.xOff;
            int probeY = y + value.yOff;

            if (contains(probeX, probeY)) {
                v.onCell(value, probeX, probeY, lines.get(probeY)[probeX]);
            }
        }
    }

    void visitOrthogonalRepeatedNeighboursOf(int x, int y, OrthogonalVisitor v) {
        for (Offset value : Offset.values()) {
            int probeX = x + value.xOff;
            int probeY = y + value.yOff;

            int modX = ((probeX % columnCount()) + columnCount()) % columnCount();
            int modY = ((probeY % rowCount()) + rowCount()) % rowCount();

            v.onCell(value, probeX, probeY, lines.get(modY)[modX]);
        }
    }


    void visitNeighboursOfXRange(int xMin, int xMax, int y, Visitor v) {
        for (int probeX = xMin - 1; probeX <= xMax + 1; probeX++) {
            for (int yOff = -1; yOff <= 1; yOff++) {
                int probeY = y + yOff;
                if (contains(probeX, probeY)) {
                    v.onCell(probeX, probeY, lines.get(probeY)[probeX]);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grid grid = (Grid) o;
        if (grid.lines.size() != lines.size()) {
            return false;
        }

        for (int i = 0; i < lines.size(); i++) {
            char[] line = lines.get(i);
            char[] theirs = grid.lines.get(i);
            if (!Arrays.equals(line, theirs)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
