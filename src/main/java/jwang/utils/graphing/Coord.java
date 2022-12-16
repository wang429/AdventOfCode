package jwang.utils.graphing;

import jwang.utils.NamedEntity;

import java.util.Objects;

// TODO generify away from Day12
public class Coord extends NamedEntity {
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
