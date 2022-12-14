package jwang.y2022;

import jwang.utils.Utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class Day6 {
  static String[] EXAMPLES = {
      "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
      "bvwbjplbgvbhsrlpgdmjqwftvncz",
      "nppdvjthqldpwncqszvftbrmjlhg",
      "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
      "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
  };

  public static void main(String[] args) throws IOException {
    for (String example : EXAMPLES) {
      findSignalCharacter(example, 14);
    }

    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day6Input.txt");
    findSignalCharacter(input.get(0), 14);
  }

  static void findSignalCharacter(String input, int distinct) {
    for (int i = 0; i < input.length() - distinct ; i++) {
      HashSet<Character> characters = new HashSet<>();
      for (int j = 0; j < distinct; j++) {
        characters.add(input.charAt(i + j));
      }
      if (characters.size() == distinct) {
        System.out.println(i + distinct);
        break;
      }
    }
  }
}
