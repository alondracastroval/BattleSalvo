package cs3500.pa04.Controller;

import cs3500.pa04.Model.AIPlayer;
import cs3500.pa04.Model.BattleGrid;
import cs3500.pa04.Model.BattleSalvoModel;
import cs3500.pa04.Model.Coord;
import cs3500.pa04.Model.GameResult;
import cs3500.pa04.Model.ManualPlayer;
import cs3500.pa04.Model.Player;
import cs3500.pa04.Model.Ship;
import cs3500.pa04.Model.ShipType;
import cs3500.pa04.View.BattleView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Deals with the using the input of a user
 */
public class BattleSalvoController implements Controller {

  Readable input;
  Appendable output;

  public ManualPlayer manPlayer;
  public AIPlayer aiPlayer;


  /**
   * constructor for the battleSalvoController
   * @param input the readable
   * @param output the appendable
   */
  public BattleSalvoController(Readable input, Appendable output) {
    this.input = Objects.requireNonNull(input);
    this.output = Objects.requireNonNull(output);
    this.manPlayer = manPlayer;
    this.aiPlayer = aiPlayer;
  }

  /**
   * Starts a game of BattleSalvo
   * @throws IOException
   *
   */
  @Override
  public void runGame() throws IOException {

    //instance of view class
    BattleView viewInstance = new BattleView(input, output);
    String p1Name = viewInstance.displayWelcomeGetName();
    ArrayList<Integer> boardDimensions = viewInstance.displayWelcomeGetSize();

    //parsing the size values into the BattleGrid
    int height = boardDimensions.get(0);
    int width = boardDimensions.get(1);
    //new instance of the grid
    BattleGrid userGrid = new BattleGrid(height, width);
    BattleGrid arIGrid = new BattleGrid(height, width);
    //making the blank grid for both
    userGrid.makeUserGrid();
    arIGrid.makeUserGrid();

    String[][] grid = userGrid.getGrid();
    String[][] aIGrid = arIGrid.getGrid();

    //displaying the initial board using the blank grid
    viewInstance.displayInitBattleGrid(grid);

    //getting the fleet from the user
    int maxFleetSize = (Collections.min(boardDimensions));
    ArrayList<Integer> fleetSizes = viewInstance.fleetSelection(maxFleetSize);

    //turning the fleet into a map
    HashMap<ShipType, Integer> emptyFleet = initFleetHashMap(fleetSizes);

    //create the instance of the model with the grids and the fleet
    BattleSalvoModel bms = new BattleSalvoModel(userGrid, arIGrid);

    //now we make instances of the players with this bms instance
    manPlayer = new ManualPlayer(p1Name,emptyFleet,bms, new ArrayList<Coord>(), new ArrayList<Coord>(),
        new ArrayList<Coord>());
    aiPlayer =  new AIPlayer("AI", emptyFleet, bms, new ArrayList<Coord>(), new ArrayList<Coord>(),
        new ArrayList<Coord>());

    //now we add the players to the model
    bms.setP1(manPlayer);
    bms.setP2(aiPlayer);

    //now lets create the set up
    manPlayer.setup(height, width, emptyFleet);
    //display
    viewInstance.drawUpdatedPlayerGrid(grid);

    //create the set up for the AI Player
    aiPlayer.setup(height, width, emptyFleet);
    //NOW DISPLAY THE EMPTY ENEMY GRID
    viewInstance.drawUpdatedAIData(aIGrid);

    //THIS IS WHERE THE GAME STARTS

    /*
    //getting the shots from the user
    ArrayList<Coord> userShots = viewInstance.askForShots(manPlayer.survivingFleet.size(), width, height);
    //setting them as the field of the model, it will get updated each time to player shoots
     bms.setCurrentPlayerShots(userShots);
     //printing out the values to make sure that they get set correctly
    manPlayer.takeShots();
    //updated grid
    viewInstance.drawUpdatedAIData(aIGrid);
    //checking to see if the ai works
    aiPlayer.takeShots();
    viewInstance.drawUpdatedPlayerGrid(grid);
    //viewInstance.afterShotsUpdate(userShots, userShots, userShots);

     */
    gameLoop(manPlayer, aiPlayer, bms, viewInstance, width, height, grid, aIGrid);
  }

  /**
   * Turns the fleet typed in by the user into a hashmap that can be used
   * in the other methods
   * @param fleetSizes the passed in integers by the user
   * @return a hashmap of ship type with its respective sizes
   */


  public HashMap<ShipType, Integer> initFleetHashMap(ArrayList<Integer> fleetSizes) {
    //turning the fleet into a map
    HashMap<ShipType, Integer> emptyFleet = new HashMap<ShipType, Integer>();
    //now we add the elements
    emptyFleet.put(ShipType.CARRIER,fleetSizes.get(0));
    emptyFleet.put(ShipType.BATTLESHIP,fleetSizes.get(1));
    emptyFleet.put(ShipType.DESTROYER,fleetSizes.get(2));
    emptyFleet.put(ShipType.SUBMARINE,fleetSizes.get(3));
    return emptyFleet;
  }

  /**
   * runs the loop of the game
   * @param p1 the manual player
   * @param p2 the AI Player
   * @param bms the controller
   * @param viewInstance an instane of the view
   * @param width the chosen width
   * @param height the chosen height
   * @param p1Grid the grid of the manual player
   * @param p2Grid the grid of the AI Player
   */

  public void gameLoop(ManualPlayer p1, AIPlayer p2, BattleSalvoModel bms, BattleView viewInstance, int width,
                       int height, String[][] p1Grid, String[][] p2Grid) {

    ArrayList<GameResult> results = new ArrayList<GameResult>();
    ArrayList<String> reasons = new ArrayList<String>();

    //while the game isn't done
    while (!bms.isGameDone()) {
      //get the shots from the manualUser first
      //changes  note, added a field that would take the enemy ship count
      ArrayList<Coord> userShots =
          viewInstance.askForShots(p1.survivingFleet.size(), width, height,p1.allPlayerShots);
      //setting them as the field of the model, it will get updated each time the player shoots
      bms.setCurrentPlayerShots(userShots);
      //BOTH OF THE PLAYERS TAKE THEIR SHOTS SIMULTANEOUSLY
      List<Coord> p1sShots = p1.takeShots();
      List<Coord> p2Shots = p2.takeShots();
      //then showing the updated grids
      viewInstance.drawUpdatedPlayerGrid(p1Grid);
      viewInstance.drawUpdatedAIData(p2Grid);
      //getting the data
      List<Coord> p1Damage = p1.reportDamage(p2.allPlayerShots);
      List<Coord> p1Succesful = p1.goodShots;
      List<Coord> p1BadShots = p1.badShots;
      //showing the results (so calling afterShotsUpdate)
      viewInstance.afterShotsUpdate(p1BadShots, p1Damage);
      p1.successfulHits(p1Succesful);

    }
    //if the game is done call these
    if (bms.isGameDone()) {
      results = bms.determineResult();
      GameResult p1Result = results.get(0);
      GameResult p2Result = results.get(1);
      reasons = bms.determineReason();

      String p1Reason = reasons.get(0);
      String p2Reason = reasons.get(0);

      p1.endGame(p1Result, p1Reason);
      p2.endGame(p2Result, p2Reason);

    }
  }

}

