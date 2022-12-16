package jwang.utils.graphing;

import jwang.utils.NamedEntity;

import java.util.*;
import java.util.function.*;

public class AStar {
  public static <T extends NamedEntity> List<Node<T>> reconstructPath(Map<Node<T>, Node<T>> nodes, Node<T> current) {
    List<Node<T>> path = new ArrayList<>();
    path.add(current);
    while(nodes.containsKey(current)) {
      current = nodes.remove(current);
      path.add(0, current);
    }
    return path;
  }

  public static <T extends NamedEntity> List<Node<T>> aStar(Node<T> start, Node<T> target, ToDoubleBiFunction<Node<T>, Node<T>> heuristic) {
    return aStar(start, target::equals, n -> heuristic.applyAsDouble(n, target));
  }

  public static <T extends NamedEntity> List<Node<T>> aStar(Node<T> start, Predicate<Node<T>> endCondition, ToDoubleFunction<Node<T>> heuristic) {
    // distances from start
    Map<Node<T>, Double> knownDistances = new HashMap<>();
    knownDistances.put(start, 0D);

    // cheapest best guesses
    Map<Node<T>, Double> estimations = new HashMap<>();
    estimations.put(start, heuristic.applyAsDouble(start));

    Map<Node<T>, Node<T>> cameFrom = new HashMap<>();

    PriorityQueue<Node<T>> openList = new PriorityQueue<>(
        Comparator.comparingDouble(n -> estimations.getOrDefault(n, Double.POSITIVE_INFINITY)));
    openList.add(start);

    while(!openList.isEmpty()){
      Node<T> node = openList.poll();
      if (endCondition.test(node)){
        return reconstructPath(cameFrom, node);
      }

      node.neighbors.forEach(edge -> {
        double currentKnownDistance = estimations.getOrDefault(node, Double.POSITIVE_INFINITY) + edge.weight;
        Node<T> neighbor = edge.node;
        if (currentKnownDistance < knownDistances.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
          cameFrom.put(neighbor, node);
          knownDistances.put(neighbor, currentKnownDistance);
          estimations.put(neighbor, currentKnownDistance + heuristic.applyAsDouble(neighbor));
          if (!openList.contains(neighbor)) {
            openList.add(neighbor);
          }
        }
      });
    }
    return null;
  }
}
