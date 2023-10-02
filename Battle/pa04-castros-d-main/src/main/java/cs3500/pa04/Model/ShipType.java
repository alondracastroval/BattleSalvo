package cs3500.pa04.Model;

/**
 * Represents the four available types of ship in Battle Salvo
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  final int size;

  ShipType(int size) {
    this.size = size;
  }

}
