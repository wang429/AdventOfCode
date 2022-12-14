package jwang.y2022;

import com.google.gson.*;
import jwang.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day13 {
  static List<String> test = Arrays.asList(
      "[1,1,3,1,1]",
      "[1,1,5,1,1]",
      "",
      "[[1],[2,3,4]]",
      "[[1],4]",
      "",
      "[9]",
      "[[8,7,6]]",
      "",
      "[[4,4],4,4]",
      "[[4,4],4,4,4]",
      "",
      "[7,7,7,7]",
      "[7,7,7]",
      "",
      "[]",
      "[3]",
      "",
      "[[[]]]",
      "[[]]",
      "",
      "[1,[2,[3,[4,[5,6,7]]]],8,9]",
      "[1,[2,[3,[4,[5,6,0]]]],8,9]"
  );

  static List<String> test2 = Arrays.asList(
      "[[],[],[[[3,6,6,1,5],9],[[7,2],[],[9,2,4],[7,8,10,0,3]],4,[[6,9,5,2]]],[6,6,[[4,10]]]]",
      "[[],[6]]",
      "",
      "[[],1]",
      "[[],2]",
      "",
      "[[],2]",
      "[[],1]",
      "",
      "[[1],1]",
      "[[1],2]",
      "",
      "[[1],2]",
      "[[1],1]",
      "",
      "[[0],[[7]]]",
      "[[[[0,4],[0]],[],[0,4,0,[5,4]],5],[[],[7,8,4,[7],[10,5]]],[1,1,[[],[],[3,0,1,3]],6],[[],[],7,7,8]]"
  );

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day13Input.txt");
//    input = test;
//    input = test2;

    System.out.println("Part1");
    long start = System.currentTimeMillis();
    part1(input);
    System.out.println(System.currentTimeMillis() - start);
    System.out.println("------------");
    System.out.println("Part2");
    start = System.currentTimeMillis();
    part2(input);
    System.out.println(System.currentTimeMillis() - start);
  }

  private static void part1(List<String> input) {
    int indexSum = 0;
    for (int i = 0; i < input.size(); i+=3) {
      String a = input.get(i);
      JsonArray jsonA = JsonParser.parseString(a).getAsJsonArray();
      String b = input.get(i+1);
      JsonArray jsonB = JsonParser.parseString(b).getAsJsonArray();
      System.out.println("--------------------------");
      System.out.println("Pair: " + (i/3 + 1) + ", line: " + (i+1));
      System.out.println("Compare " + a + " vs " + b);
      boolean rightOrder = areJsonArraysInOrder(jsonA, jsonB) <= 0;
      if(rightOrder) {
        System.out.println("right order");
        indexSum += i/3 + 1;
      } else {
        System.out.println("wrong order");
      }
      System.out.println();
    }
    System.out.println(indexSum);
  }

  private static void part2(List<String> input) {
    SortedSet<String> sortedPackets = new TreeSet<>(
        (a, b) -> areJsonArraysInOrder(JsonParser.parseString(a).getAsJsonArray(),
            JsonParser.parseString(b).getAsJsonArray()));
    for (String line : input) {
      if (line.isEmpty()) {
        continue;
      }
      sortedPackets.add(line);
    }
    sortedPackets.add("[[2]]");
    sortedPackets.add("[[6]]");
    int first = -1;
    int second = -1;
    int i = 1;
    for (String packet : sortedPackets) {
      if (packet.equals("[[2]]") && first < 0) {
        first = i;
      }
      else if (packet.equals("[[6]]") && second < 0) {
        second = i;
      }
      i++;
    }
    System.out.println(first * second);
  }

  static int areJsonArraysInOrder(JsonArray a, JsonArray b) {
    if (a.isEmpty()) {
      if (b.isEmpty()) {
        return 0;
      }
      else {
        return -1;
      }
    }
    for (int i = 0; i < a.size(); i++) {
      if (i >= b.size()) {
        // everything before now is the same, but because b is out of items, wrong order
        return 1;
      }
      JsonElement aElement = a.get(i);
      boolean aIsArray = aElement.isJsonArray();
      JsonElement bElement = b.get(i);
      boolean bIsArray = bElement.isJsonArray();
      if (!aIsArray && !bIsArray) {
        // both are ints, lower int should come first
        int value = Comparator.comparingInt(JsonElement::getAsInt).compare(aElement, bElement);
        if (value != 0) {
          return value;
        } else {
          // ints are same, go next
        }
      } else if (!aIsArray) {
        // b is array, a is int
        JsonArray aElementArray = new JsonArray();
        aElementArray.add(aElement);
        int value = areJsonArraysInOrder(aElementArray, bElement.getAsJsonArray());
        if (value != 0) {
          return value;
        } else {
          // arrays are same, go next
        }
      } else if (!bIsArray) {
        // a is array, b is int
        JsonArray bElementArray = new JsonArray();
        bElementArray.add(bElement);
        int value = areJsonArraysInOrder(aElement.getAsJsonArray(), bElementArray);
        if (value != 0) {
          return value;
        } else {
          // arrays are same, go next
        }
      } else {
        // both are arrays
        int value = areJsonArraysInOrder(aElement.getAsJsonArray(), bElement.getAsJsonArray());
        if (value != 0) {
          return value;
        } else {
          // arrays are same, go next
        }
      }
    }
    if (a.size() < b.size()) {
      return -1;
    }
    return 0;
  }
}
