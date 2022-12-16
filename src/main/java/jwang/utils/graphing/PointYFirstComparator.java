package jwang.utils.graphing;

import java.util.Comparator;

public class PointYFirstComparator implements Comparator<Point> {
  @Override
  public int compare(Point a, Point b) {
    return a.y - b.y == 0 ? a.x - b.x : a.y - b.y;
  }
}
