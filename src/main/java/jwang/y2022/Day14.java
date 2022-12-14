package jwang.y2022;

import jwang.utils.Utils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {
  public static final List<String> test = Arrays.asList(
      "498,4 -> 498,6 -> 496,6",
      "503,4 -> 502,4 -> 502,9 -> 494,9"
  );

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day14Input.txt");
//    input = test;

    SortedSet<Coord> takenUp = new TreeSet<>((a, b) -> a.x - b.x == 0 ? a.y - b.y : a.x - b.x);
    SortedSet<Integer> allX = new TreeSet<>();
    SortedSet<Integer> allY = new TreeSet<>();
    for (String line : input) {
      // each line represents a rock path
      Coord[] rockPath = Arrays.stream(line.split(" -> "))
          .sequential()
          .map(s -> s.split(","))
          .map(Coord::new)
          .toArray(Coord[]::new);
      for (int i = 0; i < rockPath.length - 1; i++) {
        Coord a = rockPath[i];
        Coord b = rockPath[i+1];
        List<Coord> coords = a.findCoordsInLine(b);
        takenUp.addAll(coords);
      }
    }
    takenUp.forEach(c -> {
      allX.add(c.x);
      allY.add(c.y);
    });

    char[][] startingDiagram = new char[allY.last() + 2][allX.last() - allX.first() + 1];
    for (int y = 0; y < startingDiagram.length; y++) {
      for (int x = 0; x < startingDiagram[y].length; x++) {
        if (takenUp.contains(new Coord(x + allX.first(), y))) {
          startingDiagram[y][x] = '#';
        }
        else if (x + allX.first() == 500 && y == 0) {
          startingDiagram[y][x] = '+';
        }
        else {
          startingDiagram[y][x] = '.';
        }
      }
    }
    int startx = 500;
    int starty = 0;
    System.out.println("part1");
    part1(new TreeSet<>(takenUp), new TreeSet<>(allX), new TreeSet<>(allY), startingDiagram, startx, starty);
    System.out.println("part2");
    part2(new TreeSet<>(takenUp), new TreeSet<>(allX), new TreeSet<>(allY), startingDiagram, startx, starty);
  }

  private static void part1(SortedSet<Coord> takenUp,
                            SortedSet<Integer> allX,
                            SortedSet<Integer> allY,
                            char[][] startingDiagram,
                            int startx,
                            int starty) {
    long start = System.currentTimeMillis();
    int count = 0;
    while (!simulateFall(new Coord(startx, starty), takenUp, allX, allY.last(), false)) {
      count++;
    }
    long end = System.currentTimeMillis();
    System.out.println(end - start);
    System.out.println(count);
    printDiagram(takenUp, allX, startingDiagram);
  }

  private static void part2(SortedSet<Coord> takenUp,
                            SortedSet<Integer> allX,
                            SortedSet<Integer> allY,
                            char[][] startingDiagram,
                            int startx,
                            int starty) {
    long start = System.currentTimeMillis();
    // add one more layer to as the layer above the floor.
    allY.add(allY.last() + 1);
    int count = 0;
    while (!takenUp.contains(new Coord(startx, starty))) {
      simulateFall(new Coord(startx, starty), takenUp, allX, allY.last(), true);
      count++;
    }
    long end = System.currentTimeMillis();
    System.out.println(count);
    System.out.println(end - start);
    printDiagram(takenUp, allX, startingDiagram);
  }

  private static void printDiagram(SortedSet<Coord> takenUp, SortedSet<Integer> allX, char[][] startingDiagram) {
    char[][] diagram = new char[startingDiagram.length][];
    for (int y = 0; y < startingDiagram.length; y++) {
      diagram[y] = new char[startingDiagram[y].length];
      for (int x = 0; x < startingDiagram[y].length; x++) {
        if (takenUp.contains(new Coord(x + allX.first(), y))) {
          if (startingDiagram[y][x] == '.') {
            diagram[y][x] = 'o';
          }
          else {
            diagram[y][x] = startingDiagram[y][x];
          }
        }
        else {
          diagram[y][x] = startingDiagram[y][x];
        }
      }
    }
    Arrays.stream(diagram).forEach(System.out::println);
  }

  /**
   * if hasFloor is false, returns true if sand ends up in the void, and false if sand ends at rest
   * if hasFloor is true, returns false
   */
  static boolean simulateFall(Coord sand, SortedSet<Coord> takenUp, final SortedSet<Integer> allX, final int depth, boolean hasFloor) {
    if (!hasFloor && (sand.x < allX.first() || sand.x > allX.last())) {
      return true;
    }
    // look down until inside of an occupied space
    while (!takenUp.contains(sand)) {
      sand.y++;
      if (sand.y > depth) {
        if (hasFloor) {
          sand.y--;
          takenUp.add(sand);
          return false;
        }
        else {
          return true;
        }
      }
    }
    // check left
    sand.x--;
    if (!takenUp.contains(sand)) {
      return simulateFall(sand, takenUp, allX, depth, hasFloor);
    }
    // check right
    sand.x += 2;
    if (!takenUp.contains(sand)) {
      return simulateFall(sand, takenUp, allX, depth, hasFloor);
    }
    // sand can't fall any more, put sand at rest (into takenUp param);
    // we're still down and to the right from where we should be
    sand.x--;
    sand.y--;
    takenUp.add(sand);
    return false;
  }

  public static class Coord {
    int x;
    int y;

    public Coord(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Coord(String[] coord) {
      this.x = Integer.parseInt(coord[0]);
      this.y = Integer.parseInt(coord[1]);
    }

    List<Coord> findCoordsInLine(Coord other) {
      if (x == other.x) {
        if (y == other.y) {
          return Collections.singletonList(this);
        }
        int y1;
        int y2;
        if (y < other.y) {
          y1 = y;
          y2 = other.y;
        }
        else {
          y1 = other.y;
          y2 = y;
        }
        // end value is exclusive, so + 1
        return IntStream.range(y1, y2 + 1).mapToObj(i -> new Coord(x, i)).collect(Collectors.toList());
      }
      int x1;
      int x2;
      if (x < other.x) {
        x1 = x;
        x2 = other.x;
      }
      else {
        x1 = other.x;
        x2 = x;
      }
      // end value is exclusive, so + 1
      return IntStream.range(x1, x2 + 1).mapToObj(i -> new Coord(i, y)).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Coord coord = (Coord) o;
      return x == coord.x &&
          y == coord.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    @Override
    public String toString() {
      return "(" + x + ", " + y + ")";
    }
  }
}
