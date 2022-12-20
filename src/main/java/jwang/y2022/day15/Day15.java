package jwang.y2022.day15;

import jwang.utils.Utils;
import jwang.utils.graphing.Point;
import jwang.utils.graphing.PointYFirstComparator;

import java.util.*;

public class Day15 {
  static List<String> test = Arrays.asList(
      "Sensor at x=2, y=18: closest beacon is at x=-2, y=15",
      "Sensor at x=9, y=16: closest beacon is at x=10, y=16",
      "Sensor at x=13, y=2: closest beacon is at x=15, y=3",
      "Sensor at x=12, y=14: closest beacon is at x=10, y=16",
      "Sensor at x=10, y=20: closest beacon is at x=10, y=16",
      "Sensor at x=14, y=17: closest beacon is at x=10, y=16",
      "Sensor at x=8, y=7: closest beacon is at x=2, y=10",
      "Sensor at x=2, y=0: closest beacon is at x=2, y=10",
      "Sensor at x=0, y=11: closest beacon is at x=2, y=10",
      "Sensor at x=20, y=14: closest beacon is at x=25, y=17",
      "Sensor at x=17, y=20: closest beacon is at x=21, y=22",
      "Sensor at x=16, y=7: closest beacon is at x=15, y=3",
      "Sensor at x=14, y=3: closest beacon is at x=15, y=3",
      "Sensor at x=20, y=1: closest beacon is at x=15, y=3"
  );

  public static void main(String[] args) throws Exception{
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day15Input.txt");
//    input = test;
    boolean testMode = input.equals(test);
    int yPart1 = testMode ? 10 : 2_000_000;
    int max = testMode ? 20 : 4000000;

    SortedMap<Point, Sensor> sensors = new TreeMap<>(new PointYFirstComparator());
    Set<Point> knownBeacons = new HashSet<>();
    for(String line : input) {
      String[] split = line.split(" ");
      Point sensorLoc = new Point(Integer.parseInt(split[2].substring(2,split[2].length()-1)),
          Integer.parseInt(split[3].substring(2,split[3].length()-1)));
      Point closestBeacon = new Point(Integer.parseInt(split[8].substring(2,split[8].length()-1)),
          Integer.parseInt(split[9].substring(2)));
      sensors.put(sensorLoc, new Sensor(sensorLoc, closestBeacon));
      knownBeacons.add(closestBeacon);
    }

    part1(sensors, knownBeacons, yPart1);
    part2(sensors, max);
  }

  static void part1(SortedMap<Point, Sensor> sensors, Set<Point> knownBeacons, int y) {
    long start = System.currentTimeMillis();

    Set<Integer> positionsWithoutBeacons = new HashSet<>();
    for (Sensor sensor : sensors.values()) {
      Point point = sensor.point;
      if (sensor.radius < Math.abs(y - point.getY())) {
        // sensor's closest beacon radius does not intersect the line at y, continue
        continue;
      }
      // starting at the current x, fan outwards until we're looking outside of the sensor's closest known beacon radius
      for (int i = 0;
           sensor.radius >= Utils.manhattanDistance(point.getX(), point.getY(), point.getX() + i, y);
           i++) {
        int x = point.getX() + i;
        if (!knownBeacons.contains(new Point(x, y))) {
          positionsWithoutBeacons.add(x);
        }
        x = point.getX() - i;
        if (!knownBeacons.contains(new Point(x, y))) {
          positionsWithoutBeacons.add(x);
        }
      }
    }
    System.out.println(positionsWithoutBeacons.size());

    long end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  static void part2(SortedMap<Point, Sensor> sensors, int max) {
    long start = System.currentTimeMillis();

    for (Sensor sensor : sensors.values()) {
      boolean present = false;
      int dx = 0;
      for (int y = Math.max(0, sensor.point.getY() - sensor.radius + 1);
           y <= Math.min(max, sensor.point.getY() + sensor.radius + 1);
           y++) {
        Set<Point> border = new HashSet<>();
        if (dx == 0) {
          border.add(new Point(sensor.point.getX(), y));
        }
        else {
          border.add(new Point(sensor.point.getX() + dx, y));
          border.add(new Point(sensor.point.getX() - dx, y));
        }
        Optional<Point> found = border.stream()
            .filter(p -> 0 <= p.getX() && p.getX() <= max)
            .filter(p -> sensors.values().stream().noneMatch(
                s -> Utils.manhattanDistance(s.point, p) <= s.radius
            ))
            .findAny();
        present = found.isPresent();
        if (present) {
          System.out.println(4000000L * found.get().getX() + found.get().getY());
          break;
        }
        else {
          if (y <= sensor.point.getY()) {
            dx++;
          }
          if (y > sensor.point.getY()) {
            dx--;
          }
        }
      }
      if (present) {
        break;
      }
    }

    long end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  static class Sensor {
    final Point point;
    final int radius;

    Sensor(Point point, int radius) {
      this.point = point;
      this.radius = radius;
    }

    Sensor(Point point, Point beacon) {
      this(point, Utils.manhattanDistance(point, beacon));
    }

    @Override
    public String toString() {
      return point + " " + radius;
    }
  }
}
