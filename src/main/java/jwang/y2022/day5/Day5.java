package jwang.y2022.day5;

import jwang.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day5 {
  public static void main(String... args) throws IOException {
    List<String> stacksInput = Utils.readFromFile("src/main/resources/jwang/y2022/Day5Input1.txt");
    List<String> steps = Utils.readFromFile("src/main/resources/jwang/y2022/Day5Input2.txt");
//    List<String> stacksInput = Arrays.asList(
//        "    [D]    ",
//        "[N] [C]    ",
//        "[Z] [M] [P]",
//        " 1   2   3 ");
//    List<String> steps = Arrays.asList(
//        "move 1 from 2 to 1",
//        "move 3 from 1 to 3",
//        "move 2 from 2 to 1",
//        "move 1 from 1 to 2");

    List<Stack<Character>> stacks = buildStacks(stacksInput);
    printStacks(stacks);

    for (String step : steps) {
      String[] parsed = step.split(" ");
      int moveCount = Integer.valueOf(parsed[1]);
      int init = Integer.valueOf(parsed[3]);
      int dest = Integer.valueOf(parsed[5]);

      Stack<Character> initStack = stacks.get(init);
      Stack<Character> destStack = stacks.get(dest);
      System.out.printf("%s: %d%n\t%d: %s%n\t%d: %s%n--------------------%n",
          step, moveCount, init, initStack, dest, destStack);

//      moveCratesPart1(moveCount, initStack, destStack);
      moveCratesPart2(moveCount, initStack, destStack);

      printStacks(stacks);
    }

    StringBuilder sb = new StringBuilder();
    for (Stack<Character> stack : stacks) {
      if (stack != null) {
        sb.append(stack.isEmpty() ? ' ' : stack.peek());
      }
    }
    System.out.println(sb);
  }

  static void moveCratesPart1(int moveCount, Stack<Character> initStack, Stack<Character> destStack) {
    for (int i = 0; i < moveCount; i++) {
      destStack.push(initStack.pop());
    }
  }

  static void moveCratesPart2(int moveCount, Stack<Character> initStack, Stack<Character> destStack) {
    Stack<Character> temp = new Stack<>();
    for (int i = 0; i < moveCount; i++) {
      temp.push(initStack.pop());
    }
    while(!temp.isEmpty()) {
      destStack.push(temp.pop());
    }
  }

  static void printStacks(List<Stack<Character>> stacks) {
    for (int i = 1; i < stacks.size(); i++) {
      System.out.println(i + ": " + stacks.get(i).toString());
    }
  }

  public static List<Stack<Character>> buildStacks(List<String> stacksInput) {
    int n = stacksInput.get(0).length();
    List<Stack<Character>> stacks = new ArrayList<>();
    for (int i = 1; i < n; i+=4) { // iterate over chars in string
      Stack<Character> stack = new Stack<>();
      for (int j = 1; j < stacksInput.size(); j++) { // iterate over strings
        char c = stacksInput.get(stacksInput.size() - j - 1).charAt(i);
        if (c == ' ') {
          break;
        }
        stack.push(c);
      }
      stacks.add(stack);
    }
    stacks.add(0, null);
    return stacks;
  }
}
