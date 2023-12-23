package net.digihippo.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class TwentyTwo extends CommonParserSolutionTemplate<Integer, TwentyTwo.Brick> {
    record Brick(int index, ThreeDPoint from, ThreeDPoint to) {
        public int minZ() {
            return Math.min(from.z(), to.z());
        }

        public boolean supports(Brick b) {
            for (ThreeDPoint ours: cubes()) {
                for (ThreeDPoint theirs : b.cubes()) {
                    if (theirs.x() == ours.x() && theirs.y() == ours.y() && theirs.z() == ours.z() + 1) {
                        return true;
                    }
                }
            }

            return false;
        }

        private List<ThreeDPoint> cubes() {
            final List<ThreeDPoint> points = new ArrayList<>();
            for (int x = Math.min(from.x(), to.x()); x <= Math.max(from.x(), to.x()); x++) {
                for (int y = Math.min(from.y(), to.y()); y <= Math.max(from.y(), to.y()); y++) {
                    for (int z = Math.min(from.z(), to.z()); z <= Math.max(from.z(), to.z()); z++) {
                        points.add(new ThreeDPoint(x, y, z));
                    }
                }
            }

            return points;
        }

        public Brick moveDown() {
            return new Brick(index, from.moveDown(), to.moveDown());
        }
    }

    int counter = 0;
    @Override
    Brick parse(String line) {
        String[] parts = line.split("~");
        return new Brick(counter++, ThreeDPoint.from(parts[0]), ThreeDPoint.from(parts[1]));
    }

    @Override
    Integer partOne(List<Brick> bricks) {
        bricks.sort(Comparator.comparingInt(Brick::minZ));

        final List<Brick> planted = new ArrayList<>();
        final Map<Integer, List<Integer>> supports = new HashMap<>();
        for (final Brick start: bricks) {
            Brick brick = start;
            int z = start.minZ();
            List<Brick> supporters = anySupport(planted, brick);
            while (z > 1 && supporters.isEmpty()) {
                brick = brick.moveDown();
                z--;
                supporters = anySupport(planted, brick);
            }


            if (!supporters.isEmpty()) {
                ArrayList<Integer> sss = new ArrayList<>();
                for (Brick supporter : supporters) {
                    sss.add(supporter.index);
                }
                supports.put(brick.index, sss);
            }

            planted.add(brick);
        }

        int removableCount = 0;
        for (int i = 0; i < bricks.size(); i++) {
            boolean removable = true;
            for (List<Integer> value : supports.values()) {
                if (value.contains(i) && value.size() == 1) {
                    removable = false;
                }
            }
            if (removable) {
                removableCount++;
            }
        }

        return removableCount;
    }

    private List<Brick> anySupport(List<Brick> planted, Brick brick) {
        return planted.stream().filter(p -> p.supports(brick)).collect(Collectors.toList());
    }

    @Override
    Integer partTwo(List<Brick> parsed) {
        return null;
    }
}
