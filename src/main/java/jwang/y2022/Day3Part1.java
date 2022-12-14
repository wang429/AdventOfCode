package jwang.y2022;

import com.google.common.collect.Sets;
import jwang.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3Part1 {
  static List<Character> chars = Arrays.asList(
      'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
      'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
  );

  public static void main(String... args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day3Input.txt");

    int score = 0;
    for (String line : input) {
      String first = line.substring(0,line.length()/2);
      String second = line.substring(line.length()/2);
      Set<Character> intersection = intersectStringChars(first, second);
      for (Character c : intersection) {
        score += findPriority(c);
      }
    }
    System.out.println(score);
  }

  static int findPriority(char c) {
    return chars.indexOf(c) + 1;
  }

  static Set<Character> intersectStringChars(String first, String second) {
    return Sets.intersection(
        first.chars().mapToObj(c -> (char)c).collect(Collectors.toSet()),
        second.chars().mapToObj(c -> (char)c).collect(Collectors.toSet()));
  }
}
