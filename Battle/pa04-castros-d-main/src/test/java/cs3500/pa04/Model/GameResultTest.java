package cs3500.pa04.Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameResultTest {

  GameResult wins1;
  GameResult wins2;
  GameResult lose;
  GameResult draw;

  @BeforeEach
  public void setup() {
    wins1 = GameResult.WIN;
    wins2 = GameResult.WIN;
    lose = GameResult.LOSE;
    draw = GameResult.DRAW;

  }

  /**
   * Making sure two different objects with the same values are marked as equal
   */
  @Test
  public void testEquals() {
    assertEquals(wins1, wins2);
    assertNotEquals(wins1, lose);
    assertNotEquals(lose, draw);

  }

}