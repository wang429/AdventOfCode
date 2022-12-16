package jwang.utils.graphing;

import java.util.Comparator;

public class PointXFirstComparator implements Comparator<Point> {
  @Override
  public int compare(Point a, Point b) {
    return a.x - b.x == 0 ? a.y - b.y : a.x - b.x;
  }
}
