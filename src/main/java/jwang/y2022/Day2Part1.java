package jwang.y2022;

import com.google.common.collect.Lists;
import jwang.utils.Utils;

import java.io.IOException;
import java.util.List;

public class Day2Part1 {
  public static void main(String... args) throws IOException {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day2Input.txt");

    int score = 0;
    for (String line : input) {
      String[] round = line.split(" ");
      RPS op = RPS.getFromSymbol(round[0]);
      RPS me = RPS.getFromSymbol(round[1]);
      score += me.playRound(op);
    }
    System.out.println(score);
  }

  enum RPS{
    R(1, Results.TIE, Results.LOSS, Results.WIN),
    P(2, Results.WIN, Results.TIE, Results.LOSS),
    S(3, Results.LOSS, Results.WIN, Results.TIE);

    int points;
    List<String> symbols;
    Results r;
    Results p;
    Results s;

    RPS(int points, Results r, Results p, Results s, String... symbols) {
      this.points = points;
      this.symbols = Lists.newArrayList(symbols);
      this.r = r;
      this.p = p;
      this.s = s;
    }

    static RPS getFromSymbol(String symbol) {
      switch (symbol) {
        case "A":
        case "X": return R;
        case "B":
        case "Y": return P;
        case "C":
        case "Z": return S;
        default: throw new RuntimeException("Unknown input. DIE");
      }
    }

    int playRound(RPS other) {
      switch(other) {
        case R: return points + r.points;
        case P: return points + p.points;
        case S: return points + s.points;
        default: throw new RuntimeException("UHHHHHHH???????????");
      }
    }
  }

  enum Results {
    WIN(6), TIE(3), LOSS(0);

    int points;

    Results(int points) {
      this.points = points;
    }
  }
}
