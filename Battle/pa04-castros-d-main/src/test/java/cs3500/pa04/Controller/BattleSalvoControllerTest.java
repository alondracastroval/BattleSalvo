package cs3500.pa04.Controller;

import static org.junit.jupiter.api.Assertions.*;

import cs3500.pa04.Model.AIPlayer;
import cs3500.pa04.Model.BattleGrid;
import cs3500.pa04.Model.BattleSalvoModel;
import cs3500.pa04.Model.Coord;
import cs3500.pa04.Model.ManualPlayer;
import cs3500.pa04.Model.Ship;
import cs3500.pa04.Model.ShipType;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BattleSalvoControllerTest {



  BattleSalvoController controllerTest;

  ManualPlayer p1;
  AIPlayer p2;
  BattleGrid manualGrid;
  BattleGrid player2Grid;

  ArrayList<Coord> currentPlayerShots;

  BattleSalvoModel modelForTest;


  @BeforeEach
  public void setup() {
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    controllerTest = new BattleSalvoController(input, output);

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
  public void testInitHashMap() {

    //array list of integers we will be passing in
    ArrayList<Integer> listOfIntegers = new ArrayList<Integer>();
    //adding one 4 times, to signify 1 of each ship
    for (int i = 0; i < 4; i++) {
      listOfIntegers.add(1);
    }
    //now the final product should be
    HashMap<ShipType, Integer> endFleet = new HashMap<ShipType, Integer>();
    endFleet.put(ShipType.CARRIER, listOfIntegers.get(0));
    endFleet.put(ShipType.BATTLESHIP, listOfIntegers.get(1));
    endFleet.put(ShipType.DESTROYER, listOfIntegers.get(2));
    endFleet.put(ShipType.SUBMARINE, listOfIntegers.get(3));

    ///now we can see if they end up being the same thing
    //when we use our initHashMap
    assertEquals(endFleet, controllerTest.initFleetHashMap(listOfIntegers));

    //now lets try with different numbers for each ship
    ArrayList<Integer> listOfInts = new ArrayList<Integer>();
    listOfInts.add(1);
    listOfInts.add(2);
    listOfInts.add(3);
    listOfInts.add(4);

    HashMap<ShipType, Integer> endFleetDifNums = new HashMap<ShipType, Integer>();
    endFleetDifNums.put(ShipType.CARRIER, listOfInts.get(0));
    endFleetDifNums.put(ShipType.BATTLESHIP, listOfInts.get(1));
    endFleetDifNums.put(ShipType.DESTROYER, listOfInts.get(2));
    endFleetDifNums.put(ShipType.SUBMARINE, listOfInts.get(3));

    //now let's check
    assertEquals(endFleetDifNums, controllerTest.initFleetHashMap(listOfInts));
  }



}