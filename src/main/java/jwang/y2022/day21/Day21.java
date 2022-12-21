package jwang.y2022.day21;

import jwang.utils.Utils;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

public class Day21 {
  static List<String> test = Arrays.asList(
      "root: pppw + sjmn",
      "dbpl: 5",
      "cczh: sllz + lgvd",
      "zczc: 2",
      "ptdq: humn - dvpt",
      "dvpt: 3",
      "lfqf: 4",
      "humn: 5",
      "ljgn: 2",
      "sjmn: drzm * dbpl",
      "sllz: 4",
      "pppw: cczh / lfqf",
      "lgvd: ljgn * ptdq",
      "drzm: hmdt - zczc",
      "hmdt: 32"
  );

  static List<String> test2 = Arrays.asList(
      "root: humn + aaaa",
      "aaaa: 50",
      "humn: 1"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day21Input.txt");
//    input = test;

    Map<String, String[]> jobs = new HashMap<>();
    Map<String, ToDoubleBiFunction<Double, Double>> functions = new HashMap<>();
    Map<String, ToDoubleBiFunction<Double, Double>> solveAFs = new HashMap<>();
    Map<String, ToDoubleBiFunction<Double, Double>> solveBFs = new HashMap<>();
    for (String line : input) {
      String[] split = line.split(":");
      String[] job = split[1].trim().split(" ");
      jobs.put(split[0], job);
      if (job.length == 1) {
        functions.put(split[0], (a, b) -> Double.parseDouble(job[0]));
      } else {
        ToDoubleBiFunction<Double, Double> f;
        ToDoubleBiFunction<Double, Double> l;
        ToDoubleBiFunction<Double, Double> r;
        switch (job[1]) {
          case "+" : f = Double::sum;
            l = (s, b) -> s - b;
            r = (s, a) -> s - a;
            break;
          case "-" : f = (a, b) -> a - b;
            l = Double::sum;
            r = (d, a) -> a - d;
            break;
          case "*" : f = (a, b) -> a * b;
            l = (p, b) -> p / b;
            r = (p, a) -> p / a;
            break;
          case "/" : f = (a, b) -> a / b;
            l = (d, b) -> d * b;
            r = (d, a) -> a / d;
            break;
          default: throw new IllegalStateException("bad operation " + job[1]);
        }
        functions.put(split[0], f);
        solveAFs.put(split[0], l);
        solveBFs.put(split[0], r);
      }
    }

    long start = System.currentTimeMillis();
    System.out.printf("%f%n", part1(jobs, functions));
    long end = System.currentTimeMillis();
    System.out.println(end - start);
    start = System.currentTimeMillis();
    System.out.printf("%f%n", part2(jobs, functions, solveAFs, solveBFs));
    end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  static double part1(Map<String, String[]> jobs, Map<String, ToDoubleBiFunction<Double, Double>> functions) {
    return f1(jobs, functions, "root");
  }

  private static double f1(Map<String, String[]> jobs, Map<String, ToDoubleBiFunction<Double, Double>> functions, String node) {
    String[] job = jobs.get(node);
    if (job.length == 1) {
      return functions.get(node).applyAsDouble(0D, 0D);
    }
    else {
      return functions.get(node).applyAsDouble(f1(jobs, functions, job[0]), f1(jobs, functions, job[2]));
    }
  }

  private static double part2(Map<String, String[]> jobs,
                              Map<String, ToDoubleBiFunction<Double, Double>> functions,
                              Map<String, ToDoubleBiFunction<Double, Double>> solveAFs,
                              Map<String, ToDoubleBiFunction<Double, Double>> solveBFs) {
    TreeNode root = new TreeNode("root");
    buildTree(jobs, functions, solveAFs, solveBFs, root);
    double val;
    double eval;
    if (root.l.humanSide) {
      eval = root.r.evaluate();
      val = root.l.solveForHuman(eval);
    }
    else {
      eval = root.l.evaluate();
      val = root.r.solveForHuman(eval);
    }
    System.out.println(eval);
    return val;
  }

  private static void buildTree(Map<String, String[]> jobs,
                                Map<String, ToDoubleBiFunction<Double, Double>> functions,
                                Map<String, ToDoubleBiFunction<Double, Double>> solveAFs,
                                Map<String, ToDoubleBiFunction<Double, Double>> solveBFs,
                                TreeNode node) {
    if (node.name.equals("humn")) {
      node.function = (a,b) -> a;
      node.humanSide = true;
      return;
    }
    node.function = functions.get(node.name);
    node.solveAF = solveAFs.get(node.name);
    node.solveBF = solveBFs.get(node.name);
    String[] job = jobs.get(node.name);
    if (job.length == 1) {
      node.humanSide = false;
      return;
    }

    node.l = new TreeNode(job[0]);
    buildTree(jobs, functions, solveAFs, solveBFs, node.l);
    node.r = new TreeNode(job[2]);
    buildTree(jobs, functions, solveAFs, solveBFs, node.r);
    node.humanSide = node.l.humanSide || node.r.humanSide;
  }

  static class TreeNode {
    TreeNode l;
    TreeNode r;
    ToDoubleBiFunction<Double, Double> function;
    ToDoubleBiFunction<Double, Double> solveAF;
    ToDoubleBiFunction<Double, Double> solveBF;
    String name;
    boolean humanSide;

    TreeNode(String name) {
      this.name = name;
    }

    double evaluate() {
      if (l == null) { // assume r is also null
        // leaf node
        return function.applyAsDouble(0D,0D);
      }
      return function.applyAsDouble(l.evaluate(), r.evaluate());
    }

    double solveForHuman(double d) {
      if (name.equals("humn")) {
        return d;
      }
      if (l == null) {
        return evaluate();
      }
      if (l.humanSide) {
        return l.solveForHuman(solveAF.applyAsDouble(d, r.evaluate()));
      }
      return r.solveForHuman(solveBF.applyAsDouble(d, l.evaluate()));
    }
  }
}
