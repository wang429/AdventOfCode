package jwang.y2022;

import utils.Utils;

import java.util.List;

public class Day1 {

  public static void main(String... args) throws Exception {
    List<String> data = Utils.readFromFile("src/main/resources/jwang/y2022/Day1Input.txt");
    Maxes max = new Maxes();
    int sum = 0;
    for (String line : data) {
      if (line == null || line.length() == 0) {
        max.compareAndUpdate(sum);
        sum = 0;
      }
      else {
        sum += Integer.valueOf(line);
      }
    }
    max.compareAndUpdate(sum);
    System.out.println(max.total());
  }

  static class Maxes {
    int first = 0;
    int second = 0;
    int third = 0;

    void compareAndUpdate(int val) {
      if (val > first) {
        third = second;
        second = first;
        first = val;
      } else if (val > second) {
        third = second;
        second = val;
      } else if (val > third) {
        third = val;
      }
    }

    int total() {
      return first + second + third;
    }
  }
}
