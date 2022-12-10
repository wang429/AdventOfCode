package jwang.y2022;

import utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day10Part2 {

  public static final List<String> test1 = Arrays.asList(
      "noop",
      "addx 3",
      "addx -5"
  );

  public static final List<String> test2 = Arrays.asList(
      "addx 15",
      "addx -11",
      "addx 6",
      "addx -3",
      "addx 5",
      "addx -1",
      "addx -8",
      "addx 13",
      "addx 4",
      "noop",
      "addx -1",
      "addx 5",
      "addx -1",
      "addx 5",
      "addx -1",
      "addx 5",
      "addx -1",
      "addx 5",
      "addx -1",
      "addx -35",
      "addx 1",
      "addx 24",
      "addx -19",
      "addx 1",
      "addx 16",
      "addx -11",
      "noop",
      "noop",
      "addx 21",
      "addx -15",
      "noop",
      "noop",
      "addx -3",
      "addx 9",
      "addx 1",
      "addx -3",
      "addx 8",
      "addx 1",
      "addx 5",
      "noop",
      "noop",
      "noop",
      "noop",
      "noop",
      "addx -36",
      "noop",
      "addx 1",
      "addx 7",
      "noop",
      "noop",
      "noop",
      "addx 2",
      "addx 6",
      "noop",
      "noop",
      "noop",
      "noop",
      "noop",
      "addx 1",
      "noop",
      "noop",
      "addx 7",
      "addx 1",
      "noop",
      "addx -13",
      "addx 13",
      "addx 7",
      "noop",
      "addx 1",
      "addx -33",
      "noop",
      "noop",
      "noop",
      "addx 2",
      "noop",
      "noop",
      "noop",
      "addx 8",
      "noop",
      "addx -1",
      "addx 2",
      "addx 1",
      "noop",
      "addx 17",
      "addx -9",
      "addx 1",
      "addx 1",
      "addx -3",
      "addx 11",
      "noop",
      "noop",
      "addx 1",
      "noop",
      "addx 1",
      "noop",
      "noop",
      "addx -13",
      "addx -19",
      "addx 1",
      "addx 3",
      "addx 26",
      "addx -30",
      "addx 12",
      "addx -1",
      "addx 3",
      "addx 1",
      "noop",
      "noop",
      "noop",
      "addx -9",
      "addx 18",
      "addx 1",
      "addx 2",
      "noop",
      "noop",
      "addx 9",
      "noop",
      "noop",
      "noop",
      "addx -1",
      "addx 2",
      "addx -37",
      "addx 1",
      "addx 3",
      "noop",
      "addx 15",
      "addx -21",
      "addx 22",
      "addx -6",
      "addx 1",
      "noop",
      "addx 2",
      "addx 1",
      "noop",
      "addx -10",
      "noop",
      "noop",
      "addx 20",
      "addx 1",
      "addx 2",
      "addx 2",
      "addx -6",
      "addx -11",
      "noop",
      "noop",
      "noop"
  );

  static int cycle = 0;

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day10Input.txt");
//    input = test2;

    int x = 1;
    boolean[] out = new boolean[240];
    for (String line : input) {
      performCycle(x, out);
      if (line.startsWith("addx")) {
        performCycle(x, out);
        x += Integer.parseInt(line.split(" ")[1]);
      }
    }

    for (int i = 0; i < out.length; i++) {
      if (i % 40 == 0) {
        System.out.println();
      }
      System.out.printf("%s", out[i] ? "#" : ".");
    }
    System.out.println();
  }

  private static void performCycle(int x, boolean[] out) {
    draw(out, cycle, x);
    cycle++;
  }

  static void draw(boolean[] out, int cycle, int x) {
    int rowMod = (cycle / 40) * 40;
    int index = x - 1;
    if (0 <= index && index < 40 && index + rowMod == cycle) {
      out[index + rowMod] = true;
      return;
    }
    index++;
    if (0 <= index && index < 40 && index + rowMod == cycle) {
      out[index + rowMod] = true;
      return;
    }
    index++;
    if (0 <= index && index < 40 && index + rowMod == cycle) {
      out[index + rowMod] = true;
    }
  }
}
