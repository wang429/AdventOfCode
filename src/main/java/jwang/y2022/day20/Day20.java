package jwang.y2022.day20;

import jwang.utils.Utils;

import java.util.*;

public class Day20 {
  static final long KEY = 811589153L;

  static List<String> test = Arrays.asList(
      "1",
      "2",
      "-3",
      "3",
      "-2",
      "0",
      "4"
  );

  static List<String> test2 = Arrays.asList("0", "-1", "-1", "1");

  static List<String> test3 = Arrays.asList("1", "2", "-3", "3", "-2", "0", "-1");

  static List<String> test4 = Arrays.asList(
      "0",
      "4",
      "5",
      "1",
      "2",
      "3"
  );

  public static void main(String[] args) throws Exception{
    List<String> fileInput = Utils.readFromFile("src/main/resources/jwang/y2022/Day20Input.txt");
    List<String> input = fileInput;
    boolean testMode = input != fileInput;

    long start = System.currentTimeMillis();
    part1(input);
    long end = System.currentTimeMillis();
    System.out.println(end - start);
    start = System.currentTimeMillis();
    part2(input);
    end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  private static void part1(List<String> input) {
    List<Node> nodes = new ArrayList<>(input.size());
    Node head = null;
    Node prev = null;
    Node zero = null;
    int index = 0;
    for (String line : input) {
      Node curr = new Node(head, head, Long.parseLong(line.trim()), index);
      if (prev != null) {
        prev.next = curr;
      }
      curr.prev = prev;
      prev = curr;
      if (head == null) {
        head = curr;
      }
      else {
        head.prev = curr;
      }
      if (curr.modVal == 0) {
        zero = curr;
      }
      index++;
      nodes.add(curr);
    }
    if (zero == null) {
      throw new IllegalStateException("no 0 element found");
    }

    modVals(nodes);

    for (Node n : nodes) {
      if (n.modVal == 0) {
        continue;
      }
      n.move(n.modVal);
    }
    printSum(zero);
  }

  private static void part2(List<String> input) {
    List<Node> nodes = new ArrayList<>(input.size());
    Node head = null;
    Node prev = null;
    Node zero = null;
    int index = 0;
    for (String line : input) {
      Node curr = new Node(head, head, Long.parseLong(line.trim()) * KEY, index);
      if (prev != null) {
        prev.next = curr;
      }
      curr.prev = prev;
      prev = curr;
      if (head == null) {
        head = curr;
      }
      else {
        head.prev = curr;
      }
      if (curr.modVal == 0) {
        zero = curr;
      }
      index++;
      nodes.add(curr);
    }
    if (zero == null) {
      throw new IllegalStateException("no 0 element found");
    }

    modVals(nodes);

    for (int i = 0; i < 10; i++) {
      for (Node n : nodes) {
        if (n.modVal == 0) {
          continue;
        }
        n.move(n.modVal);
      }
    }
    printSum(zero);
  }

  private static void modVals(List<Node> nodes) {
    int size = nodes.size();
    for (Node node : nodes) {
      node.modVal %= (size-1);
      if (node.modVal < 0) {
        node.modVal += (size-1);
      }
    }
  }

  private static void printSum(Node zero) {
    // find 1000, 2000, 3000th item after zero
    Node n = zero;
    long sum = 0;
    for (int i = 1; i <= 3000; i++) {
      n = n.next;
      if (i % 1000 == 0) {
        sum += n.val;
      }
    }
    System.out.println(sum);
  }

  private static void print(boolean print, Node start, long i, boolean full) {
    if (print) {
      System.out.print(i + ": ");
      Node n = start;
      do {
        System.out.print((full ? n : n.modVal) + ", ");
        n = n.next;
      }
      while (!n.equals(start));
      System.out.println();
    }
  }

  static class Node {
    Node next;
    Node prev;
    long val;
    long modVal;
    int index;

    Node(Node next, Node prev, long val, int index) {
      this.next = next;
      this.prev = prev;
      this.val = val;
      this.modVal = val;
      this.index = index;
    }

    Node travel(long n) {
      Node ret = this;
      for (int j = 0; j < n; j++) {
        ret = ret.next;
      }
      return ret;
    }

    private void move(long n) {
      Node newPrev = travel(n);
      Node newNext = newPrev.next;

      Node oldPrev = prev;
      Node temp = this;
      Node oldNext = next;

      oldPrev.next = oldNext;
      oldNext.prev = oldPrev;

      newNext.prev = temp;
      newPrev.next = temp;

      next = newNext;
      prev = newPrev;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Node node = (Node) o;
      return modVal == node.modVal && val == node.val &&
          Objects.equals(next, node.next) && Objects.equals(prev, node.prev) && index == node.index;
    }

    @Override
    public int hashCode() {
      return Objects.hash(modVal, val, index);
    }

    @Override
    public String toString() {
      return "Node" + index + "{" +
          (prev == null ? "null" : prev.modVal) +
          ", " + modVal +
          ", " + val +
          ", " + (next == null ? "null" : next.modVal) +
          '}';
    }
  }
}
