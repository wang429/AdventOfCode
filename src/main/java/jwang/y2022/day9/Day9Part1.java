package jwang.y2022.day9;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jwang.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day9Part1 {
  static String[] test = {
      "R 4",
      "U 4",
      "L 3",
      "D 1",
      "R 4",
      "D 1",
      "L 5",
      "R 2"
  };

  static String[] manualTest = {
      "U 1",
      "R 2"
  };

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day9Input.txt");

    input = Arrays.asList(test);

    Coord start = new Coord(0, 0);
    Rope rope = new Rope(0, 0);
    List<Coord> headCoords = Lists.newArrayList(start);
    List<Coord> allTailCoords = Lists.newArrayList(start);
    Set<Coord> tailCoords = Sets.newHashSet(start);
    for (String line : input) {
      String[] split = line.split(" ");

      for (int i = 0; i < Integer.parseInt(split[1]); i++) {
        rope.move(DIR.valueOf(split[0]));
        tailCoords.add(new Coord(rope.tail.x, rope.tail.y));
        allTailCoords.add(new Coord(rope.tail.x, rope.tail.y));
        headCoords.add(new Coord(rope.head.x, rope.head.y));
      }
    }

    System.out.println(headCoords);
    System.out.println(allTailCoords);
    System.out.println(tailCoords);
    System.out.println(tailCoords.size());
  }

  static class Coord {
    int x;
    int y;

    Coord(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Coord)) {
        return false;
      }
      Coord other = (Coord) obj;
      return other.x == this.x && other.y == this.y;
    }

    @Override
    public String toString() {
      return String.format("(%d, %d)", x, y);
    }
  }

  static class Rope {
    Coord head;
    Coord tail;

    Rope(int x, int y) {
      head = new Coord(x, y);
      tail = new Coord(x, y);
    }

    void move(DIR dir) {
      head.x += dir.x;
      head.y += dir.y;

      int dx = head.x - tail.x;
      int dy = head.y - tail.y;
      int axisShift = (Math.abs(dx) + Math.abs(dy)) / 3;
      switch (dir) {
        case R:
        case L:
          tail.x += dx / 2;
          tail.y += dy * axisShift;
          break;
        case U:
        case D:
          tail.x += dx * axisShift;
          tail.y += dy / 2;
          break;
        default:
          throw new RuntimeException("NANI THE FUCK?");
      }
    }
  }

  enum DIR {
    R(1, 0), U(0, 1), L(-1, 0), D(0, -1);

    int x;
    int y;

    DIR(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
