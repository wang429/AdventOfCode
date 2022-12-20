package jwang.y2022.day19;

import jwang.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day19 {
  static List<String> test = Arrays.asList(
      "Blueprint 1:" +
        " Each ore robot costs 4 ore." +
        " Each clay robot costs 2 ore." +
        " Each obsidian robot costs 3 ore and 14 clay." +
        " Each geode robot costs 2 ore and 7 obsidian.",
      "Blueprint 2:" +
        " Each ore robot costs 2 ore." +
        " Each clay robot costs 3 ore." +
        " Each obsidian robot costs 3 ore and 8 clay." +
        " Each geode robot costs 3 ore and 12 obsidian."
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day19Input.txt");
//    input = test;
    boolean testMode = input.equals(test);

    List<Blueprint> blueprints = new ArrayList<>();
    for (String line : input) {
      String[] split = line.split(" ");
      Blueprint bp = new Blueprint(
          Integer.parseInt(split[1].substring(0,split[1].length()-1)),
          new BotCost(Integer.parseInt(split[6]), 0, 0),
          new BotCost(Integer.parseInt(split[12]), 0, 0),
          new BotCost(Integer.parseInt(split[18]), Integer.parseInt(split[21]), 0),
          new BotCost(Integer.parseInt(split[27]), 0, Integer.parseInt(split[30]))
      );
      blueprints.add(bp);
    }

    System.out.println(blueprints);

    long start = System.currentTimeMillis();
    long retVal = part1(blueprints);
    long end = System.currentTimeMillis();
    System.out.println(retVal);
    System.out.println(end - start);

    start = System.currentTimeMillis();
    retVal = part2(blueprints);
    end = System.currentTimeMillis();
    System.out.println(retVal);
    System.out.println(end - start);
  }

  static long part1(List<Blueprint> blueprints) {
    long retVal = 0L;
    for (Blueprint bp : blueprints) {
      retVal += bp.id * (long) f(bp, 24, 1, 0, 0, 0, 0, 0, 0, 0);
    }
    return retVal;
  }

  static long part2(List<Blueprint> blueprints) {
    long retVal = 1L;
    for (Blueprint bp : blueprints.subList(0, 3)) {
      retVal *= f(bp, 30, 1, 0, 0, 0, 0, 0, 0, 0);
    }
    return retVal;
  }

  static int f(Blueprint bp, int timeRemaining,
               int oreBot, int clayBot, int obBot, int geodeBot,
               int oreAmt, int clayAmt, int obAmt, int geodeAmt) {
//    System.out.printf("%2s\t%2s\t%2s\t%2s\t%2s\t%2s\t%2s\t%2s\t%2s%n", timeRemaining,
//        oreBot, clayBot, obBot, geodeBot,
//        oreAmt, clayAmt, obAmt, geodeAmt);
    if (timeRemaining <= 0) {
      return geodeAmt;
    }
    int newTime = timeRemaining - 1;
    int newOreAmt = oreAmt + oreBot;
    int newClayAmt = clayAmt + clayBot;
    int newObAmt = obAmt + obBot;
    int newGeodeAmt = geodeAmt + geodeBot;
    int max = f(bp, newTime, oreBot, clayBot, obBot, geodeBot,
        newOreAmt, newClayAmt, newObAmt, newGeodeAmt);
    if (obAmt >= bp.geode.ob &&
        oreAmt >= bp.geode.ore) {
      max = Math.max(max, f(bp, newTime, oreBot, clayBot, obBot, geodeBot+1,
          newOreAmt-bp.geode.ore, newClayAmt, newObAmt-bp.geode.ob, newGeodeAmt));
    }
    if (mayNeedMoreBotsOfType(BotType.OB, bp, oreBot, clayBot, obBot) &&
        clayAmt >= bp.ob.clay &&
        oreAmt >= bp.ob.ore) {
      max = Math.max(max, f(bp, newTime, oreBot, clayBot, obBot+1, geodeBot,
          newOreAmt-bp.ob.ore, newClayAmt-bp.ob.clay, newObAmt, newGeodeAmt));
    }
    if (mayNeedMoreBotsOfType(BotType.CLAY, bp, oreBot, clayBot, obBot) &&
        oreAmt >= bp.clay.ore) {
      max = Math.max(max, f(bp, newTime, oreBot, clayBot+1, obBot, geodeBot,
          newOreAmt-bp.clay.ore, newClayAmt, newObAmt, newGeodeAmt));
    }
    if (mayNeedMoreBotsOfType(BotType.ORE, bp, oreBot, clayBot, obBot) &&
        oreAmt >= bp.ore.ore) {
      max = Math.max(max, f(bp, newTime, oreBot+1, clayBot, obBot, geodeBot,
          newOreAmt-bp.ore.ore, newClayAmt, newObAmt, newGeodeAmt));
    }
    return max;
  }

  /**
   * A very rough upper bound for needed number of bots that collect a specific resource for a given blueprint.
   *
   * Specifically, if the number of bots that collect a given type or resource is greater than or equal to the largest
   * amount of that resource any bot costs, we do not need more of that resource collector bot because the bot factory
   * simply will not keep up.
   */
  static boolean mayNeedMoreBotsOfType(BotType type, Blueprint bp, int oreBot, int clayBot, int obBot) {
    switch (type) {
      case ORE:
        return Math.max(Math.max(bp.ore.ore, bp.clay.ore), Math.max(bp.ob.ore, bp.geode.ore)) > oreBot;
      case CLAY:
        return Math.max(Math.max(bp.ore.clay, bp.clay.clay), Math.max(bp.ob.clay, bp.geode.clay)) > clayBot;
      case OB:
        return Math.max(Math.max(bp.ore.ob, bp.clay.ob), Math.max(bp.ob.ob, bp.geode.ob)) > obBot;
      case GEODE:
      default:
        return true;
    }
  }

  static class Blueprint {
    int id;
    BotCost ore;
    BotCost clay;
    BotCost ob;
    BotCost geode;

    public Blueprint(int id, BotCost ore, BotCost clay, BotCost ob, BotCost geode) {
      this.id = id;
      this.ore = ore;
      this.clay = clay;
      this.ob = ob;
      this.geode = geode;
    }

    @Override
    public String toString() {
      return "Blueprint{" +
          "id=" + id +
          ", ore=" + ore +
          ", clay=" + clay +
          ", obsidian=" + ob +
          ", geode=" + geode +
          '}';
    }
  }

  static class BotCost {
    int ore;
    int clay;
    int ob;

    public BotCost(int ore, int clay, int ob) {
      this.ore = ore;
      this.clay = clay;
      this.ob = ob;
    }

    @Override
    public String toString() {
      return "BotCost{" +
          "ore=" + ore +
          ", clay=" + clay +
          ", obsidian=" + ob +
          '}';
    }
  }

  enum BotType {
    ORE, CLAY, OB, GEODE
  }
}
