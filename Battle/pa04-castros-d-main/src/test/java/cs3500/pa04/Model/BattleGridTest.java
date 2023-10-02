package cs3500.pa04.Model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BattleGridTest {
  ManualPlayer p1;
  AIPlayer p2;
  BattleGrid manualGrid;
  BattleGrid player2Grid;

  ArrayList<Coord> currentPlayerShots;

  BattleSalvoModel modelForTest;


  @BeforeEach
  public void setUp() {

    //setting the values of the grids for both players
    manualGrid = new BattleGrid(6, 6);
    player2Grid = new BattleGrid(8, 8);
    manualGrid.makeUserGrid();
    player2Grid.makeUserGrid();

    //now we set the values of the model for test
    modelForTest = new BattleSalvoModel(manualGrid, player2Grid);

    //making a fake fleet
    Map<ShipType, Integer> fakeFleet = new HashMap<ShipType, Integer>();
    //now we add the elements
    fakeFleet.put(ShipType.CARRIER, 1);
    fakeFleet.put(ShipType.BATTLESHIP, 1);
    fakeFleet.put(ShipType.DESTROYER, 1);
    fakeFleet.put(ShipType.SUBMARINE, 1);

    //fake instances of good and bad shots
    List<Coord> goodShotList = new ArrayList<Coord>();
    Coord goodShot1 = new Coord(0, 0);
    Coord goodShot2 = new Coord(0, 1);
    Coord goodShot3 = new Coord(1, 1);
    //adding them
    goodShotList.add(goodShot1);
    goodShotList.add(goodShot2);
    goodShotList.add(goodShot3);

    List<Coord> badShotList = new ArrayList<Coord>();
    Coord badShot1 = new Coord(5, 5);
    Coord badShot2 = new Coord(3, 2);
    badShotList.add(badShot1);
    badShotList.add(badShot2);

    //lists of all playerShots
    List<Coord> allShotList = new ArrayList<Coord>();
    allShotList.add(goodShot1);
    allShotList.add(goodShot2);
    allShotList.add(goodShot2);
    allShotList.add(badShot1);
    allShotList.add(badShot2);

    //good and badshort list for player2
    List<Coord> goodShotListAi = new ArrayList<Coord>();
    Coord goodShot1Ai = new Coord(3, 0);
    Coord goodShot2Ai = new Coord(4, 1);
    //adding them
    goodShotListAi.add(goodShot1Ai);
    goodShotListAi.add(goodShot2Ai);

    //EmptyList for badshots
    List<Coord> badShotListAi = new ArrayList<Coord>();
    //all AI shots
    List<Coord> allShotListAi = new ArrayList<Coord>();
    allShotListAi.add(goodShot1Ai);
    allShotListAi.add(goodShot2Ai);
    //now we set the values of the players using the previous info
    p1 = new ManualPlayer("manualForTest", fakeFleet, modelForTest, badShotList, goodShotList,
        allShotList);
    //setting values of surviving fleet
    Ship ship1 = new Ship(ShipType.CARRIER, 6, false);
    Ship ship2 = new Ship(ShipType.SUBMARINE, 3, false);
    p1.survivingFleet = Arrays.asList(ship1, ship2);

    //p2 has no surviving ships
    p2 = new AIPlayer("AI", fakeFleet, modelForTest, badShotListAi, goodShotListAi, allShotListAi);
    p2.survivingFleet = new ArrayList<Ship>();

    //instance of current player shots
    ArrayList<Coord> currPlayerShots = new ArrayList<Coord>();
    currPlayerShots.add(new Coord(3, 3));
    currPlayerShots.add(new Coord(5, 2));

    //now we update the battleModel to include other stuff
    modelForTest.setP1(p1);
    modelForTest.setP2(p2);
    modelForTest.setCurrentPlayerShots(currPlayerShots);

  }

  @Test
  public void testAddCarrier() {
    //adding a carrier at a list of certain coordinates of the 6x6 grid
    Ship carrierShip = new Ship(ShipType.CARRIER, 6, false);
    ArrayList<Coord> carrierCoords = new ArrayList<Coord>();
    carrierCoords.add(new Coord(0, 0));
    carrierCoords.add(new Coord(0, 1));
    carrierCoords.add(new Coord(0, 2));
    carrierCoords.add(new Coord(0, 3));
    carrierCoords.add(new Coord(0, 4));
    carrierCoords.add(new Coord(0, 5));
    manualGrid.addCarrier(carrierCoords, carrierShip);
    //now we make sure that the coordinates value is a "C"
    assertEquals("C",
        manualGrid.getGrid()[0][0]);
    assertEquals("C",
        manualGrid.getGrid()[1][0]);
    assertEquals("C",
        manualGrid.getGrid()[2][0]);
    assertEquals("C",
        manualGrid.getGrid()[3][0]);
    assertEquals("C",
        manualGrid.getGrid()[4][0]);
    assertEquals("C",
        manualGrid.getGrid()[5][0]);

  }

  @Test
  public void testAddBattleShipTest() {
    Ship battleship = new Ship(ShipType.BATTLESHIP, 5, false);
    ArrayList<Coord> battleshipCoords = new ArrayList<Coord>();
    battleshipCoords.add(new Coord(1, 0));
    battleshipCoords.add(new Coord(2, 0));
    battleshipCoords.add(new Coord(3, 0));
    battleshipCoords.add(new Coord(4, 0));
    battleshipCoords.add(new Coord(5, 0));

    manualGrid.addBattleShip(battleshipCoords);
    //now we make sure that the coordinates value is a "B"
    assertEquals("B",
        manualGrid.getGrid()[0][1]);
    assertEquals("B",
        manualGrid.getGrid()[0][2]);
    assertEquals("B",
        manualGrid.getGrid()[0][3]);
    assertEquals("B",
        manualGrid.getGrid()[0][4]);
    assertEquals("B",
        manualGrid.getGrid()[0][5]);

  }

  @Test
  public void testAddDestroyer() {

    Ship destroyer = new Ship(ShipType.DESTROYER, 4, false);
    ArrayList<Coord> destroyerCoords = new ArrayList<Coord>();
    destroyerCoords.add(new Coord(1, 3));
    destroyerCoords.add(new Coord(2, 3));
    destroyerCoords.add(new Coord(3, 3));
    destroyerCoords.add(new Coord(4, 3));

    manualGrid.addDestroyer(destroyerCoords);

    assertEquals("D",
        manualGrid.getGrid()[3][1]);
    assertEquals("D",
        manualGrid.getGrid()[3][2]);
    assertEquals("D",
        manualGrid.getGrid()[3][3]);
    assertEquals("D",
        manualGrid.getGrid()[3][4]);

  }

  @Test
  public void testAddSubmarine() {
    Ship submarine = new Ship(ShipType.SUBMARINE, 3, false);
    ArrayList<Coord> submarineCoords = new ArrayList<Coord>();
    submarineCoords.add(new Coord(1, 3));
    submarineCoords.add(new Coord(2, 3));
    submarineCoords.add(new Coord(3, 3));

    manualGrid.addSubmarine(submarineCoords);
    assertEquals("S",
        manualGrid.getGrid()[3][1]);
    assertEquals("S",
        manualGrid.getGrid()[3][2]);
    assertEquals("S",
        manualGrid.getGrid()[3][3]);
  }

  @Test
  public void testRecordSuccessfulShot() {

    Coord coord = new Coord(0, 0);
    Coord coord2 = new Coord(5, 1);

    //adding the successful shot to the grid
    manualGrid.recordSuccessfulShot(coord);
    manualGrid.recordSuccessfulShot(coord2);

    assertEquals("X",
        manualGrid.getGrid()[0][0]);

    assertEquals("X",
        manualGrid.getGrid()[1][5]);

  }

  @Test
  public void testRecordMissedShot() {
    Coord coord = new Coord(2, 0);
    Coord coord2 = new Coord(3, 5);


    //adding the missed shot to the grid
    manualGrid.recordMissedShot(coord);
    manualGrid.recordMissedShot(coord2);

    assertEquals("M",
        manualGrid.getGrid()[5][3]);

    assertEquals("M",
        manualGrid.getGrid()[0][2]);

  }

  @Test
  public void testValidShots() {

    //all the shots in the players field
    ArrayList<Coord> allShots = new ArrayList<Coord>();
    allShots.add(new Coord(1, 3));
    allShots.add(new Coord(0, 0));
    allShots.add(new Coord(4, 4));
    allShots.add(new Coord(5, 5));


    //set of valid shots
    ArrayList<Coord> validShots = new ArrayList<Coord>();
    validShots.add(new Coord(1, 1));
    validShots.add(new Coord(2, 3));
    validShots.add(new Coord(3, 3));

    //set of invalid shots
    ArrayList<Coord> invalidShots = new ArrayList<Coord>();
    invalidShots.add(new Coord(1, 3));
    invalidShots.add(new Coord(4, 3));
    invalidShots.add(new Coord(3, 5));

    //making sure it's true when the shots are not valid
    assertTrue(manualGrid.validShots(invalidShots, allShots));

    //making sure it's false when the shots are valid
    assertFalse(manualGrid.validShots(validShots, allShots));


  }



}