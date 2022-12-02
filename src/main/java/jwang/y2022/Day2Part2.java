package jwang.y2022;

import com.google.common.collect.Lists;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day2Part2 {
  static List<String> testInput = Lists.newArrayList(
      "A Y",
      "B X",
      "C Z"
  );

  public static void main(String... args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day2Input.txt");

    int score = 0;
    for (String line : input) {
      String[] round = line.split(" ");
      RPS op = RPS.getFromSymbol(round[0]);
      Results me = Results.getFromSymbol(round[1]);
      score += me.getPointsFromForcedPlay(op);
    }
    System.out.println(score);
  }

  enum RPS{
    R(1),
    P(2),
    S(3);

    int points;

    RPS(int points) {
      this.points = points;
    }

    static RPS getFromSymbol(String symbol) {
      switch (symbol) {
        case "A": return R;
        case "B": return P;
        case "C": return S;
        default: throw new RuntimeException("Unknown input. DIE 1");
      }
    }
  }

  enum Results {
    WIN(6, RPS.P, RPS.S, RPS.R),
    TIE(3, RPS.R, RPS.P, RPS.S),
    LOSS(0, RPS.S, RPS.R, RPS.P);

    int points;
    RPS r;
    RPS p;
    RPS s;

    Results(int points, RPS r, RPS p, RPS s) {
      this.points = points;
      this.r = r;
      this.p = p;
      this.s = s;
    }

    static Results getFromSymbol(String symbol) {
      switch (symbol) {
        case "X": return LOSS;
        case "Y": return TIE;
        case "Z": return WIN;
        default: throw new RuntimeException("Unknown input. DIE 2");
      }
    }

    int getPointsFromForcedPlay(RPS other) {
      switch (other) {
        case R: return points + r.points;
        case P: return points + p.points;
        case S: return points + s.points;
        default: throw new RuntimeException("UHHHHHHH???????????");
      }
    }
  }
}
