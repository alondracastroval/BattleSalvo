package cs3500.pa04.Model;


import java.util.ArrayList;
import java.util.Objects;

public class Coord {
  private int x;
  private int y;

  private boolean filled;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  //rando getters
  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }


  //Overriding equals here
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coord coord = (Coord) o;
    return x == coord.x && y == coord.y;
  }

  public int hashCode() {
    return Objects.hash(x, y);
  }



  /**
   * Prints coordinates
   */
  public void printCoords() {
    System.out.print("[" + x + "," + y + "]");
  }
}


