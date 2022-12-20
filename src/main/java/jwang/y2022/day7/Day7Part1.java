package jwang.y2022.day7;

import jwang.utils.Utils;

import java.io.IOException;
import java.util.*;

public class Day7Part1 {

  public static final List<String> TEST = Arrays.asList(
      "$ cd /",
      "$ ls",
      "dir a",
      "14848514 b.txt",
      "8504156 c.dat",
      "dir d",
      "$ cd a",
      "$ ls",
      "dir e",
      "29116 f",
      "2557 g",
      "62596 h.lst",
      "$ cd e",
      "$ ls",
      "584 i",
      "$ cd ..",
      "$ cd ..",
      "$ cd d",
      "$ ls",
      "4060174 j",
      "8033020 d.log",
      "5626152 d.ext",
      "7214296 k"
  );

  public static void main(String[] args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day7Input.txt");
//    List<String> input = TEST;

    Directory start = new Directory(null);
    start.directories.put("/", new Directory("/"));
    Stack<Directory> path = new Stack<>();
    Directory curr = start;
    for (String line : input) {
      String[] split = line.split(" ");
      if (split[0].equals("$")) {
        if (split[1].equals("cd")) {
          if (split[2].equals("..")) {
            curr = path.pop();
          }
          else {
            path.push(curr);
            curr = curr.directories.get(split[2]);
          }
        }
      }
      else if (split[0].equals("dir")) {
        Directory subDir = new Directory(split[1]);
        curr.directories.put(subDir.name, subDir);
      }
      else {
        long fileSize = Long.parseLong(split[0]);
        curr.files.put(split[1], fileSize);
        curr.size += fileSize;
      }
    }

    //TODO integrate summing into the above...
    Queue<Directory> bfs = new ArrayDeque<>();
    bfs.add(start);
    long sum = 0;
    while(!bfs.isEmpty()) {
      curr = bfs.poll();
      for (Directory subDir : curr.directories.values()) {
        bfs.add(subDir);
      }
      long size = curr.getTotalSize();
      if (size <= 100_000) {
        sum += size;
      }
    }
    System.out.println(sum);
  }

  static class Directory{
    String name;
    long size = 0;
    Map<String, Long> files = new HashMap<>();
    Map<String, Directory> directories = new HashMap<>();

    Directory(String name) {
      this.name = name;
    }

    long getTotalSize() {
      long sum = 0;
      for (long size : files.values()) {
        sum += size;
      }
      for (Directory dir : directories.values()) {
        sum += dir.getTotalSize();
      }
      return sum;
    }
  }
}
