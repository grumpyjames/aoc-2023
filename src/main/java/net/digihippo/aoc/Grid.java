package net.digihippo.aoc;

import java.io.PrintStream;
import java.util.*;

public class Grid {
    private final List<String> lines;

    public Grid(List<String> lines) {
        this.lines = lines;
    }

    public static Grid fromString(String str) {
        final List<String> lines = new ArrayList<>();
        String[] split = str.split("\n");
        Collections.addAll(lines, split);

        return new Grid(lines);
    }

    boolean contains(int x, int y) {
        return 0 <= x && x < lines.getFirst().length() && 0 <= y && y < lines.size();
    }

    public String substring(int xStart, int xEndExclusive, int y) {
        return lines.get(y).substring(xStart, xEndExclusive);
    }

    public List<Integer> findEmptyColumns(char emptyChar) {
        final List<Integer> result = new ArrayList<>();

        for (int i = 0; i < lines.getFirst().length(); i++) {
            boolean empty = true;
            for (String line : lines) {
                if (line.charAt(i) != emptyChar) {
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
            String line = lines.get(j);
            boolean empty = true;
            for (int i = 0; i < lines.getFirst().length(); i++) {
                if (line.charAt(i) != emptyChar) {
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
        return lines.get(y).charAt(x);
    }

    public void visitBoundary(Visitor visitor) {
        String top = lines.getFirst();
        for (int i = 0; i < top.length(); i++) {
             visitor.onCell(i, 0, top.charAt(i));
        }

        for (int i = 1; i <= lines.size() - 2; i++) {
            String row = lines.get(i);

            visitor.onCell(0, i, row.charAt(0));
            visitor.onCell(row.length() - 1, i, row.charAt(row.length() - 1));
        }

        String bottom = lines.getLast();
        for (int i = 0; i < bottom.length(); i++) {
            visitor.onCell(i, lines.size() - 1, bottom.charAt(i));
        }
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

    interface CharPredicate
    {
        boolean matches(char c);
    }

    public List<TwoDPoint> find(CharPredicate charPredicate) {
        final List<TwoDPoint> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (charPredicate.matches(line.charAt(j))) {
                    result.add(new TwoDPoint(j, i));
                }
            }
        }

        return result;
    }

    public int columnCount() {
        return lines.getFirst().length();
    }

    public boolean equalColumns(int i, int j) {
        for (String line : lines) {
            if (line.charAt(i) != line.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    public int rowCount() {
        return lines.size();
    }

    public boolean equalRows(int i, int j) {
        return lines.get(i).equals(lines.get(j));
    }

    public void printTo(PrintStream out) {
        for (String line : lines) {
            out.println(line);
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

    void visit(RowVisitor v) {
        for (int j = 0; j < lines.size(); j++) {
            String line = lines.get(j);
            v.rowStarted(j);

            for (int i = 0; i < line.length(); i++) {
                v.onCell(i, j, line.charAt(i));
            }

            v.rowDone(j);
        }
    }

    public interface Visitor
    {
        void onCell(int x, int y, char content);
    }

    public interface OrthogonalVisitor
    {
        void onCell(Offset o, int x, int y, char content);
    }

    enum Offset
    {
        Up(0, -1),
        Down(0, 1),
        Left(-1, 0),
        Right(1, 0);

        private final int xOff;
        private final int yOff;

        Offset(int xOff, int yOff) {
            this.xOff = xOff;
            this.yOff = yOff;
        }
    }

    void visitOrthogonalNeighboursOf(int x, int y, OrthogonalVisitor v) {
        for (Offset value : Offset.values()) {
            int probeX = x + value.xOff;
            int probeY = y + value.yOff;

            if (contains(probeX, probeY)) {
                v.onCell(value, probeX, probeY, lines.get(probeY).charAt(probeX));
            }
        }
    }

    void visitNeighboursOf(int x, int y, Visitor v) {
        visitNeighboursOfXRange(x, x, y, v);
    }

    void visitNeighboursOfXRange(int xMin, int xMax, int y, Visitor v) {
        for (int probeX = xMin - 1; probeX <= xMax + 1; probeX++) {
            for (int yOff = -1; yOff <= 1; yOff++) {
                int probeY = y + yOff;
                if (contains(probeX, probeY)) {
                    v.onCell(probeX, probeY, lines.get(probeY).charAt(probeX));
                }
            }
        }
    }
}
