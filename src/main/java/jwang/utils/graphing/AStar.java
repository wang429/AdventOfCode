package jwang.utils.graphing;

import jwang.utils.graphing.Node;

import java.util.*;
import java.util.function.*;

public class AStar {
  public static List<Node> reconstructPath(Map<Node, Node> nodes, Node current) {
    List<Node> path = new ArrayList<>();
    path.add(current);
    while(nodes.containsKey(current)) {
      current = nodes.remove(current);
      path.add(0, current);
    }
    return path;
  }

  public static List<Node> aStar(Node start, Node target) {
    return aStar(start, target::equals, node -> node.heuristic(target));
  }

  public static List<Node> aStar(Node start, Predicate<Node> endCondition, ToDoubleFunction<Node> heuristic) {
    // distances from start
    Map<Node, Double> knownDistances = new HashMap<>();
    knownDistances.put(start, 0D);

    // cheapest best guesses
    Map<Node, Double> estimations = new HashMap<>();
    estimations.put(start, heuristic.applyAsDouble(start));

    Map<Node, Node> cameFrom = new HashMap<>();

    PriorityQueue<Node> openList = new PriorityQueue<>(
        Comparator.comparingDouble(n -> estimations.getOrDefault(n, Double.POSITIVE_INFINITY)));
    openList.add(start);

    while(!openList.isEmpty()){
      Node node = openList.poll();
      if (endCondition.test(node)){
        return reconstructPath(cameFrom, node);
      }

      for (Node.Edge edge : node.neighbors) {
        double currentKnownDistance = estimations.getOrDefault(node, Double.POSITIVE_INFINITY) + edge.weight;
        Node neighbor = edge.node;
        if (currentKnownDistance < knownDistances.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
          cameFrom.put(neighbor, node);
          knownDistances.put(neighbor, currentKnownDistance);
          estimations.put(neighbor, currentKnownDistance + heuristic.applyAsDouble(neighbor));
          if (!openList.contains(neighbor)) {
            openList.add(neighbor);
          }
        }
      }
    }
    return null;
  }
}
