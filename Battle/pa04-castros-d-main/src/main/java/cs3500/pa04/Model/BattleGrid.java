package cs3500.pa04.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a board/grid in a game of BattleSalvo
 */
public class BattleGrid {
  int height;
  int width;
  private String[][] grid;
  private ArrayList<Coord> allOccupiedCoordinates;
  public List<Ship> fleetsInGrid;


  //constructor with just the height and the width
  public BattleGrid(int height, int width) {
    this.height = height;
    this.width = width;

  }

  /**
   * Makes a grid based on the user provided height and width
   */
  public void makeUserGrid() {
    grid = new String[height][width];
    //loop to give every element of the grid a 0
    for (int i = 0; i < grid.length; i++) {
      for (int b = 0; b < grid[i].length; b++) {
        grid[i][b] = "_";
      }
    }

  }

  public void setFleetsInGrid(List<Ship> survivingFleet) {
    this.fleetsInGrid = survivingFleet;
  }

  public void removeAShip(List<Ship> enemySurvivingFleet) {
    for (Ship ship : enemySurvivingFleet) {
      if (ship.isShipDestroyed()) {
        fleetsInGrid.remove(ship);
      }
    }
  }

  public List<Ship> getFleetsInGrid() {
    return fleetsInGrid;
  }



  /**
   * Gets the grid 2d Aray from the BattleGrid instance
   *
   * @return the ships grid
   */
  public String[][] getGrid() {
    return this.grid;
  }


  /**
   * Adds a ship to the board based on what type of ship it is
   *
   * @param coords the coordinates of each ship
   * @param ship   the ship being added to the board
   */
  public void addShip(ArrayList<Coord> coords, Ship ship) {
    ShipType sType = ship.getType();
    if (sType.equals(ShipType.CARRIER)) {
      addCarrier(coords, ship);
    }
    if (sType.equals(ShipType.SUBMARINE)) {
      addSubmarine(coords);
    }
    if (sType.equals(ShipType.DESTROYER)) {
      addDestroyer(coords);
    }
    if (sType.equals(ShipType.BATTLESHIP)) {
      addBattleShip(coords);
    }


  }

  /**
   * Adds carrier ships to the board
   *
   * @param coords the coordinates of the ship
   * @param ship   the ship being added to the board
   */
  public void addCarrier(ArrayList<Coord> coords, Ship ship) {
    if (ship.getType().equals(ShipType.CARRIER)) {
      for (int i = 0; i < coords.size(); i++) {
        Coord currentCoord = coords.get(i);
        int x = currentCoord.getX();
        int y = currentCoord.getY();
        grid[y][x] = "C";
      }
    }
  }

  /**
   * Adds a ship of type BattleShip to the board
   *
   * @param coords the coordinates of the ship
   */
  public void addBattleShip(ArrayList<Coord> coords) {
    for (int i = 0; i < coords.size(); i++) {
      Coord currentCoord = coords.get(i);
      int x = currentCoord.getX();
      int y = currentCoord.getY();
      grid[y][x] = "B";
    }
  }

  /**
   * Adds a ship of type destroyer to the board
   *
   * @param coords the coordinates of the ship
   */
  public void addDestroyer(ArrayList<Coord> coords) {
    for (int i = 0; i < coords.size(); i++) {
      Coord currentCoord = coords.get(i);
      int x = currentCoord.getX();
      int y = currentCoord.getY();
      grid[y][x] = "D";
    }
  }

  /**
   * Adds a ship of type submarine to the board
   *
   * @param coords the coordinates where the ship is located
   */
  public void addSubmarine(ArrayList<Coord> coords) {
    for (int i = 0; i < coords.size(); i++) {
      Coord currentCoord = coords.get(i);
      int x = currentCoord.getX();
      int y = currentCoord.getY();
      grid[y][x] = "S";
    }
  }

  /**
   * Sets the list of allOccupiedCoordinates to a desired value
   *
   * @param allCoords the value that will be set to the list
   */
  public void setAllOccupiedCoordinates(ArrayList<Coord> allCoords) {
    this.allOccupiedCoordinates = allCoords;
  }

  public ArrayList<Coord> getAllOccupiedCoordinates() {
    return allOccupiedCoordinates;
  }


  /**
   * Adds a successful shot marker to the grid
   *
   * @param shotLocation the location of the shot
   */
  public void recordSuccessfulShot(Coord shotLocation) {
    int x = shotLocation.getX();
    int y = shotLocation.getY();
    if ((x <= width) && (y <= height)) {
      grid[y][x] = "X";
    }
  }

  public List<Coord> generateRandomShots(int height, int width, int aliveShips) {
    ArrayList<Coord> possibleCords = new ArrayList<>();
    Random random = new Random();
    for(int i = 0; i < aliveShips ; i++) {

      int randomXCoord = random.nextInt(width - 1);
      int randomYCoord = random.nextInt(height - 1);

      Coord oneShot = new Coord(randomXCoord, randomYCoord);
      possibleCords.add(oneShot);
    }
    return possibleCords;
  }

  public Boolean validShots(List<Coord> possibleShots, List<Coord> allShots) {
    ArrayList<String> checking = new ArrayList<>();
    for(int i = 0; i < possibleShots.size() ; i++) {
      Coord coordAtIndex = possibleShots.get(i);
      if(allShots.contains(coordAtIndex)) {
        checking.add("COPY");
      } else {
        checking.add("NAH");
      }
    }
    return ((checking.contains("COPY")));

  }



  /**
   * Adds a missed shot to the grid
   * @param shotLocation the location of the missed
   */
  public void recordMissedShot(Coord shotLocation) {
    int x = shotLocation.getX();
    int y = shotLocation.getY();
    if ((x <= width) && (y <= height)) {
      grid[y][x] = "M";
    }
  }

  public static int howManyCoordsLeft(String[][] playerGrid) {

    int count = 0;

    for (int i = 0; i < playerGrid.length; i++) {
      for (int b = 0; b < playerGrid[i].length; b++) {
        if (playerGrid[i][b].equals("_")) {
        count += 1;
      }
    }
  }
    return count;
  }






}
