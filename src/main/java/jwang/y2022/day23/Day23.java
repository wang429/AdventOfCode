package jwang.y2022.day23;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import jwang.utils.Utils;

import java.util.*;
import java.util.function.BiFunction;

public class Day23 {

  static List<String> test = Arrays.asList(
      "....#..",
      "..###.#",
      "#...#.#",
      ".#...##",
      "#.###..",
      "##.#.##",
      ".#..#.."
  );

  static List<String> test2 = Arrays.asList( // 25
      ".....",
      "..##.",
      "..#..",
      ".....",
      "..##.",
      "....."
  );

  public static void main(String[] args) throws Exception {
    List<String> file = Utils.readFromFile("src/main/resources/jwang/y2022/Day23Input.txt");
    List<String> input = file;
//    input = test;
    boolean testMode = input != file;

    Grid grid1 = new Grid();
    Grid grid2 = new Grid();
    for (int y = 0; y < input.size(); y++) {
      char[] chars = input.get(y).toCharArray();
      for (int x = 0; x < chars.length; x++) {
        if (chars[x] == '#') {
          grid1.elves.put(new Coord(x, y), new Elf(x, y));
          grid2.elves.put(new Coord(x, y), new Elf(x, y));
        }
      }
    }

    System.out.println(grid1.elves);

    long start = System.currentTimeMillis();
    moveElves(testMode, grid1, false);
    int minX = grid1.elves.firstKey().x;
    int maxX = grid1.elves.firstKey().x;
    int minY = grid1.elves.firstKey().y;
    int maxY = grid1.elves.firstKey().y;
    for (Coord c : grid1.elves.keySet()) {
      if (c.x < minX) {
        minX = c.x;
      } else if (c.x > maxX) {
        maxX = c.x;
      }
      if (c.y < minY) {
        minY = c.y;
      } else if (c.y > maxY) {
        maxY = c.y;
      }
    }
    System.out.println((maxX - minX + 1) * (maxY - minY + 1) - grid1.elves.size());
    long end = System.currentTimeMillis();
    System.out.println(end - start);

    Elf.index=0;
    start = System.currentTimeMillis();
    System.out.println(moveElves(testMode, grid2, true));
    end = System.currentTimeMillis();
    System.out.println(end - start);
    printElves(grid2);
  }

  private static int moveElves(boolean testMode, Grid grid, boolean part2) {
    int round = 0;
    boolean move = true;
    while (move && (part2 || round < 10)) {
      if (testMode) {
        System.out.println("Round " + round + ": first move attempt: " + Elf.index);
      }
      SetMultimap<Coord, Elf> elvesByProposedLocations = HashMultimap.create();
      for (Map.Entry<Coord, Elf> entry : grid.elves.entrySet()) {
        Coord proposal = entry.getValue().proposeMove(grid);
        if (proposal.reason==null) {
          if (testMode) {
            System.out.println(entry.getValue() + " wants to move to " + proposal);
          }
          elvesByProposedLocations.put(proposal, entry.getValue());
        } else {
          if (testMode) {
            System.out.println(entry.getValue() + " doesn't want to move: " + proposal);
          }
        }
      }
      if (elvesByProposedLocations.isEmpty()) {
        move = false;
      }
      for (Coord proposal : elvesByProposedLocations.keySet()) {
        Set<Elf> elves = elvesByProposedLocations.get(proposal);
        // move to proposed location if only elf that proposed that location
        if (elves.size() != 1) {
          continue;
        }
        Elf elf = elves.iterator().next();
        grid.elves.remove(elf.coord);
        elf.coord = proposal;
        grid.elves.put(proposal, elf);
      }
      Elf.index = (++round) % 4;
      if (testMode) {
        printElves(grid);
        System.out.println();
      }
    }
    return round;
  }

  static void printElves(Grid grid) {
    int minX = grid.elves.firstKey().x;
    int maxX = grid.elves.firstKey().x;
    int minY = grid.elves.firstKey().y;
    int maxY = grid.elves.firstKey().y;
    for (Coord c : grid.elves.keySet()) {
      if (c.x < minX) {
        minX = c.x;
      } else if (c.x > maxX) {
        maxX = c.x;
      }
      if (c.y < minY) {
        minY = c.y;
      } else if (c.y > maxY) {
        maxY = c.y;
      }
    }
    System.out.println("======================");
    for (int y = minY; y <= maxY; y++) {
      for (int x = minX; x <= maxX; x++) {
        if (grid.elves.containsKey(new Coord(x, y))) {
          System.out.print('#');
        } else {
          System.out.print('.');
        }
      }
      System.out.println();
    }
  }

  static class Elf {
    int id;
    Coord coord;

    /**
     * N=0, S=1, W=2, E=3
     */
    static BiFunction<Grid, Coord, Coord>[] proposals = new BiFunction[4];
    static int index;

    static {
      proposals[0] = (g, c) -> {
        if (!hasNeighbors(g, c)) {
          return new Coord("no neighbor");
        } else if (considerNorth(g, c)) {
          return new Coord(c.x, c.y - 1);
        } else if (considerSouth(g, c)) {
          return new Coord(c.x, c.y + 1);
        } else if (considerWest(g, c)) {
          return new Coord(c.x - 1, c.y);
        } else if (considerEast(g, c)) {
          return new Coord(c.x + 1, c.y);
        } else {
          return new Coord("all dirs occupied");
        }
      };
      proposals[1] = (g, c) -> {
        if (!hasNeighbors(g, c)) {
          return new Coord("no neighbor");
        } else if (considerSouth(g, c)) {
          return new Coord(c.x, c.y + 1);
        } else if (considerWest(g, c)) {
          return new Coord(c.x - 1, c.y);
        } else if (considerEast(g, c)) {
          return new Coord(c.x + 1, c.y);
        } else if (considerNorth(g, c)) {
          return new Coord(c.x, c.y - 1);
        } else {
          return new Coord("all dirs occupied");
        }
      };
      proposals[2] = (g, c) -> {
        if (!hasNeighbors(g, c)) {
          return new Coord("no neighbor");
        } else if (considerWest(g, c)) {
          return new Coord(c.x - 1, c.y);
        } else if (considerEast(g, c)) {
          return new Coord(c.x + 1, c.y);
        } else if (considerNorth(g, c)) {
          return new Coord(c.x, c.y - 1);
        } else if (considerSouth(g, c)) {
          return new Coord(c.x, c.y + 1);
        } else {
          return new Coord("all dirs occupied");
        }
      };
      proposals[3] = (g, c) -> {
        if (!hasNeighbors(g, c)) {
          return new Coord("no neighbor");
        } else if (considerEast(g, c)) {
          return new Coord(c.x + 1, c.y);
        } else if (considerNorth(g, c)) {
          return new Coord(c.x, c.y - 1);
        } else if (considerSouth(g, c)) {
          return new Coord(c.x, c.y + 1);
        } else if (considerWest(g, c)) {
          return new Coord(c.x - 1, c.y);
        } else {
          return new Coord("all dirs occupied");
        }
      };
    }

    Elf(int x, int y) {
      this.coord = new Coord(x, y);
      index = 0;
      id = hashCode();
    }

    /**
     * N=0, S=1, W=2, E=3
     */
    Coord proposeMove(Grid grid) {
      return proposals[index].apply(grid, coord);
    }

    static boolean considerNorth(Grid grid, Coord coord) {
      return !(grid.elves.containsKey(new Coord(coord.x-1, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y-1)));
    }

    static boolean considerSouth(Grid grid, Coord coord) {
      return !(grid.elves.containsKey(new Coord(coord.x-1, coord.y+1)) ||
          grid.elves.containsKey(new Coord(coord.x, coord.y+1)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y+1)));
    }

    static boolean considerWest(Grid grid, Coord coord) {
      return !(grid.elves.containsKey(new Coord(coord.x-1, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x-1, coord.y)) ||
          grid.elves.containsKey(new Coord(coord.x-1, coord.y+1)));
    }

    static boolean considerEast(Grid grid, Coord coord) {
      return !(grid.elves.containsKey(new Coord(coord.x+1, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y+1)));
    }

    static boolean hasNeighbors(Grid grid, Coord coord) {
      return grid.elves.containsKey(new Coord(coord.x-1, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x-1, coord.y)) ||
          grid.elves.containsKey(new Coord(coord.x-1, coord.y+1)) ||
          grid.elves.containsKey(new Coord(coord.x, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x, coord.y+1)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y-1)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y)) ||
          grid.elves.containsKey(new Coord(coord.x+1, coord.y+1));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Elf elf = (Elf) o;
      return id == elf.id && coord == elf.coord;
    }

    @Override
    public int hashCode() {
      return Objects.hash(coord, id);
    }

    @Override
    public String toString() {
      return "Elf{" +
          "id=" + id +
          ", coord=" + coord +
          '}';
    }
  }

  static class Grid {
    SortedMap<Coord, Elf> elves;

    Grid() {
      elves = new TreeMap<>((a, b) -> a.x == b.x ? a.y - b.y : a.x - b.x);
    }
  }

  static class Coord {
    String reason;
    int x;
    int y;
    Coord(int x, int y) {
      this.x = x;
      this.y = y;
    }

    Coord(String reason) {
      this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Coord coord = (Coord) o;
      return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    @Override
    public String toString() {
      return reason != null ? reason : "(" + x +
          ", " + y +
          ')';
    }
  }
}
