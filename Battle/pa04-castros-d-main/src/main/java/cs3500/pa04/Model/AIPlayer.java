package cs3500.pa04.Model;

import cs3500.pa04.View.BattleView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents the automated player of a Battle Salvo game
 */
public class AIPlayer implements Player {
  private String name;
  public List<Coord> badShots;
  public List<Coord> goodShots;
  private List<ShipType> playerFleet;
  public List<Ship> survivingFleet;
  private Map<ShipType, Integer> fleet;
  private BattleSalvoModel gameForGrid;
  public List<Coord> allCoordinates;
  public List<Coord> allPlayerShots;

  public int enemySurvivingFleetCount;

  public AIPlayer(String name, Map<ShipType, Integer> fleet, BattleSalvoModel gameForGrid,
                  List<Coord> badShots, List<Coord> goodShots, List<Coord> allPlayerShots) {
    this.name = name;
    this.fleet = fleet;
    this.gameForGrid = gameForGrid;
    this.badShots = badShots;
    this.goodShots = goodShots;
    this.allPlayerShots = new ArrayList<Coord>();
    this.allCoordinates = new ArrayList<Coord>();

  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return null;
  }


  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    //empty listS to start off
    List<Ship> listWithCoords = new ArrayList<Ship>();
    ArrayList<Coord> allCoords = new ArrayList<Coord>();
    ArrayList<Coord> oneShipLocations = new ArrayList<Coord>();

    //converting the hashmap into the list of ships with no coords, yet
    hashMapToShipList(specifications, listWithCoords);

    //FOR EVERY ONE OF THOSE SHIPS //GENERATE A LOCATION AND THEN ADD IT TO THE SHIP FIELD
    for (int i = 0; i < listWithCoords.size(); i++) {
      Ship currentShip = listWithCoords.get(i);
      oneShipLocations = currentShip.locationGeneration(currentShip, height, width);
      //checking for repeats
      while (i > 0 && currentShip.isThereRepeats(allCoords, oneShipLocations)) {
        oneShipLocations = currentShip.locationGeneration(currentShip, height, width);
        currentShip.setOccupiedLocations(oneShipLocations);
      }
      //add the coords into the all coords list once they don't repeat
      allCoords.addAll(oneShipLocations);
      allCoordinates.addAll(oneShipLocations);
      //add that list to the ships field
      currentShip.setOccupiedLocations(oneShipLocations);
      //now we can add them to the grid, first carriers then rest
      gameForGrid.getP2Grid().addCarrier(oneShipLocations, currentShip);
      gameForGrid.getP2Grid().addShip(oneShipLocations, currentShip);

    }
    addingFleetHelper(listWithCoords);
    //maybe remove this: but adding them to the player's grid surviving fleet
    gameForGrid.getP2Grid().setFleetsInGrid(listWithCoords);

    //added this to help out the take shots
    gameForGrid.getP2Grid().setAllOccupiedCoordinates(allCoords);
    return listWithCoords;
  }


  private void hashMapToShipList(Map<ShipType, Integer> specifications, List<Ship>
      listWithCoords) {
    for (Map.Entry<ShipType, Integer> val : specifications.entrySet()) {
      ShipType shipType = val.getKey();
      Integer instances = val.getValue();
      //makes a ship based on the type
      ArrayList<Ship> shipMadeList = Ship.makeAShip(shipType, instances);
      listWithCoords.addAll(shipMadeList);
    }
  }

  private void addingFleetHelper(List<Ship> survivingShips) {
    this.survivingFleet = survivingShips;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    //total list
    //temporary list

    List<Coord> tempShots = new ArrayList<>();
    List<Coord> allShots = new ArrayList<>();
    BattleGrid AIgrid = gameForGrid.getP2Grid();
    BattleGrid manualGrid = gameForGrid.getMpGrid();
    List<Coord> shipCoords = gameForGrid.getMpGrid().getAllOccupiedCoordinates();
    List<Ship> survivingFleet = gameForGrid.getP1Player().survivingFleet;

    //get the model
    //make sure you add it to allShots

    tempShots = AIgrid.generateRandomShots(AIgrid.height,
        AIgrid.width, survivingFleet.size());

    //true means its its not valid, if shotBefore doesn't work then try witht he allShots
    while (AIgrid.validShots(tempShots, allShots) || (shotBefore(tempShots)) ||
        (validNumberofShots(survivingFleet, gameForGrid.p1.survivingFleet))) {
      tempShots = AIgrid.generateRandomShots(AIgrid.height,
          AIgrid.width, survivingFleet.size());
    }

    allShots.addAll(tempShots);
    //maybe delete this if it doesn't work

    for (int i = 0; i < tempShots.size(); i++) {
      Coord currentCoord = tempShots.get(i);
      if (shipCoords.contains(currentCoord)) {
        manualGrid.recordSuccessfulShot(currentCoord);
        goodShots.add(currentCoord);

        Ship shipShot = whatShipWasHit(currentCoord, survivingFleet);
        shipShot.addADestroyedLocation();
        System.out.println(shipShot.getType().toString() + shipShot.destroyedLocationsCount);
      }

      if (!shipCoords.contains(currentCoord)) {
        manualGrid.recordMissedShot(currentCoord);
        badShots.add(currentCoord);
      }

    }

    List<Ship> destroyedShips = findDestroyedShips(survivingFleet);
    removeDestroyedFleet(destroyedShips, survivingFleet);
    allPlayerShots.addAll(tempShots);

    return tempShots;
  }

  /**
   * Checks if a shot has been shot before
   * @param currentShots the current shots
   * @return whether it has been shot or not before
   */
  private boolean shotBefore(List<Coord> currentShots) {
    ArrayList<String> no = new ArrayList<String>();
    for (Coord c : currentShots) {
      if (allPlayerShots.contains(c)) {
        //add a yes to the list
        no.add("yes");
      }
    }
    //if the no list contains at least 1 yes then, it is false
    return no.contains("yes");
  }


  /**
   * Checks if the number of shots is valid
   * @param survivingFleet the surviving fleet of this ship
   * @param opponentFleet the opponents fleet
   * @return whether the number of shots is valid
   */
  private boolean validNumberofShots(List<Ship> survivingFleet, List<Ship> opponentFleet) {
    //if the size of this fleet is larger than the number of coords left the person has
    int shotsLeft = this.survivingFleet.size();
    int enemyCoordsLeft = BattleGrid.howManyCoordsLeft(gameForGrid.getMpGrid().getGrid());

    return (shotsLeft > enemyCoordsLeft);
  }


  /**
   * Removes a destroyed ship from the fleet
   * @param destroyedShips the ships that have been destroyed
   * @param survivingFleet the surviving fleet
   */
  private void removeDestroyedFleet(List<Ship> destroyedShips, List<Ship> survivingFleet) {
    for(Ship ship: destroyedShips) {
      survivingFleet.remove(ship);
    }
  }

  /**
   * Finds which ship has been destroyed
   *
   * @param enemySurvivingFleet the enemy surviving fleet
   * @return the destroyed ship object
   */
  private List<Ship> findDestroyedShips(List<Ship> enemySurvivingFleet) {
    //storing the list of destroyed ships
    List<Ship> destroyedShips = new ArrayList<Ship>();
    for (Ship ship: enemySurvivingFleet) {
      if (ship.isShipDestroyed()) {
        destroyedShips.add(ship);
      }
    }
    return destroyedShips;

  }


  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   * ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    //report which shots hit a ship on this player's board
    //just return the enemy successful hits
    List<Coord> damagedList = new ArrayList<Coord>();
    for(Coord c: opponentShotsOnBoard) {
      if (this.allCoordinates.contains(c)) {
        damagedList.add(c);
      }
    }
    System.out.println(damagedList.toString());
    return damagedList;

  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    System.out.println("Your shots that successfully hit the other Player :D : ");
    for (Coord c : goodShots) {
      c.printCoords();
    }

  }


  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    System.out.println("The game ended in a : " + result.toString() + "" + reason);

  }


  /**
   * Returns whatShipWasHit
   * @param userShot the shot of the user
   * @param allShips all the ships of the user
   * @return
   */
  private Ship whatShipWasHit(Coord userShot, List<Ship> allShips) {
    //if the coordinates equal any of the coordintes from the allShips
    //lis then return that ship!
    Ship shipShot = null;
    for(int i = 0; i < allShips.size(); i++) {
      //getting thr ship at that index
      Ship currentShip = allShips.get(i);
      List<Coord> shipCoords = currentShip.getOccupiedLocations();
      //check if the coordinate hit was in that list, if so return that ship
      if(shipCoords.contains(userShot)) {
        shipShot = currentShip;
      }
    }
    return shipShot;
  }




}
