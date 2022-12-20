package jwang.y2022.day17;

import jwang.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day17 {

  public static final int ITERATIONS = 2022;
  static List<String> test = Arrays.asList(
      ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day17Input.txt");

    int iterations = ITERATIONS;
//    input = test;
//    input = Collections.singletonList("<<<<<<<<>>><><<<<<>><<<<<<<<<<<<");
    boolean testMode = input.equals(test);

    // < is 60, > is 62
    int[] dirs = input.get(0).chars().map(i -> i-61).toArray();

    RockStack room1 = new RockStack(5000, 7);
    RockStack room2 = new RockStack(5000, 7);
    Rock[] rocks = Rock.values();
    // part 1
    long start = System.currentTimeMillis();
    part1(iterations, testMode, dirs, room1, rocks);
    long end = System.currentTimeMillis();
    if (true) {
      printRoom(true, room1, null, 0, 0);
    }
    else {
      System.out.println(room1.top);
    }
    System.out.println(end - start);
    // part 2
    part2(dirs, room2, rocks);
  }

  static void part1(int iterations, boolean testMode, int[] dirs, RockStack room, Rock[] rocks) {
    int dirCount = 0;
    for (int i = 0; i < iterations; i++) {
      dirCount = dropRock(iterations < 20, testMode, dirs, room, rocks[i % rocks.length], dirCount);
    }
    System.out.println(dirCount);
  }

  static long part2(int[] dirs, RockStack room, Rock[] rocks) {
    System.out.println(dirs.length * rocks.length);
    return 0l;
  }

  static int dropRock(boolean doPrint, boolean testMode, int[] dirs, RockStack room, Rock rock, int dirCount) {
    int x = 2;
    int y = 3 + room.top;
    if (doPrint) {
      System.out.println("New rock: " + rock.name());
      printRoom(doPrint, room, rock, x, y);
    }

    room.stack.ensureCapacity(y + 4);

    while (true) {
      int dir = dirs[dirCount % dirs.length];
      if (doPrint) {
        System.out.println(Arrays.stream(dirs).mapToObj(d -> (char) (d + 61)).map(String::valueOf).collect(Collectors.joining()));
        System.out.println(IntStream.range(0,dirCount).mapToObj(j -> " ").collect(Collectors.joining()) + "^");
        System.out.println(dirCount + " " + dir + " " + ((char) (dir + 61)) + " " + (dir < 0 ? "Push left" : "Push right"));
      }
      dirCount++;
      if (room.testRockHorizontal(rock, x, y, dir)) {
        x += dir;
      }
      printRoom(doPrint, room, rock, x, y);
      if (room.testRockVertical(rock, x, y)) {
        y--;
      }
      else {
        printRoom(doPrint, room, rock, x, y);
        break;
      }
      printRoom(doPrint, room, rock, x, y);
    }
    room.addRock(rock, x, y);
    room.top = Math.max(room.top, y + rock.h);
    if (testMode) {
      printRoom(doPrint, room, null, x, y);
    }
    return dirCount;
  }

  private static void printRoom(boolean doPrint, RockStack room, Rock rock, int x, int y) {
    if (!doPrint) {
      return;
    }
    printStack(room.stack, room.top, room.width, rock, x, y);
  }

  static void printStack(ArrayList<boolean[]> stack, int top, int width, Rock rock, int x, int y) {
    String[][] str = new String[top + 7][width];
    for (int i = 0; i < str.length; i++) {
      boolean[] row = stack.get(i);
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
    System.out.println(top);
    System.out.println();
  }

  static class RockStack {
    ArrayList<boolean[]> stack;
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
      if (x + dir < 0 || rock.w - 1 + x + dir >= width) {
        return false;
      }
      // check if rock fits into stack
      for (int i = 0; i < rock.shape.length; i++) {
        for (int j = 0; j < rock.shape[i].length; j++) {
          if (dir < 0) {
            boolean rockAtNextLoc = stack.get(i + y)[j + x + dir];
            if (rock.shape[i][j] && rockAtNextLoc) {
              return false;
            }
          } else {
            boolean rockAtNextLoc = stack.get(i + y)[rock.w - j - 1 + x + dir];
            if (rock.shape[i][rock.w - j - 1] && rockAtNextLoc) {
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
