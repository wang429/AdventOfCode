package jwang.y2022;

import jwang.utils.Utils;

import java.util.*;

public class Day18 {
  public static final int ITERATIONS = 2022;
  static List<String> test = Arrays.asList(
      "2,2,2",
      "1,2,2",
      "3,2,2",
      "2,1,2",
      "2,3,2",
      "2,2,1",
      "2,2,3",
      "2,2,4",
      "2,2,6",
      "1,2,5",
      "3,2,5",
      "2,1,5",
      "2,3,5"
  );

  static List<String> test2 = Arrays.asList(
      "1,1,1", "1,1,2", "1,1,3",
      "1,2,1", "1,2,2", "1,2,3",
      "1,3,1", "1,3,2", "1,3,3",

      "2,1,1", "2,1,2", "2,1,3",
      "2,2,1", "2,2,2", "2,2,3",
      "2,3,1", "2,3,2", "2,3,3",

      "3,1,1", "3,1,2", "3,1,3",
      "3,2,1", "3,2,2", "3,2,3",
      "3,3,1", "3,3,2", "3,3,3"
  );

  static List<String> test3 = Arrays.asList(
      "1,1,1", "1,1,2", "1,1,3", "1,1,4",
      "1,2,1", "1,2,2", "1,2,3", "1,2,4",
      "1,3,1", "1,3,2", "1,3,3", "1,3,4",
      "1,4,1", "1,4,2", "1,4,3", "1,4,4",

      "2,1,1", "2,1,2", "2,1,3", "2,1,4",
      "2,2,1",                   "2,2,4",
      "2,3,1",                   "2,3,4",
      "2,4,1", "2,4,2", "2,4,3", "2,4,4",

      "3,1,1", "3,1,2", "3,1,3", "3,1,4",
      "3,2,1",                   "3,2,4",
      "3,3,1",                   "3,3,4",
      "3,4,1", "3,4,2", "3,4,3", "3,4,4",

      "4,1,1", "4,1,2", "4,1,3", "4,1,4",
      "4,2,1", "4,2,2", "4,2,3", "4,2,4",
      "4,3,1", "4,3,2", "4,3,3", "4,3,4",
      "4,4,1", "4,4,2", "4,4,3", "4,4,4"
  );

  static List<String> test4 = Arrays.asList(
               "1,1,2", "1,1,3", "1,1,4",
      "1,2,1", "1,2,2", "1,2,3", "1,2,4",          "1,2,6",
      "1,3,1", "1,3,2", "1,3,3", "1,3,4",
      "1,4,1", "1,4,2", "1,4,3", "1,4,4",

      "2,1,1", "2,1,2", "2,1,3", "2,1,4",
      "2,2,1",          "2,2,3", "2,2,4",
      "2,3,1", "2,3,2",          "2,3,4",
      "2,4,1", "2,4,2", "2,4,3", "2,4,4",

      "3,1,1", "3,1,2", "3,1,3", "3,1,4",
      "3,2,1",                   "3,2,4",
      "3,3,1",          "3,3,3",          "3,3,5",
      "3,4,1", "3,4,2", "3,4,3", "3,4,4",

      "4,1,1", "4,1,2", "4,1,3", "4,1,4",
      "4,2,1", "4,2,2", "4,2,3",          "4,2,5",
               "4,3,2",          "4,3,4",
      "4,4,1", "4,4,2", "4,4,3", "4,4,4"
  );

  static List<String> test5 = Arrays.asList(
      "1,1,1", "1,1,2", "1,1,3", "1,1,4",
      "1,2,1", "1,2,2", "1,2,3", "1,2,4",          "1,2,6", "1,2,7", "1,2,8",
      "1,3,1", "1,3,2", "1,3,3", "1,3,4",          "1,3,6",          "1,3,8",
      "1,4,1", "1,4,2", "1,4,3", "1,4,4",          "1,4,6", "1,4,7", "1,4,8",

      "2,1,1", "2,1,2", "2,1,3", "2,1,4",
      "2,2,1",          "2,2,3", "2,2,4",          "2,2,6", "2,2,7", "2,2,8",
      "2,3,1", "2,3,2",          "2,3,4",          "2,3,6",          "2,3,8",
      "2,4,1", "2,4,2", "2,4,3", "2,4,4",          "2,4,6", "2,4,7", "2,4,8",

      "3,1,1", "3,1,2", "3,1,3", "3,1,4",
      "3,2,1",                   "3,2,4",          "3,2,6", "3,2,7", "3,2,8",
      "3,3,1",          "3,3,3", "3,3,4",          "3,3,6", "3,3,7", "3,3,8",
      "3,4,1", "3,4,2", "3,4,3", "3,4,4",          "3,4,6", "3,4,7", "3,4,8",

      "4,1,1", "4,1,2", "4,1,3", "4,1,4",
      "4,2,1", "4,2,2", "4,2,3", "4,2,4",
      "4,3,1", "4,3,2", "4,3,3", "4,3,4",
      "4,4,1", "4,4,2", "4,4,3", "4,4,4"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day18Input.txt");
//    input = test;
//    input = test2;
//    input = test5;
    boolean testMode = input.equals(test);

    SortedSet<Point> points = new TreeSet<>((a, b) -> a.x - b.x == 0 ? (a.y - b.y == 0 ? a.z - b.z : a.y - b.y) : a.x - b.x);
    int maxX = 0;
    int maxY = 0;
    int maxZ = 0;
    for (String line : input) {
      String[] split = line.split(",");
      int x = Integer.parseInt(split[0]) + 1;
      int y = Integer.parseInt(split[1]) + 1;
      int z = Integer.parseInt(split[2]) + 1;
      Point point = new Point(x, y, z, Type.OBSIDIAN);
      points.add(point);

      if (x > maxX) {
        maxX = x;
      }
      if (y > maxY) {
        maxY = y;
      }
      if (z > maxZ) {
        maxZ = z;
      }
    }

    Point[][][] grid = new Point[maxX + 2][maxY + 2][maxZ + 2];
    points.forEach(p -> grid[p.x][p.y][p.z] = p);

    printGrid(grid);

    long start1 = System.currentTimeMillis();
    long area1 = part1(points, grid);
    long end1 = System.currentTimeMillis();

    long start2 = System.currentTimeMillis();
    long area2 = part2(points, grid);
    long end2 = System.currentTimeMillis();

    printGrid(grid);
    System.out.println("===Part 1:===");
    System.out.println(end1 - start1);
    System.out.println(area1);
    System.out.println("===Part 2:===");
    System.out.println(end2 - start2);
    System.out.println(area2);

    System.out.println(points);
  }

  static void printGrid(Point[][][] grid) {
    for (int x = 0; x < grid.length; x++) {
      System.out.println("====================" + x + "====================");
      for (int y = 0; y < grid[x].length; y++) {
        for (int z = 0; z < grid[x][y].length; z++) {
          if (grid[x][y][z] == null) {
            System.out.print(".");
          }
          else if (grid[x][y][z].type == Type.OBSIDIAN){
            System.out.print("#");
          }
          else {
            System.out.print("~");
          }
        }
        System.out.println();
      }
      System.out.println("====================" + x + "====================");
    }
  }

  static long part1(SortedSet<Point> points, Point[][][] grid) {
    Set<Point> notVisited = new HashSet<>(points);
    Queue<Point> queue = new ArrayDeque<>();
    queue.add(points.first());
    long area = 0L;
    while(!queue.isEmpty()) {
      Point curr = queue.poll();
      notVisited.remove(curr);
      List<Point> neighbors = getNeighbors(grid, curr);
      area += 6 - neighbors.size();
      for (Point neighbor : neighbors) {
        if(notVisited.contains(neighbor) && !queue.contains(neighbor)) {
          queue.add(neighbor);
        }
      }

      // all the cubes in the network have been found
      // look for any other point not already visited. This will be in an unvisited network
      if (queue.isEmpty() && !notVisited.isEmpty()) {
        queue.add(notVisited.iterator().next());
      }
    }
    return area;
  }

  static long part2(SortedSet<Point> points, Point[][][] grid) {
    Set<Point> notVisited = new HashSet<>(points);
    Queue<Point> queue = new ArrayDeque<>();
    queue.add(points.first());
    long area = 0L;

    floodFill(grid);

    while(!queue.isEmpty()) {
      Point curr = queue.poll();
      notVisited.remove(curr);
      List<Point> neighbors = getNeighbors(grid, curr);
      for (Point neighbor : neighbors) {
        if (neighbor.type == Type.WATER) {
          area++;
        }
        if (notVisited.contains(neighbor) && !queue.contains(neighbor)) {
          queue.add(neighbor);
        }
      }

      // all the cubes in the network have been found
      // look for any other point not already visited. This will be in an unvisited network
      if (queue.isEmpty() && !notVisited.isEmpty()) {
        queue.add(notVisited.iterator().next());
      }
    }
    return area;
  }

  static List<Point> getNeighbors(Point[][][] grid, Point p) {
    List<Point> neighbors = new ArrayList<>();
    if (p.x + 1 < grid.length && grid[p.x + 1][p.y][p.z] != null) {
      neighbors.add(grid[p.x + 1][p.y][p.z]);
    }
    if (p.x - 1 >= 0 && grid[p.x - 1][p.y][p.z] != null) {
      neighbors.add(grid[p.x - 1][p.y][p.z]);
    }
    if (p.y + 1 < grid[p.x].length && grid[p.x][p.y + 1][p.z] != null) {
      neighbors.add(grid[p.x][p.y + 1][p.z]);
    }
    if (p.y - 1 >= 0 && grid[p.x][p.y - 1][p.z] != null) {
      neighbors.add(grid[p.x][p.y - 1][p.z]);
    }
    if (p.z + 1 < grid[p.x][p.y].length && grid[p.x][p.y][p.z + 1] != null) {
      neighbors.add(grid[p.x][p.y][p.z + 1]);
    }
    if (p.z - 1 >= 0 && grid[p.x][p.y][p.z - 1] != null) {
      neighbors.add(grid[p.x][p.y][p.z - 1]);
    }
    return neighbors;
  }

  static void floodFill(Point[][][] grid) {
    Set<Point> visited = new HashSet<>();
    Queue<Point> queue = new ArrayDeque<>();
    queue.add(new Point(0, 0, 0, Type.WATER));
    while(!queue.isEmpty()) {
      Point p = queue.poll();
      visited.add(p);
      grid[p.x][p.y][p.z] = p;
      List<Point> neighbors = new ArrayList<>();
      if (p.x + 1 < grid.length && grid[p.x + 1][p.y][p.z] == null) {
        neighbors.add(new Point(p.x + 1, p.y, p.z, Type.WATER));
      }
      if (p.x - 1 >= 0 && grid[p.x - 1][p.y][p.z] == null) {
        neighbors.add(new Point(p.x - 1, p.y, p.z, Type.WATER));
      }
      if (p.y + 1 < grid[p.x].length && grid[p.x][p.y + 1][p.z] == null) {
        neighbors.add(new Point(p.x, p.y + 1, p.z, Type.WATER));
      }
      if (p.y - 1 >= 0 && grid[p.x][p.y - 1][p.z] == null) {
        neighbors.add(new Point(p.x, p.y - 1, p.z, Type.WATER));
      }
      if (p.z + 1 < grid[p.x][p.y].length && grid[p.x][p.y][p.z + 1] == null) {
        neighbors.add(new Point(p.x, p.y, p.z + 1, Type.WATER));
      }
      if (p.z - 1 >= 0 && grid[p.x][p.y][p.z - 1] == null) {
        neighbors.add(new Point(p.x, p.y, p.z - 1, Type.WATER));
      }
      for (Point neighbor : neighbors) {
        if(!visited.contains(neighbor) && !queue.contains(neighbor)) {
          queue.add(neighbor);
        }
      }
    }
  }

  static class Point {
    final int x;
    final int y;
    final int z;
    final Type type;

    Point(int x, int y, int z, Type type) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.type = type;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x &&
          y == point.y &&
          z == point.z;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
      return "(" + x +
          ", " + y +
          ", " + z +
          ")";
    }
  }

  enum Type {
    OBSIDIAN, WATER
  }
}
