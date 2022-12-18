package jwang.y2022;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class Day17Test {

  @BeforeEach
  void setup(TestInfo testInfo) {
    System.out.printf("name: %s, method: %s", testInfo.getDisplayName(), testInfo.getDisplayName());
  }

  @Disabled
  @ParameterizedTest
  @EnumSource(Day17.Rock.class)
  void testRightThenAllLeft(Day17.Rock rock) {
    Day17.RockStack room = new Day17.RockStack(100, 7);
    room.stack.set(5, new boolean[]{true, false, false, false, false, false, false});
    room.stack.set(4, new boolean[]{true, false, false, false, false, false, false});
    room.stack.set(3, new boolean[]{true, false, false, false, false, false, false});
    room.stack.set(2, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(1, new boolean[]{true, false, false, false, false, false, false});
    room.stack.set(0, new boolean[]{true, false, false, false, false, false, false});
    room.top = 6;

    int[] dirs = ">><<<<<<<<<<".chars().map(c -> c - 61).toArray();
    int dirCount = 0;
    Day17.dropRock(true, true, dirs, room, rock, dirCount);
  }

  @Disabled
  @ParameterizedTest
  @EnumSource(Day17.Rock.class)
  void testLeftThenAllRight(Day17.Rock rock) {
    Day17.RockStack room = new Day17.RockStack(100, 7);
    room.stack.set(5, new boolean[]{false, false, false, false, false, false, true});
    room.stack.set(4, new boolean[]{false, false, false, false, false, false, true});
    room.stack.set(3, new boolean[]{false, false, false, false, false, false, true});
    room.stack.set(2, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(1, new boolean[]{false, false, false, false, false, false, true});
    room.stack.set(0, new boolean[]{false, false, false, false, false, false, true});
    room.top = 6;

    int[] dirs = "<<>>>>>>>>>>".chars().map(c -> c - 61).toArray();
    int dirCount = 0;
    Day17.dropRock(true, true, dirs, room, rock, dirCount);
  }

  @Test
  void testLGoesRightIntoPlus() {
    Day17.RockStack room = new Day17.RockStack(100, 7);
    room.stack.set(5, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(4, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(3, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(2, new boolean[]{false, false, false, false, false, true, false});
    room.stack.set(1, new boolean[]{false, false, false, false, true, true, true});
    room.stack.set(0, new boolean[]{false, false, false, false, false, true, true});
    room.top = 3;

    Day17.printStack(room.stack, room.top, 7, null, 0, 0);

    int[] dirs = "<<<<<>>>>>".chars().map(c -> c - 61).toArray();
    int dirCount = 0;
    Day17.dropRock(true, true, dirs, room, Day17.Rock.L, dirCount);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(room.stack.get(2))
        .as("Row 2")
        .isEqualTo(new boolean[]{false, false, false, true, false, true, false});
    softly.assertThat(room.stack.get(1))
        .as("Row 1")
        .isEqualTo(new boolean[]{false, false, false, true, true, true, true});
    softly.assertThat(room.stack.get(0))
        .as("Row 0")
        .isEqualTo(new boolean[]{false, true, true, true, false, true, true});
    softly.assertAll();
  }

  @Test
  void testPlusGoesLeftIntoMinus_BottomRow() {
    Day17.RockStack room = new Day17.RockStack(100, 7);
    room.stack.set(5, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(4, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(3, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(2, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(1, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(0, new boolean[]{true, true, true, true, false, false, false});
    room.top = 3;

    Day17.printStack(room.stack, room.top, 7, null, 0, 0);

    int[] dirs = "<<<>>><".chars().map(c -> c - 61).toArray();
    int dirCount = 0;
    Day17.dropRock(true, true, dirs, room, Day17.Rock.P, dirCount);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(room.stack.get(2))
        .as("Row 2")
        .isEqualTo(new boolean[]{false, false, false, false, true, false, false});
    softly.assertThat(room.stack.get(1))
        .as("Row 1")
        .isEqualTo(new boolean[]{false, false, false, true, true, true, false});
    softly.assertThat(room.stack.get(0))
        .as("Row 0")
        .isEqualTo(new boolean[]{true, true, true, true, true, false, false});
    softly.assertAll();
  }

  @Test
  void testPlusGoesRightIntoMinus_BottomRow() {
    Day17.RockStack room = new Day17.RockStack(100, 7);
    room.stack.set(5, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(4, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(3, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(2, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(1, new boolean[]{false, false, false, false, false, false, false});
    room.stack.set(0, new boolean[]{false, false, false, true, true, true, true});
    room.top = 3;

    Day17.printStack(room.stack, room.top, 7, null, 0, 0);

    int[] dirs = ">>><<<>".chars().map(c -> c - 61).toArray();
    int dirCount = 0;
    Day17.dropRock(true, true, dirs, room, Day17.Rock.P, dirCount);

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(room.stack.get(2))
        .as("Row 2")
        .isEqualTo(new boolean[]{false, false, true, false, false, false, false});
    softly.assertThat(room.stack.get(1))
        .as("Row 1")
        .isEqualTo(new boolean[]{false, true, true, true, false, false, false});
    softly.assertThat(room.stack.get(0))
        .as("Row 0")
        .isEqualTo(new boolean[]{false, false, true, true, true, true, true});
    softly.assertAll();
  }
}
