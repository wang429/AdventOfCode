package jwang.y2022;

import jwang.utils.Utils;

import java.util.*;
import java.util.function.Function;

public class Day17 {

  public static final int ITERATIONS = 2022;
  static List<String> test = Arrays.asList(
      ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day17Input.txt");

    int iterations = 15;
    input = test;
//    input = Collections.singletonList("<<<<<<<<>>><><<<<<>><<<<<<<<<<<<");
    boolean testMode = input.equals(test);

    long start = System.currentTimeMillis();

    // < is 60, > is 62
    int[] dirs = input.get(0).chars().map(i -> i-61).toArray();

    RockStack room = new RockStack(5000, 7);
    Rock[] rocks = Rock.values();
    int dirCount = 0;
    for (int i = 0; i < iterations; i++) {
      dirCount = dropRock(iterations, testMode, dirs, room, rocks[i % rocks.length], dirCount);
    }

    long end = System.currentTimeMillis();
    printRoom(room, null, 0, 0);
    System.out.println(end - start);
  }

  private static int dropRock(int iterations, boolean testMode, int[] dirs, RockStack room, Rock rock, int dirCount) {
    int x = 2;
    int y = 3 + room.top;
    if (iterations < 20) {
      System.out.println("New rock: " + rock.name());
      printRoom(room, rock, x, y);
    }

    while (true) {
      int dir = dirs[dirCount];
      if (iterations < 20) {
        System.out.println(dirCount + " " + dir + " " + ((char) (dir + 61)) + " " + (dir < 0 ? "Push left" : "Push right"));
      }
      dirCount = (dirCount + 1) % dirs.length;
      if (room.testRockHorizontal(rock, x, y, dir)) {
        x += dir;
      }
      if (room.testRockVertical(rock, x, y)) {
        y--;
      }
      else {
        if (iterations < 20) {
          printRoom(room, rock, x, y);
        }
        break;
      }

      if (iterations < 20) {
        printRoom(room, rock, x, y);
      }
    }
    room.addRock(rock, x, y);
    room.top = Math.max(room.top, y + rock.h);
    if (testMode) {
      System.out.println(room.top);
    }
    return dirCount;
  }

  private static void printRoom(RockStack room, Rock rock, int x, int y) {
    String[][] str = new String[room.top + 7][room.width];
    for (int i = 0; i < str.length; i++) {
      boolean[] row = room.stack.get(i);
      for (int j = 0; j < str[i].length; j++) {
        String val;
        if (rock != null && 0 <= i - y && i - y < rock.h && 0 <= j - x && j - x < rock.w && rock.shape[i - y][j - x]) {
          val = "@";
        }
        else {
          val = row[j] ? "#" : ".";
        }
        str[str.length - 1 - i][j] = val;
      }
    }

    Arrays.stream(str).forEach(row -> {
      System.out.print("|");
      Arrays.stream(row).forEach(System.out::print);
      System.out.println("|");
    });
    System.out.println("+-------+");
    System.out.println(room.top);
    System.out.println();
  }

  static class RockStack {
    List<boolean[]> stack;
    final int width;
    int top;

    RockStack(int initialCapacity, int width) {
      stack = new ArrayList<>(initialCapacity);
      this.width = width;
      for (int i = 0; i < initialCapacity; i++) {
        boolean[] row = new boolean[width];
        stack.add(row);
      }
      top = 0;
    }

    /**
     * @param x represents the current zero-indexed left edge of the rock
     * @param y represents the current zero-indexed bottom edge of the rock
     * @param dir -1 to move left, +1 to move right
     * @return is true if the rock can move in the direction provided, else false;
     */
    boolean testRockHorizontal(Rock rock, int x, int y, int dir) {
      if (x+dir < 0 || x+dir+rock.w > width) {
        return false;
      }
      // check if rock fits into stack
      for (int i = 0; i < rock.shape.length; i++) {
        for (int j = 0; j < rock.shape[i].length; j++) {
          boolean rockAtNextLoc = stack.get(i + y)[j + x + dir];
          if (dir < 0) {
            if (rock.shape[i][j] && rockAtNextLoc) {
              return false;
            }
          }
          else {
            if (rock.shape[i][rock.w-1-j] && rockAtNextLoc) {
              return false;
            }
          }
        }
      }
      return true;
    }

    /**
     * checks if there is a space into which the rock fit one space below
     *
     * @param x represents the current zero-indexed left edge of the rock
     * @param y represents the current zero-indexed bottom edge of the rock
     * @return is true if the rock can move downward, else false;
     */
    boolean testRockVertical(Rock rock, int x, int y) {
      if (y == 0) {
        return false;
      }
      // check if rock fits into stack
      for (int i = 0; i < rock.shape.length; i++) {
        for (int j = 0; j < rock.shape[i].length; j++) {
          if (rock.shape[i][j] &&
              stack.get(y+i-1)[j + x]) {
            return false;
          }
        }
      }
      return true;
    }

    void addRock(Rock rock, int x, int y) {
      for (int i = 0; i < rock.shape.length; i++) {
        for (int j = 0; j < rock.shape[i].length; j++) {
          boolean b = rock.shape[i][j];
          stack.get(y+i)[x+j] = b || stack.get(y + i)[x + j];
        }
      }
    }
  }

  enum Rock {
    // shapes have (y, x) coords and y=0 is first row
    M(new String[][]{{"#", "#", "#", "#"}}),
    P(new String[][]{{".", "#", "."}, {"#", "#", "#"}, {".", "#", "."}}),
    L(new String[][]{{"#", "#", "#"}, {".", ".", "#"}, {".", ".", "#"}}),
    I(new String[][]{{"#"}, {"#"}, {"#"}, {"#"}}),
    O(new String[][]{{"#", "#"}, {"#", "#"}});

    boolean[][] shape;
    int h;
    int w;

    Rock(String[][] shape) {
      this.shape = new boolean[shape.length][];
      for (int i = 0; i < shape.length; i++) {
        this.shape[i] = new boolean[shape[i].length];
        for (int j = 0; j < shape[i].length; j++) {
          this.shape[i][j] = shape[i][j].equals("#");
        }
      }
      this.h = shape.length;
      this.w = Math.toIntExact(Arrays.stream(shape).map(row -> Arrays.stream(row).filter("#"::equals).count()).max(Comparator.comparingLong(Long::longValue)).get());
    }
  }
}
