package net.digihippo.aoc;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final List<String> lines;

    public Grid(List<String> lines) {
        this.lines = lines;
    }

    boolean contains(int x, int y) {
        return 0 <= x && x < lines.get(0).length() && 0 <= y && y < lines.size();
    }

    public String substring(int xStart, int xEndExclusive, int y) {
        return lines.get(y).substring(xStart, xEndExclusive);
    }

    public List<Integer> findEmptyColumns(char emptyChar) {
        final List<Integer> result = new ArrayList<>();

        for (int i = 0; i < lines.get(0).length(); i++) {
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
            for (int i = 0; i < lines.get(0).length(); i++) {
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

    interface RowVisitor extends Visitor
    {
        void rowStarted(int y);
        void rowDone(int y);
    }

    void visitRows(RowVisitor v) {
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
