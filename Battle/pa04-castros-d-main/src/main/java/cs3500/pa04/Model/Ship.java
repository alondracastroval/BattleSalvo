package cs3500.pa04.Model;


import cs3500.pa04.Json.CoordRecord;
import cs3500.pa04.Json.Direction;
import cs3500.pa04.Json.ShipAdapter;
import cs3500.pa04.Json.UnserializedShipAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a Ship in a game of BattleSalvo
 */
public class Ship {

  public ShipType type;
  public List<Coord> occupiedLocations;
  public int destroyedLocationsCount;
  public boolean destroyed;
  public int size;
  public Direction direction;


  public Ship(ShipType type, int size, Boolean destroyed) {
    this.type = type;
    this.size = size;

  }

  public Ship(ShipType type, List<Coord> occupiedLocations, List<Coord> destroyedLocations,
              Boolean destroyed, int size) {
    this.type = type;
    this.occupiedLocations = occupiedLocations;
    this.destroyedLocationsCount = 0;
    this.destroyed = destroyed;
    this.size = size;
  }


  /**
   * Creates a list of ships based on ship type and instances
   * @param type the ship type of the ship
   * @param instances the amount of that type of ship the user wants
   * @return the list of all the ships created
   */
  public static ArrayList<Ship> makeAShip(ShipType type, Integer instances) {
    ArrayList<Ship> shipList = new ArrayList<Ship>();

    if (type.equals(ShipType.CARRIER)) {
      for (int i = 0; i < instances; i++) {
        Ship shipMade = new Ship(ShipType.CARRIER, 6, false);
        shipList.add(shipMade);
      }
    } else if (type.equals(ShipType.BATTLESHIP)) {
      for (int i = 0; i < instances; i++) {
        Ship shipMade = new Ship(ShipType.BATTLESHIP, 5, false);
        shipList.add(shipMade);
      }
    } else if (type.equals(ShipType.DESTROYER)) {
      for (int i = 0; i < instances; i++) {
        Ship shipMade = new Ship(ShipType.DESTROYER, 4, false);
        shipList.add(shipMade);
      }
    } else
      for (int i = 0; i < instances; i++) {
        Ship shipMade = new Ship(ShipType.SUBMARINE, 3, false);
        shipList.add(shipMade);
      }

    return shipList;
  }

  /**
   * Gets the type of a ship
   * @return the type of a ship
   */
  public ShipType getType() {
    return type;
  }

  /**
   * Generates random locations for a ship
   * @param ship the ship that is getting random coordinates
   * @param boardHeight the height of the board
   * @param boardWidth the width of the board
   * @return a list of random coordinates the ship occupies
   */
  public ArrayList<Coord> locationGeneration(Ship ship, int boardHeight, int boardWidth) {
    ArrayList<Coord> endList = new ArrayList<Coord>();
    Random random = new Random();
    //generate the beggining completely randomly
    int randomYCoord = random.nextInt(boardHeight - (ship.size - 1));
    int randomXCoord = random.nextInt(boardWidth - (ship.size - 1));
    Coord actualEnd = null;

    Coord beginningCoord = new Coord(randomXCoord, randomYCoord);
    //adding it to the list
    endList.add(beginningCoord);

    ArrayList<Coord> potentialEnds = new ArrayList<Coord>();
    //now generate the end based on the begginning
    Coord verticalEnd = new Coord(randomXCoord + (ship.size - 1), randomYCoord);
    Coord horizontalEnd = new Coord(randomXCoord, randomYCoord + (ship.size - 1));
    potentialEnds.add(verticalEnd);
    potentialEnds.add(horizontalEnd);

    //randomly generating whether it is vertical or horizontal
    for (int a = 0; a < potentialEnds.size() - 1; a++) {
      int randomNum = random.nextInt(2);
      if (randomNum == 0) {
        actualEnd = verticalEnd;
        ship.direction = Direction.HORIZONTAL;
        endList.add(actualEnd);
      }
      if (randomNum == 1) {
        actualEnd = horizontalEnd;
        ship.direction = Direction.VERTICAL;
        endList.add(actualEnd);
      }
    }
    //generating the middle coordinates using a helper method
    generateMiddle(beginningCoord, verticalEnd, horizontalEnd, endList, actualEnd);
    return endList;
  }


  /**
   * Helper for location generation method, generates the intermediate coordinates
   * @param beginningCoord the first coord of a ship
   * @param verticalEnd the last coordinate if the ship is vertical
   * @param horizontalEnd the last coordinate if the ship is horizontal
   * @param endList the list of coordinates generated in the end
   * @param actualEnd the vertical/horizontal end picked randomly
   */
  public void generateMiddle(Coord beginningCoord, Coord verticalEnd, Coord horizontalEnd,
                             ArrayList<Coord> endList, Coord actualEnd) {
    // now we generate the middle spots
    if (actualEnd.equals(verticalEnd)) {
      for (int i = 0; i < size - 2; i++) {
        //leepting the y the same
        Coord midCord = new Coord(beginningCoord.getX() + 1 + i, beginningCoord.getY());
        endList.add(midCord);

      }
    }
    if (actualEnd.equals(horizontalEnd)) {
      for (int i = 0; i < size - 2; i++) {
        //leepting the y the same
        Coord midCord = new Coord(beginningCoord.getX(), beginningCoord.getY() + 1 + i);
        endList.add(midCord);
      }
    }
  }

  /**
   * Determines if there is two coordinates repeated
   * @param allCoords all the coordinates in the game
   * @param currentCoords the current coordinates of a ship
   * @return whether there is repeated coords
   */
  public boolean isThereRepeats(ArrayList<Coord> allCoords, ArrayList<Coord> currentCoords) {

    //if any of the coordinates in the current coords list is inside
    // all coords return true!
    boolean answer = false;
    for (Coord currentCoordinate : currentCoords) {
      if (allCoords.contains(currentCoordinate)) {
        return true;
      }
    }
    return false;
  }







  /**
   * Adds a destroyed spot to the count of destroyed locations
   */
  public void addADestroyedLocation() {
    destroyedLocationsCount += 1;
  }

  /**
   * Changes the state of a ship to destroyed
   */
  public void shipDestroyed() {
    destroyed = true;
  }


  /**
   * Determines if a ship is destroyed
   * @return whether a ship is destroyed
   */
  public boolean isShipDestroyed() {
    if (destroyedLocationsCount == size) {
      return true;
    } else return false;
  }

  /**
   * Sets the list of coordinates (occupied location) to a value
   * @param occupLocs the list of coordinates a ship takes up
   */
  public void setOccupiedLocations(ArrayList<Coord> occupLocs) {
    this.occupiedLocations = occupLocs;
  }


  public List<Coord> getOccupiedLocations() {
    return occupiedLocations;
  }



  /**
   * Converts a list of regular ships into a list of ship adapters
   * @param listOfShips the list of ships to be parsed
   * @return the converted list of ShipAdapters
   */
  public static List<UnserializedShipAdapter> convertShipToAdapter(List<Ship> listOfShips) {
    List<UnserializedShipAdapter> convertedList = new ArrayList<UnserializedShipAdapter>();
    //for every element of the list convert that ship into a ship adapter
    for(int i = 0; i < listOfShips.size(); i++) {
      Ship currentShip = listOfShips.get(i);
      //get the first coordinate
      //then the size
      //and then direction(you'll need to update the setupMethod to include a direction
      Coord startingCoord = currentShip.getOccupiedLocations().get(0);
      CoordRecord coordRecord = new CoordRecord(startingCoord.getX(), startingCoord.getY());
      int currentShipSize = currentShip.size;
      Direction currentDirection = currentShip.direction;
      //now we have the information to make the new ship adapters
     UnserializedShipAdapter createdAdapter = new UnserializedShipAdapter(coordRecord, currentShipSize, currentDirection);
      convertedList.add(createdAdapter);
    }
    return convertedList;

  }

  //MIGHT DELETE THIS IF IT CAUSES TROUBLE
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ship ship = (Ship) o;
    return type == ship.type && size == ship.size;
  }


}






