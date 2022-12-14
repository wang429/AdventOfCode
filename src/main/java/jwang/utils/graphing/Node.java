package jwang.utils.graphing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

  public final Coord coord;

  public final List<Edge> neighbors;

  public Node(Coord coord){
    this.coord = coord;
    this.neighbors = new ArrayList<>();
  }

  // TODO generify
  public double heuristic(Node target) {
    return Math.max(Math.abs(target.coord.i - coord.i), Math.abs(target.coord.j - coord.j));
  }

  public static class Edge {
    Edge(int weight, Node node){
      this.weight = weight;
      this.node = node;
    }

    public final double weight;
    public final Node node;

    @Override
    public String toString() {
      return String.format("(%s, %3s)", node, weight);
    }
  }

  public void addBranch(int weight, Node node){
    Edge newEdge = new Edge(weight, node);
    neighbors.add(newEdge);
  }

  @Override
  public String toString() {
    return getId();
  }

  public String getId() {
    return coord.name;
  }

  public static class Coord {
    public final int i;
    public final int j;
    public final char c;
    final String name;

    public Coord(int i, int j, char c) {
      this.i = i;
      this.j = j;
      this.c = c;
      this.name = String.format("(%3s, %3s, %c)", i, j, c);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Coord coord = (Coord) o;
      return i == coord.i && j == coord.j && c == coord.c;
    }

    @Override
    public int hashCode() {
      return Objects.hash(i, j, c);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return coord.equals(node.coord);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coord);
  }
}
