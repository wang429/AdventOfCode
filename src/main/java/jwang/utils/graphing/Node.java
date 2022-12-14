package jwang.utils.graphing;

import jwang.utils.NamedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node<T extends NamedEntity> {

  public final T entity;

  public final List<Edge> neighbors;

  public Node(T entity){
    this.entity = entity;
    this.neighbors = new ArrayList<>();
  }

  public class Edge {
    Edge(double weight, Node<T> node){
      this.weight = weight;
      this.node = node;
    }

    public double weight;
    public final Node<T> node;

    @Override
    public String toString() {
      return String.format("(%s, %3s)", node, weight);
    }
  }

  public void addBranch(double weight, Node<T> node){
    Edge newEdge = new Edge(weight, node);
    neighbors.add(newEdge);
  }

  @Override
  public String toString() {
    return getId();
  }

  public String getId() {
    return entity.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node<?> node = (Node<?>) o;
    return entity.equals(node.entity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entity);
  }
}
