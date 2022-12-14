package jwang.utils;

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
}
