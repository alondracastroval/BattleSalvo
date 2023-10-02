package cs3500.pa04.Model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordTest {

  Coord coord1;
  Coord coord2;
  Coord coord3;
  Coord coord4;
  Coord coord5;

  /**
   * Setting initial values for testing examples
   */
  @BeforeEach
  public void setUp() {

    coord1 = new Coord(1, 1);
    coord2 = new Coord(2, 4);
    coord3 = new Coord(0, 0);
    coord4 = new Coord(5, 5);
    coord5 = new Coord(1, 1);
  }

  /**
   * Making sure the getX() method works by getting correct x value;
   */
  @Test
  public void testGetX() {
    assertEquals(1, coord1.getX());
    assertEquals(2, coord2.getX());
    assertEquals(0, coord3.getX());
    assertEquals(5, coord4.getX());
  }

  /**
   * Making sure the getY() method works by getting correct y value
   */
  @Test
  public void testGetY() {
    assertEquals(1, coord1.getY());
    assertEquals(4, coord2.getY());
    assertEquals(0, coord3.getY());
    assertEquals(5, coord4.getY());
  }

  /**
   * Making sure the equals method was overriding correctly and two objects are compared correctly
   */
  @Test
  public void testEquals() {
    assertNotEquals(coord1, coord2);
    assertNotEquals(coord2, coord3);
    assertEquals(coord1, coord5);

  }

  /**
   * Making sure the hashcode method works correctly
   */
  @Test
  public void testHashCode() {
    assertEquals(Objects.hash(1, 1),  coord1.hashCode());
    assertEquals(Objects.hash(2, 4),  coord2.hashCode());
    assertEquals(Objects.hash(0, 0),  coord3.hashCode());

  }


}