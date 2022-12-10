package jwang.y2022;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day9Part2 {
  static String[] test1 = {
      "R 4",
      "U 4",
      "L 3",
      "D 1",
      "R 4",
      "D 1",
      "L 5",
      "R 2"
  };
  static String[] test2 = {
      "R 5",
      "U 8",
      "L 8",
      "D 3",
      "R 17",
      "D 10",
      "L 25",
      "U 20",
  };

  static String[] manualTest = {
      "U 1",
      "R 2"
  };

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day9Input.txt");

//    input = Arrays.asList(test2);

    Coord start = new Coord(0, 0);
    Rope rope = new Rope(0, 0);
    List<Coord> headCoords = Lists.newArrayList(start);
    List<Coord> allTailCoords = Lists.newArrayList(start);
    Set<Coord> tailCoords = Sets.newHashSet(start);
    for (String line : input) {
      String[] split = line.split(" ");
      for (int i = 0; i < Integer.parseInt(split[1]); i++) {
        rope.move(DIR.valueOf(split[0]));
        Coord tail = rope.knots[rope.knots.length - 1];
        headCoords.add(rope.knots[0]);
        allTailCoords.add(tail);
        tailCoords.add(tail);
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

    boolean isAdjacentTo(Coord other) {
      return Math.abs(other.x - x) <= 1 && Math.abs(other.y - y) <= 1;
    }

    Coord getNewCoord(DIR dir) {
      return new Coord(x + dir.x, y + dir.y);
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
      return String.format("(%4d, %4d)", x, y);
    }
  }

  static class Rope {
    Coord[] knots = new Coord[10];

    Rope(int x, int y) {
      for (int i = 0; i < knots.length; i++) {
        knots[i] = new Coord(x, y);
      }
    }

    void move(DIR dir) {
      // treat `head` as the head of the segment, not the head of the rope
      Coord head = knots[0];
      knots[0] = head.getNewCoord(dir);
      for (int i = 1; i < knots.length; i++) {
        if (knots[i].isAdjacentTo(knots[i-1])) {
          // return instead of break or continue because we know all following knots won't move if this knot doesn't move
          return;
        }
        knots[i] = knots[i].getNewCoord(DIR.getDir(knots[i - 1], knots[i]));
      }
    }
  }

  enum DIR {
    R(1, 0), U(0, 1), L(-1, 0), D(0, -1),
    RU(1, 1), LU(-1, 1), LD(-1, -1), RD(1, -1),
    N(0, 0);

    int x;
    int y;

    DIR(int x, int y) {
      this.x = x;
      this.y = y;
    }

    static DIR getDir(Coord head, Coord tail) {
      return getDir(head.x - tail.x, head.y - tail.y);
    }

    static DIR getDir(int x, int y) {
      for (DIR dir : DIR.values()) {
        if (dir.x == (bindToUnitRange(x)) && dir.y == bindToUnitRange(y)) {
          return dir;
        }
      }
      throw new RuntimeException("!?!?!?!?!?!?!? " + x + " " + y);
    }

    private static int bindToUnitRange(int n) {
      return n == 0 ? n : n / Math.abs(n);
    }
  }
}
