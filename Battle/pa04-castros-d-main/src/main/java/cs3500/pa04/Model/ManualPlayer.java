package cs3500.pa04.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManualPlayer implements Player {
  private String name;
  public List<Coord> badShots;
  public List<Coord> goodShots;
  private List<ShipType> playerFleet;
  public List<Ship> survivingFleet;
  private Map<ShipType, Integer> fleet;
  public BattleSalvoModel gameForGrid;
  public List<Coord> allCoordinates;
  public List<Coord> allPlayerShots;

  public int enemySurvivingFleetCount;
  //added to try to help implementation of the take shots method, possibly set them when the user takes a shot through
  // view

  // for initializing in controller
  public ManualPlayer(String name, Map<ShipType, Integer> fleet, BattleSalvoModel gameForGrid,
                      List<Coord> badShots,
                      List<Coord> goodShots, List<Coord> allPlayerShots) {
    this.name = name;
    this.fleet = fleet;
    this.gameForGrid = gameForGrid;
    this.badShots = badShots;
    this.goodShots = goodShots;
    this.allPlayerShots = allPlayerShots;
    this.enemySurvivingFleetCount = 4; //random number
    this.allCoordinates = new ArrayList<Coord>();
  }


  @Override
  public String name() {
    return name;
  }

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
      gameForGrid.getMpGrid().addCarrier(oneShipLocations, currentShip);
      gameForGrid.getMpGrid().addShip(oneShipLocations, currentShip);
    }
    //adding them to this player's surviving fleet
    addingFleetHelper(listWithCoords);

    //maybe remove this: but adding them to the player's grid surviving fleet
    gameForGrid.getMpGrid().setFleetsInGrid(listWithCoords);

    //added this to help out the take shots
    gameForGrid.getMpGrid().setAllOccupiedCoordinates(allCoords);
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

  private void setAllCoordinates(ArrayList<Coord> allCoords) {
    allCoordinates = allCoords;
  }

  @Override
  public List<Coord> takeShots() {
    //getting the list of shots from the model
    ArrayList<Coord> userShots = gameForGrid.getCurrentPlayerShots();
    ArrayList<Coord> allCoordinates = gameForGrid.getP2Grid().getAllOccupiedCoordinates();
    BattleGrid aIPlayerGrid = gameForGrid.getP2Grid();
    List<Ship> survivingShips = gameForGrid.getP2Player().survivingFleet;

    for (int i = 0; i < userShots.size(); i++) {
      Coord currentCoord = userShots.get(i);
      //IF it hits one in the player's class
      if (allCoordinates.contains(currentCoord)) {
        aIPlayerGrid.recordSuccessfulShot(currentCoord);
        //add it to the list of good counts
        goodShots.add(currentCoord);
        //now find which ship it was and update its fields
        Ship shipShot = whatShipWasHit(currentCoord, survivingShips);
        shipShot.addADestroyedLocation();
      }
      if (!allCoordinates.contains(currentCoord)) {
        aIPlayerGrid.recordMissedShot(currentCoord);
        badShots.add(currentCoord);
      }
      //add the shot into a list of all the shots taken
      allPlayerShots.add(currentCoord);
    }
    //removing it from the other player's grid surviving fleet
    //get the destroyed fleet:
    List<Ship> destroyedShips = findDestroyedShips(survivingShips);
    removeDestroyedFleet(destroyedShips, survivingShips);

    return userShots;
  }


  private Ship whatShipWasHit(Coord userShot, List<Ship> allShips) {
    //if the coordinates equal any of the coordintes from the allShips
    //lis then return that ship!
    Ship shipShot = null;
    for (int i = 0; i < allShips.size(); i++) {
      //getting thr ship at that index
      Ship currentShip = allShips.get(i);
      List<Coord> shipCoords = currentShip.getOccupiedLocations();
      //check if the coordinate hit was in that list, if so return that ship
      if (shipCoords.contains(userShot)) {
        shipShot = currentShip;
      }
    }
    return shipShot;
  }

  //UPDATE THIS ONE TO GET THE P2 SURVIVING FLEET INSTEAD OF THIS ONE
  private void removeDestroyedFleet(List<Ship> destroyedShips, List<Ship> survivingFleet) {
    for(Ship ship: destroyedShips) {
      survivingFleet.remove(ship);
    }
  }

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




  //well just pass in the current shots from the game model
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    //just return the enemy successful hits
    List<Coord> enemySuccessfulHits = gameForGrid.getP2Player().goodShots;
    return enemySuccessfulHits;

  }

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    System.out.println("Your shots that successfully hit the other Player :D : ");
    for (Coord c : goodShots) {
      c.printCoords();
    }
  }

  @Override
  public void endGame(GameResult result, String reason) {
    System.out.println("The game ended in a : " + result.toString() + "" + reason);
  }
}



