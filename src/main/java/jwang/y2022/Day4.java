package jwang.y2022;

import com.google.common.collect.Sets;
import jwang.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
  public static void main(String... args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day4Input.txt");
//    List<String> input = Arrays.asList("2-4,6-8",
//        "2-3,4-5",
//        "5-7,7-9",
//        "2-8,3-7",
//        "6-6,4-6",
//        "2-6,4-8");
    int count = 0;
    for (String line : input) {
      String[] pair = line.split(",");
      Set<Integer> first = makeList(pair[0]);
      Set<Integer> second = makeList(pair[1]);
      if (comparePart2(first, second)) {
        count++;
      }
    }
    System.out.println(count);
  }

  static boolean comparePart1(Set<Integer> first, Set<Integer> second) {
    return first.containsAll(second) || second.containsAll(first);
  }

  static boolean comparePart2(Set<Integer> first, Set<Integer> second) {
    return !Sets.intersection(first, second).isEmpty();
  }

  static Set<Integer> makeList(String range) {
    String[] rangeArray = range.split("-");
    return IntStream.range(Integer.parseInt(rangeArray[0]), Integer.parseInt(rangeArray[1]) + 1)
        .mapToObj(Integer::new)
        .collect(Collectors.toSet());
  }
}
