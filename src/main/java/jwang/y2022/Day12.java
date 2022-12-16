package jwang.y2022;

import jwang.utils.*;
import jwang.utils.graphing.*;

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Day12 {
  static String[] test = {
      "Sabqponm",
      "abcryxxl",
      "accszExk",
      "acctuvwj",
      "abdefghi"
  };

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day12Input.txt");
//        input = Arrays.asList(test);

    char[][] map = new char[input.size()][];
    for (int i = 0; i < input.size(); i++) {
      map[i] = input.get(i).toCharArray();
    }

    // part 1
    Node<Coord> start = null;
    Node<Coord> target = null;
    SortedMap<Coord, Node<Coord>> part1Nodes = new TreeMap<>((u, v) -> (u.i - v.i) * 1000 + (u.j - v.j));
    // part 2
    Node<Coord> part2Start = null;
    SortedMap<Coord, Node<Coord>> part2Nodes = new TreeMap<>((u, v) -> (u.i - v.i) * 1000 + (u.j - v.j));
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        char c = map[i][j];
        switch (c) {
          case 'S':
            c = 'a';
            map[i][j] = c;
            start = part1Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            part2Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            break;
          case 'E':
            c = 'z';
            map[i][j] = c;
            target = part1Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            part2Start = part2Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            break;
          default:
            part1Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            part2Nodes.computeIfAbsent(new Coord(i, j, c), Node::new);
            break;
        }
      }
    }

    // part 1
    populateNeighbors(map, part1Nodes, (nc, c) -> nc - c <= 1);
    // part 2
    populateNeighbors(map, part2Nodes, (nc, c) -> c - nc <= 1);

    if (start == null || target == null) {
      return;
    }

    List<Node<Coord>> path = AStar.aStar(start, target, Day12::heuristic);
    if (path == null) {
      throw new IllegalStateException("null path");
    }
    System.out.println("Part 1: " + path.size() + " " + path);

    path = AStar.aStar(part2Start, n -> n.entity.c == 'a', n -> Math.pow(n.entity.c - (double) 'a', 2));
    if (path == null) {
      throw new IllegalStateException("null path");
    }
    System.out.println("Part 2: " + path.size() + " " + path);

    printMap(map, path.stream().map(n -> n.entity).collect(Collectors.toSet()));
  }

  private static void populateNeighbors(char[][] map, SortedMap<Coord, Node<Coord>> nodes, BiPredicate<Character, Character> tester) {
    for (Map.Entry<Coord, Node<Coord>> entry : nodes.entrySet()) {
      Coord coord = entry.getKey();
      Node<Coord> node = entry.getValue();

      for (DIR dir : DIR.values()) {
        int ni = coord.i + dir.i;
        int nj = coord.j + dir.j;
        if (0 > ni || 0 > nj || map.length <= ni || map[ni].length <= nj) {
          continue;
        }
        char nc = map[ni][nj];
        if (tester.test(nc, coord.c)) {
          Node<Coord> neighbor = nodes.computeIfAbsent(new Coord(ni, nj, nc), Node::new);
          node.addBranch(1, neighbor);
        }
      }
    }
  }

  // TODO figure out the name of this heuristic
  public static double heuristic(Node<Coord> start, Node<Coord> target) {
    return Math.max(Math.abs(target.entity.i - start.entity.i), Math.abs(target.entity.j - start.entity.j));
  }

  static void printMap(char[][] map, Set<Coord> path) {
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        if (path.contains(new Coord(i, j, map[i][j]))) {
//                    System.out.print((map[i][j] + "").toUpperCase(Locale.ROOT));
          System.out.print("#");
        } else {
          System.out.print(map[i][j]);
        }
      }
      System.out.println();
    }
  }

  enum DIR {
    N(-1, 0), W(0, -1), S(1, 0), E(0, 1);

    int i;
    int j;

    DIR(int i, int j) {
      this.i = i;
      this.j = j;
    }
  }
}
