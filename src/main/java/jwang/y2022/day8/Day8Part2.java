package jwang.y2022.day8;

import jwang.utils.Utils;

import java.io.IOException;
import java.util.List;

public class Day8Part2 {
  static int[][] test = {
      {3,0,3,7,3},
      {2,5,5,1,2},
      {6,5,3,3,2},
      {3,3,5,4,9},
      {3,5,3,9,0}
  };

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day8Input.txt");
    // transform input into 2d array
    int[][] grid = new int[input.size()][];
    for (int i = 0; i < input.size(); i++) {
      String line = input.get(i);
      grid[i] = new int[line.length()];
      for (int j = 0; j < line.length(); j++) {
        grid[i][j] = line.charAt(j) - '0';
      }
    }
//    grid = test;

    long max = 0L;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        long curr = isVisible(i, j, grid);
        if (curr > max) {
          max = curr;
        }
      }
    }
    System.out.println(max);
  }

  static long isVisible(int i, int j, int[][] grid) {
    if (i == 0 || j == 0 || i == grid.length-1 || j == grid[i].length-1) {
      return 0;
    }
    return isVisibleUp(i, j, grid) *
      isVisibleDown(i, j, grid) *
      isVisibleLeft(i, j, grid) *
      isVisibleRight(i, j, grid);
  }

  private static long isVisibleUp(int i, int j, int[][] grid) {
    int k;
    for (k = 0; k < i; k++) {
      if(grid[i][j] <= grid[i-k-1][j]) {
        return k + 1;
      }
    }
    return k;
  }

  private static long isVisibleDown(int i, int j, int[][] grid) {
    int k;
    for (k = 0; k < grid.length - i - 1; k++) {
      if(grid[i][j] <= grid[i+k+1][j]) {
        return k + 1;
      }
    }
    return k;
  }

  private static long isVisibleLeft(int i, int j, int[][] grid) {
    int k;
    for (k = 0; k < j; k++) {
      if(grid[i][j] <= grid[i][j-k-1]) {
        return k + 1;
      }
    }
    return k;
  }

  private static long isVisibleRight(int i, int j, int[][] grid) {
    int k;
    for (k = 0; k < grid[i].length - j - 1; k++) {
      if(grid[i][j] <= grid[i][j+k+1]) {
        return k + 1;
      }
    }
    return k;
  }
}
