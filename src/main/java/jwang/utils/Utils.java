package jwang.utils;

import jwang.utils.graphing.Point;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  public static final String INPUT_PATH_PATTERN = "src/main/resources/jwang/y%d/Day%dInput.txt";

  public static List<String> readFromFile(String relativePath) throws IOException {
    Path path = Paths.get(relativePath);
    try (BufferedReader br = Files.newBufferedReader(path)) {
      List<String> ret = new ArrayList<>();
      String line;
      while ((line = br.readLine()) != null) {
        ret.add(line);
      }
      return ret;
    }
  }

  public static List<String> readFromFile(int year, int day) throws IOException {
    return readFromFile(String.format(INPUT_PATH_PATTERN, year, day));
  }

  public static int manhattanDistance(Point a, Point b) {
    return manhattanDistance(a.getX(), a.getY(), b.getX(), b.getY());
  }

  public static int manhattanDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }
}
