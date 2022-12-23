package jwang.y2022.day22;

import com.google.common.collect.Lists;
import jwang.utils.Utils;

import java.util.*;

public class Day22 {
  static List<String> test = Arrays.asList(
      "        ...#",
      "        .#..",
      "        #...",
      "        ....",
      "...#.......#",
      "........#...",
      "..#....#....",
      "..........#.",
      "        ...#....",
      "        .....#..",
      "        .#......",
      "        ......#.",
      "",
      "10R5L5R10L4R5L5"
  );
  static List<String> test2 = Arrays.asList(
      "##.###############",
      "#.................",
      "#.................",
      "",
      "10R5L100R100R3R4"
  );

  public static void main(String[] args) throws Exception{
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day22Input.txt");
//    input = test2;

    // (y, x) coord scheme, null is empty, true is '.' or walkable, false is '#' or wall
    Boolean[][] grid = new Boolean[input.size() - 2][];
    List<int[]> rowIndexRange = new ArrayList<>();
    int maxRowSize = 0;
    for (int y = 0; y < input.size() - 2; y++) {
      String line = input.get(y);
      maxRowSize = Math.max(line.length(), maxRowSize);
      grid[y] = new Boolean[line.length()];
      boolean foundRowStart = false;
      int[] rowRange = new int[2];
      for (int x = 0; x < line.length(); x++) {
        switch (line.charAt(x)) {
          case ' ':
            grid[y][x] = null;
            break;
          case '.':
            grid[y][x] = true;
            if (!foundRowStart) {
              foundRowStart = true;
              rowRange[0] = x;
            }
            break;
          case '#':
            grid[y][x] = false;
            if (!foundRowStart) {
              foundRowStart = true;
              rowRange[0] = x;
            }
            break;
          default:
            throw new IllegalStateException("unexpected char at (y,x)=(" + y + ',' + x + "): " + line.charAt(x));
        }
      }
      rowRange[1] = line.length();
      rowIndexRange.add(rowRange);
    }

    List<int[]> colIndexRange = new ArrayList<>(maxRowSize);
    for (int x = 0; x < maxRowSize; x++) {
      boolean colStartFound = false;
      boolean colEndFound = false;
      int[] colRange = new int[2];
      for (int y = 0; y < grid.length; y++) {
        if (x >= grid[y].length) {
          if (colStartFound && !colEndFound) {
            colEndFound = true;
            colRange[1] = y;
          }
          continue;
        }
        if (!colStartFound && grid[y][x] != null) {
          colStartFound = true;
          colRange[0] = y;
        }
        if (colStartFound && !colEndFound && grid[y][x] == null) {
          colEndFound = true;
          colRange[1] = y;
        }
      }
      if (colStartFound && !colEndFound) {
        colRange[1] = grid.length;
      }
      colIndexRange.add(colRange);
    }

    String directionStr = input.get(input.size() - 1);
    List<String> directions = new ArrayList<>();
    int start = 0;
    for (int i = 0; i < directionStr.length(); i++) {
      char c = directionStr.charAt(i);
      if (c == 'L' || c == 'R') {
        directions.add(directionStr.substring(start, i));
        directions.add(directionStr.substring(i, i+1));
        start = i+1;
      }
    }
    int lastL = directionStr.lastIndexOf('L');
    int lastR = directionStr.lastIndexOf('R');
    directions.add(directionStr.substring(Math.max(lastR+1, lastL+1)));

    rowIndexRange.forEach(a -> System.out.print(Arrays.toString(a)));
    System.out.println();
    colIndexRange.forEach(a -> System.out.print(Arrays.toString(a)));
    System.out.println();
    System.out.println(directions);
    printGrid(grid, Collections.emptyList());

    int[] currLoc = {0, Arrays.asList(grid[0]).indexOf(Boolean.TRUE)};
    Facing currFace = Facing.R;
    List<int[]> path = Lists.newArrayList(Arrays.copyOf(currLoc, currLoc.length));
    for (String direction : directions) {
      if (direction.equals("L") || direction.equals("R")) {
        currFace = currFace.turn(direction.charAt(0));
        continue;
      }
      int steps = Integer.parseInt(direction);
      for (int i = 0; i < steps; i++) {
        if(currFace == Facing.R || currFace == Facing.L){
          int[] range = rowIndexRange.get(currLoc[0]);
          int index = currLoc[1] + currFace.x;
          if (index >= range[1]) {
            index = range[0];
          } else if (index < range[0]) {
            index = range[1] - 1;
          }
          boolean b;
          try {
            b = grid[currLoc[0]][index];
          } catch (Exception e) {
            System.out.println(Arrays.toString(currLoc) + currFace);
            System.out.println(index);
            System.out.println(grid.length);
            System.out.println(maxRowSize);
            System.out.println(Arrays.toString(range));
            throw e;
          }

          if (b) {
            currLoc[1] = index;
          } else {
            break;
          }
        } else if(currFace == Facing.D || currFace == Facing.U){
          int[] range = colIndexRange.get(currLoc[1]);
          int index = currLoc[0] + currFace.y;
          if (index >= range[1]) {
            index = range[0];
          } else if (index < range[0]) {
            index = range[1] - 1;
          }

          boolean b;
          try {
            b = grid[index][currLoc[1]];
          } catch (Exception e) {
            System.out.println(Arrays.toString(currLoc) + currFace);
            System.out.println(index);
            System.out.println(grid.length);
            System.out.println(maxRowSize);
            System.out.println(Arrays.toString(range));
            throw e;
          }

          if (b) {
            currLoc[0] = index;
          } else {
            break;
          }
        }
        path.add(Arrays.copyOf(currLoc, currLoc.length));
      }
    }
    System.out.println("============================================================================");
    printGrid(grid, path);
    path.forEach(a -> System.out.print(Arrays.toString(a)));
    System.out.println();
    System.out.println(Arrays.toString(currLoc) + " " + currFace);
    System.out.println(1000 * (currLoc[0]+1) + 4 * (currLoc[1]+1) + currFace.ordinal());
  }

  static void printGrid(Boolean[][] grid, List<int[]> path) {
    SortedSet<int[]> pathSearch = new TreeSet<>((a, b) -> a[0]-b[0] == 0 ? a[1]-b[1] : a[0]-b[0]);
    pathSearch.addAll(path);
    for (int i = 0, gridLength = grid.length; i < gridLength; i++) {
      Boolean[] row = grid[i];
      for (int j = 0, rowLength = row.length; j < rowLength; j++) {
        Boolean b = row[j];
        if (b == null) {
          System.out.print(' ');
        } else if (b) {
          if (pathSearch.contains(new int[]{i, j})) {
            System.out.print('o');
          }
          else {
            System.out.print('.');
          }
        } else {
          System.out.print('#');
        }
      }
      System.out.println();
    }
  }

  enum Facing {
    R(0, 1), D(1, 0), L(0, -1), U(-1, 0);

    int y;
    int x;

    Facing(int y, int x) {
      this.y = y;
      this.x = x;
    }

    Facing turn(char dir) {
      if (dir == 'L') {
        return Facing.values()[(this.ordinal() + 3) % Facing.values().length];
      }
      if (dir == 'R') {
        return Facing.values()[(this.ordinal() + 1) % Facing.values().length];
      }
      throw new IllegalStateException("invalid dir: " + dir);
    }
  }
}
