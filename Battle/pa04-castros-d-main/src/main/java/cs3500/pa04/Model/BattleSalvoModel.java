package cs3500.pa04.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleSalvoModel {
  ManualPlayer p1;
  AIPlayer p2;
  BattleGrid mpGrid;
  BattleGrid p2Grid;
  private ArrayList<Coord> currentPlayerShots;
  private Boolean gameDone;

  public BattleSalvoModel(BattleGrid mpGrid, BattleGrid p2Grid) {
    this.mpGrid = mpGrid;
    this.p2Grid = p2Grid;
    this.gameDone = false;
  }

  /**
   * Gets the grid of the manual player
   *
   * @return the grid of the manual player
   */
  public BattleGrid getMpGrid() {
    return mpGrid;
  }

  /**
   * Gets the Grid of the AI Player
   *
   * @return the grid of the AI Player
   */
  public BattleGrid getP2Grid() {
    return p2Grid;
  }

  public void setCurrentPlayerShots(ArrayList<Coord> currentShots) {
    this.currentPlayerShots = currentShots;
  }

  public ArrayList<Coord> getCurrentPlayerShots() {
    return this.currentPlayerShots;
  }

  public AIPlayer getP2Player() {
    return p2;
  }

  public ManualPlayer getP1Player() {
    return p1;
  }

  public void setP2(AIPlayer aiPlayer) {
    p2 = aiPlayer;
  }

  public void setP1(ManualPlayer mPlayer) {
    p1 = mPlayer;
  }

  /**
   * Determines the game result for both players
   *
   * @return the gameResult for bothPlayers
   */
  public ArrayList<GameResult> determineResult() {
    ArrayList<GameResult> resultsForPlayers = new ArrayList<GameResult>();
    //the 0th index is the first players result
    //the 1st index is the second players result

    //this is when the first player lose
    if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() > 0)) {
      resultsForPlayers.add(GameResult.LOSE);
      resultsForPlayers.add(GameResult.WIN);
      setGameDone(true);
    }
    //if manual player one wins
    if ((p1.survivingFleet.size() > 0) && (p2.survivingFleet.size() == 0)) {
      resultsForPlayers.add(GameResult.WIN);
      resultsForPlayers.add(GameResult.LOSE);
      setGameDone(true);
    }
    //draw
    else if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() == 0))
      resultsForPlayers.add(GameResult.DRAW);
       resultsForPlayers.add(GameResult.DRAW);
    setGameDone(true);


    return resultsForPlayers;
  }


  public ArrayList<String> determineReason() {
    ArrayList<String> reasons = new ArrayList<String>();
    //the 0th index is the first players reason
    //the 1st index is the second players reason

    //this is when the first player lose
    if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() > 0)) {
      reasons.add("Unfortunately, player 1 lost because the destroyed your fleet faster than you did theirs :(");
      reasons.add("Congratulations player 2! You won since you destroyed player 1's fleet faster than them");
    }
    //if manual player one wins
    if ((p1.survivingFleet.size() > 0) && (p2.survivingFleet.size() == 0)) {
      reasons.add("Congratulations player 1! You won since you destroyed player 2's fleet faster than them");
      reasons.add("Unfortunately, player 2 lost because the destroyed your fleet faster than you did theirs :(");
    }
    //draw
    if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() == 0))
      reasons.add("Both of the fleets have been completely destroyed, so its a tie :|");
    reasons.add("Both of the fleets have been completely destroyed, so its a tie :|");


    return reasons;
  }


  public Boolean getGameDone() {
    return gameDone;
  }

  public void setGameDone(Boolean gameDone) {
    this.gameDone = gameDone;
  }

  public boolean isGameDone() {
    if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() > 0)) {
      return true;
    }
    if ((p1.survivingFleet.size() > 0) && (p2.survivingFleet.size() == 0)) {
      return true;
    }
     if ((p1.survivingFleet.size() == 0) && (p2.survivingFleet.size() == 0)) {
      return true;
    }
     else return false;
  }







}
