package jwang.y2022;

import com.google.common.collect.ImmutableMap;
import utils.Utils;

import java.util.*;

public class Day11Part1 {
  static List<String> test = Arrays.asList(
    "Monkey 0:",
    "  Starting items: 79, 98",
    "  Operation: new = old * 19",
    "  Test: divisible by 23",
    "    If true: throw to monkey 2",
    "    If false: throw to monkey 3",
    "",
    "Monkey 1:",
    "  Starting items: 54, 65, 75, 74",
    "  Operation: new = old + 6",
    "  Test: divisible by 19",
    "    If true: throw to monkey 2",
    "    If false: throw to monkey 0",
    "",
    "Monkey 2:",
    "  Starting items: 79, 60, 97",
    "  Operation: new = old * old",
    "  Test: divisible by 13",
    "    If true: throw to monkey 1",
    "    If false: throw to monkey 3",
    "",
    "Monkey 3:",
    "  Starting items: 74",
    "  Operation: new = old + 3",
    "  Test: divisible by 17",
    "    If true: throw to monkey 0",
    "    If false: throw to monkey 1"
  );

  public static void main(String... args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day11Input.txt");
//    input = test;
    List<Monkey> monkeys = new ArrayList<>();

    for (int i = 0; i < input.size(); i+=7) {
      Queue<Integer> startingItems = new ArrayDeque<>();
      Arrays.stream(input.get(i+1).trim().split(": ")[1].split(", "))
          .mapToInt(Integer::parseInt)
          .forEach(startingItems::add);
      String[] operation = input.get(i+2).trim().split(" ");
      Op op = Op.OPS.get(operation[4]);
      String change = operation[5];
      int testVal = Integer.parseInt(input.get(i+3).trim().split(" ")[3]);
      int ifT = Integer.parseInt(input.get(i+4).trim().split(" ")[5]);
      int ifF = Integer.parseInt(input.get(i+5).trim().split(" ")[5]);
      monkeys.add(new Monkey(op, change, testVal, ifT, ifF, startingItems));
    }

    final int rounds = 20;
    for (int round = 0; round < rounds; round++) {
      for (Monkey monkey : monkeys) {
        monkey.activity+=monkey.items.size();
        while(!monkey.items.isEmpty()) {
          int item = monkey.items.poll();
          // update worry
          int dW = monkey.change.equals("old") ? item : Integer.parseInt(monkey.change);
          item = monkey.op.perform(item, dW);
          // get bored
          item /= 3;
          // throw item
          if (item % monkey.testVal == 0) {
            monkeys.get(monkey.ifT).items.add(item);
          }
          else {
            monkeys.get(monkey.ifF).items.add(item);
          }
        }
      }
    }

    int first = 0;
    int second = 0;
    for (Monkey monkey : monkeys) {
      if (monkey.activity > first) {
        second = first;
        first = monkey.activity;
      }
      else if (monkey.activity > second) {
        second = monkey.activity;
      }
    }

    System.out.println(first * second);
  }

  static class Monkey {
    Op op;
    String change;
    int testVal;
    int ifT;
    int ifF;
    Queue<Integer> items;
    int activity;

    Monkey(Op op, String change, int testVal, int ifT, int ifF, Queue<Integer> items) {
      this.op = op;
      this.change = change;
      this.testVal = testVal;
      this.ifT = ifT;
      this.ifF = ifF;
      this.items = items;
      this.activity = 0;
    }
  }

  enum Op {
    ADD {
      @Override
      int perform(int a, int b) {
        return a + b;
      }
    },
    DIV {
      @Override
      int perform(int a, int b) {
        return a / b;
      }
    },
    MUL {
      @Override
      int perform(int a, int b) {
        return a * b;
      }
    },
    SUB {
      @Override
      int perform(int a, int b) {
        return a - b;
      }
    };

    static final ImmutableMap<String, Op> OPS = ImmutableMap.of("+", ADD, "/", DIV, "*", MUL, "-", SUB);

    abstract int perform(int a, int b);
  }
}
