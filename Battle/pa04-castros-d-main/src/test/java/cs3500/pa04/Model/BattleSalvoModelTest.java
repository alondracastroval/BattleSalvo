package cs3500.pa04.Model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BattleSalvoModelTest {

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
  public void testDetermineResult() {
    ArrayList<GameResult> resultsForPlayers = new ArrayList<GameResult>();
    resultsForPlayers.add(GameResult.WIN);
    resultsForPlayers.add(GameResult.LOSE);
    resultsForPlayers.add(GameResult.DRAW);


    //based on the definitions in setup the list above should be the result
    //for p1 Winning
    assertEquals(resultsForPlayers, modelForTest.determineResult());



    //now we mutate the values to get a tie
    p1.survivingFleet = new ArrayList<Ship>();
    //now it should be giving a different list of results
    ArrayList<GameResult> resultsForPlayersTie = new ArrayList<GameResult>();
    resultsForPlayersTie.add(GameResult.DRAW);
    resultsForPlayersTie.add(GameResult.DRAW);
    assertEquals(resultsForPlayersTie, modelForTest.determineResult());

    //now lets mutate so that p2 wins!
    Ship ship1 = new Ship(ShipType.CARRIER, 6, false);
    Ship ship2 = new Ship(ShipType.SUBMARINE, 3, false);
    p2.survivingFleet = Arrays.asList(ship1, ship2);

    //now
    ArrayList<GameResult> resultsForPlayersP1Lose = new ArrayList<GameResult>();
    resultsForPlayersP1Lose.add(GameResult.LOSE);
    resultsForPlayersP1Lose.add(GameResult.WIN);
    resultsForPlayersP1Lose.add(GameResult.DRAW);
    assertEquals(resultsForPlayersP1Lose, modelForTest.determineResult());
  }

  @Test
  public void testDetermineReason() {

    //testing case when P1Wins
    ArrayList<String> reasonsP1Wins = new ArrayList<String>();
    reasonsP1Wins.add("Congratulations player 1! You won since you destroyed player 2's fleet"
        +
        " faster than them");
    reasonsP1Wins.add("Unfortunately, player 2 lost because the destroyed"
        +
        " your fleet faster than you did theirs :(");
    reasonsP1Wins.add("Both of the fleets have been completely destroyed, so its a tie :|");

    assertEquals(reasonsP1Wins, modelForTest.determineReason());


    //now we mutate the values to get a tie
    p1.survivingFleet = new ArrayList<Ship>();
    //now it should be giving a different list of results
    ArrayList<String> resultsForPlayersTie = new ArrayList<String>();
    resultsForPlayersTie.add("Both of the fleets have been completely destroyed, so its a tie :|");
    resultsForPlayersTie.add("Both of the fleets have been completely destroyed, so its a tie :|");
    assertEquals(resultsForPlayersTie, modelForTest.determineReason());


    //now lets mutate so that p2 wins!
    Ship ship1 = new Ship(ShipType.CARRIER, 6, false);
    Ship ship2 = new Ship(ShipType.SUBMARINE, 3, false);
    p2.survivingFleet = Arrays.asList(ship1, ship2);

    //now
    ArrayList<String> reasonsForPlayersP1Lose = new ArrayList<String>();
    reasonsForPlayersP1Lose.add("Unfortunately, player 1 lost because the "
        +
        "destroyed your fleet faster than you did theirs :(");
    reasonsForPlayersP1Lose.add("Congratulations player 2! You "
        +
        "won since you destroyed player 1's fleet faster than them");
    reasonsForPlayersP1Lose.add("Both of the fleets have been completely destroyed, so its "
        +
        "a tie :|");
    assertEquals(reasonsForPlayersP1Lose, modelForTest.determineReason());

  }

  @Test
  public void testIsGameDone() {

    //testing is gameDone when P2's fleet has no ships left
    assertTrue(modelForTest.isGameDone());

    //now when there is a tie
    //now we mutate the values to get a tie
    p1.survivingFleet = new ArrayList<Ship>();
    assertTrue(modelForTest.isGameDone());


    //now when p1 reaches 0 ships left first
    //now lets mutate so that p2 wins!
    Ship ship1 = new Ship(ShipType.CARRIER, 6, false);
    Ship ship2 = new Ship(ShipType.SUBMARINE, 3, false);
    p2.survivingFleet = Arrays.asList(ship1, ship2);
    assertTrue(modelForTest.isGameDone());
  }

  @Test
  public void testGetMpGrid() {
    assertEquals(manualGrid, modelForTest.getMpGrid());
  }

  @Test
  public void testGetP2Grid() {
    assertEquals(player2Grid, modelForTest.getP2Grid());
  }

  @Test
  public void testGetP2Player() {
    assertEquals(p2, modelForTest.getP2Player());
  }

  @Test
  public void testGetP1Player() {
    assertEquals(p1, modelForTest.getP1Player());
  }


}