package jwang.y2022;

import com.google.common.collect.*;
import jwang.utils.NamedEntity;
import jwang.utils.Utils;
import jwang.utils.graphing.AStar;
import jwang.utils.graphing.Node;

import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
  static List<String> test = Arrays.asList(
      "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB",
      "Valve BB has flow rate=13; tunnels lead to valves CC, AA",
      "Valve CC has flow rate=2; tunnels lead to valves DD, BB",
      "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE",
      "Valve EE has flow rate=3; tunnels lead to valves FF, DD",
      "Valve FF has flow rate=0; tunnels lead to valves EE, GG",
      "Valve GG has flow rate=0; tunnels lead to valves FF, HH",
      "Valve HH has flow rate=22; tunnel leads to valve GG",
      "Valve II has flow rate=0; tunnels lead to valves AA, JJ",
      "Valve JJ has flow rate=21; tunnel leads to valve II"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day16Input.txt");
    input = test;
    boolean testMode = input.equals(test);

    Map<String, Node<Valve>> nodes = new HashMap<>();
    Map<String, List<String>> nodeNeighbors = new HashMap<>();
    for(String line : input) {
      String[] split = line.split(" ");

      Valve valve = new Valve(
          split[1],
          false,
          Integer.parseInt(split[4].substring(5, split[4].length() - 1))
      );
      List<String> neighborNames = Arrays.asList(split).subList(9, split.length).stream().map(s -> s.replace(",", "")).collect(Collectors.toList());

      nodes.put(valve.name, new Node<>(valve));
      nodeNeighbors.put(valve.name, neighborNames);
    }

    for (Node<Valve> node : nodes.values()) {
      for (String neighbor : nodeNeighbors.get(node.getId())) {
        node.addBranch(calcWeight(nodes.get(neighbor), 1D), nodes.get(neighbor));
      }
    }

    // shortest path for each node to each other node
    ListMultimap<String, NodePathWrapper> shortestPaths = ArrayListMultimap.create();
    for (Node<Valve> node : nodes.values()) {
      for (Node<Valve> node2 : nodes.values()) {
        if (node == node2 || node2.entity.rate == 0) {
          continue;
        }
        List<Node<Valve>> path = AStar.aStar(node, node2, (u, v) -> 1);
        shortestPaths.put(node.getId(), new NodePathWrapper(path.get(path.size()-1).getId(), path));
      }
    }

    System.out.println(nodes.values().stream().map(n -> n.entity).collect(Collectors.toList()));

    Node<Valve> curr = nodes.get("AA");
    long start = System.currentTimeMillis();
    System.out.println(part1(shortestPaths, curr, 0, 30, new ArrayList<>()));
    System.out.println(System.currentTimeMillis() - start);
    start = System.currentTimeMillis();
    System.out.println(part1Redux(nodes, curr));
    System.out.println(System.currentTimeMillis() - start);
    start = System.currentTimeMillis();
    System.out.println(f(nodes, new HashMap<>(90_000_000), 26, curr, new ArrayList<>(), 26, 1));
    System.out.println(System.currentTimeMillis() - start);
  }

  static int part1(ListMultimap<String, NodePathWrapper> shortestPaths,
                   Node<Valve> curr,
                   int totalRate,
                   int timeRemaining,
                   List<String> pathTaken) {
    if (pathTaken.contains(curr.getId())) {
      return 0;
    }
    List<String> nextPath = Lists.newArrayList(pathTaken);
    nextPath.add(curr.getId());
    int releasedByValve = curr.entity.rate * timeRemaining;
    if (timeRemaining <= 0) {
      return releasedByValve;
    }
    int max = 0;
//    System.out.printf("Time remaining: %2s, rate: %3s, released: %4s, node: %s%ncurrent path taken: %s%n",
//        timeRemaining, totalRate, releasedByValve, curr, pathTaken);
    for (NodePathWrapper pathWrapper : shortestPaths.get(curr.getId())) {
      List<Node<Valve>> path = pathWrapper.path;
      Node<Valve> next = path.get(path.size() - 1);
      max = Math.max(max, part1(shortestPaths,
          next,
          totalRate + next.entity.rate,
          timeRemaining - (path.size()),
          nextPath));
    }
    return releasedByValve + max;
  }

  static int part1Redux(Map<String, Node<Valve>> nodes,
                        Node<Valve> curr) {
    return f(nodes, new HashMap<>(), 30, curr, new ArrayList<>(), 30, 0);
  }

  static int f(Map<String, Node<Valve>> nodes,
               Map<Integer, Integer> dpMap,
               int maxTime,
               Node<Valve> curr,
               List<String> opened,
               int time,
               int otherPlayers) {
    if (time == 0) {
      return otherPlayers > 0 ? f(nodes, dpMap, maxTime, nodes.get("AA"), opened, maxTime, otherPlayers-1) : 0;
    }

    int key = Objects.hash(opened, curr, time, otherPlayers);
    if (dpMap.containsKey(key)) {
      return dpMap.get(key);
    }

    int max = 0;
    int rate = curr.entity.rate;
    if (!opened.contains(curr.getId()) && rate > 0) {
      List<String> newOpened = Lists.newArrayList(opened);
      newOpened.add(curr.getId());
      max = Math.max(max, (time-1) * rate + f(nodes, dpMap, maxTime, curr, newOpened, time-1, otherPlayers));
    }
    for (Node<Valve>.Edge neighbor : curr.neighbors) {
      max = Math.max(max, f(nodes, dpMap, maxTime, neighbor.node, opened, time-1, otherPlayers));
    }

    dpMap.put(key, max);

    return max;
  }

  /**
   * returns weight of going to b from a.
   */
  static double calcWeight(Node<Valve> b, double distance) {
    return b.entity.open || distance == 0D ? 0D : b.entity.rate / (distance + 1);
  }

  static class NodePathWrapper {
    String nodeName;
    List<Node<Valve>> path;

    public NodePathWrapper(String nodeName, List<Node<Valve>> path) {
      this.nodeName = nodeName;
      this.path = path;
    }

    @Override
    public String toString() {
      return "NodePathWrapper{" +
          "nodeName='" + nodeName + '\'' +
          ", path=" + path +
          '}';
    }
  }

  static class Valve extends NamedEntity {
    boolean open;
    int rate;

    Valve(String name, boolean open, int rate) {
      this.name = name;
      this.open = open;
      this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Valve valve = (Valve) o;
      return open == valve.open &&
          rate == valve.rate &&
          Objects.equals(name, valve.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(open, rate, name);
    }

    @Override
    public String toString() {
      return "Node{" +
          "name='" + name + '\'' +
          ", open=" + open +
          ", flowRate=" + rate +
          '}';
    }
  }
}
