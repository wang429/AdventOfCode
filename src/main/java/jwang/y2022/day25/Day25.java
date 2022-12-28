package jwang.y2022.day25;

import jwang.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class Day25 {

  static List<String> test = Arrays.asList(
      "1=-0-2",
      "12111",
      "2=0=",
      "21",
      "2=01",
      "111",
      "20012",
      "112",
      "1=-1=",
      "1-12",
      "12",
      "1=",
      "122"
  );

  public static void main(String[] args) throws Exception {
    List<String> input = Utils.readFromFile("src/main/resources/jwang/y2022/Day25Input.txt");
//    input = test;

    long sum = 0;
    for (String line : input) {
      SnafuNumber snafu = SnafuNumber.parse(line);
      sum += snafu.value;
    }

    SnafuNumber snafuNumber = SnafuNumber.valueOf(sum);
    System.out.println(snafuNumber);
    System.out.println("sum: " + sum + ", base 5: " + Long.toString(snafuNumber.value, 5) + ", SNAFU: " + snafuNumber);
  }

  static class SnafuNumber extends Number {

    String str;
    long value = 0L;

    static SnafuNumber parse(String str) {
      SnafuNumber ret = new SnafuNumber();
      ret.str = str;
      char[] chars = str.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[chars.length - 1 - i];
        switch (c) {
          case '1':
          case '2':
          case '0':
            ret.value += (long) ((c - '0') * Math.pow(5, i));
            break;
          case '-':
            ret.value += (long) (-1 * Math.pow(5, i));
            break;
          case '=':
            ret.value += (long) (-2 * Math.pow(5, i));
            break;
          default:
            throw new IllegalStateException("Unexpected value: " + c);
        }
      }
      return ret;
    }

    static SnafuNumber valueOf(long l) {
      SnafuNumber ret = new SnafuNumber();
      ret.value = l;
      char[] base5 = Long.toString(ret.value, 5).toCharArray();
      boolean b = false;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < base5.length; i++) {
        char c = base5[base5.length - 1 - i];
        switch (c) {
          case '0':
          case '1':
            sb.append(b ? (char) (c+1) : c);
            b = false;
            break;
          case '2':
            sb.append(b ? '=' : c);
            break;
          case '3':
            sb.append(b ? '-' : '=');
            b = true;
            break;
          case '4':
            sb.append(b ? '0' : '-');
            b = true;
            break;
          default:
            throw new IllegalStateException("Unexpected value: " + c);
        }
      }
      if (b) {
        sb.append('1');
      }
      ret.str = sb.reverse().toString();
      return ret;
    }

    @Override
    public int intValue() {
      return Math.toIntExact(value);
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }

    @Override
    public String toString() {
      return str;
    }
  }
}
