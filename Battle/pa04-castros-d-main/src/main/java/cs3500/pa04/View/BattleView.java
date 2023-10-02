package cs3500.pa04.View;


import cs3500.pa04.Model.Coord;
import cs3500.pa04.Model.ShipType;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Deals with the display aspects of the battleSalvo game
 */
public class BattleView {

  Scanner scan;
  Appendable output;

  public BattleView(Readable input, Appendable output) {
    this.scan = new Scanner(input);
    this.output = output ;
  }

  /**
   * Displays the welcome screen and asks user to input name
   * @return the name of the manual user
   */
  public String displayWelcomeGetName() {
    PrintStream stream = new PrintStream(System.out);
    stream.append("Greetings Salvo Soldier, Please Enter Your Name Below:");
    stream.println();
    return scan.nextLine();

  }

  /**
   * Displays the second part of the welcome screen and gets the board dimensions
   * @return the board dimensions
   * @throws IOException when the output can't be appended to the print stream
   */
  public ArrayList<Integer> displayWelcomeGetSize() throws IOException {
    ArrayList<Integer> sizes = new ArrayList<Integer>();
    output.append("-------------------------------------------------------------------------");
    output.append("\nNice to meet you!"
        + " Please enter a valid height game board between sizes 6-15 (inclusive) :");

    int height = scan.nextInt();
    while (height < 6 || height > 15) {
      output.append("");
      output.append("\n-------------------------------------------------------------------------");
      output.append("\nYikes! The height must be between the range" +
          " of 6-15 inclusive, please try again:");
      height = scan.nextInt();
    }
    output.append("\n-------------------------------------------------------------------------");
    System.out.print("\nCool! Now"
        + " Please enter a valid weight game board between sizes 6-15 (inclusive) :");

    int width = scan.nextInt();
    while (width < 6 || width > 15) {
      output.append("");
      output.append("\n-------------------------------------------------------------------------");
      output.append("\nYikes! The width must be between the range" +
          " of 6-15 inclusive, please try again:");
      //ask to put input again
      width = scan.nextInt();
    }
    //adding the sizes to the list
    sizes.add(height);
    sizes.add(width);

    return sizes;
  }

  /**
   * Displays the initial battle grids of both the player and the opponent
   * @param currentGrid the current state of the board
   */
  public void displayInitBattleGrid(String[][] currentGrid) {

    try {
      output.append("\n-------------------------------------------------------------------------");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nBased on the dimensions you provided this is how the boards look:");
    } catch (IOException ignore) {
    }
    try {
      output.append("");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nYour Board: ");
    } catch (IOException ignore) {
    }

    //looping through all the rows
    drawOneBoard(currentGrid);

    try {
      output.append("");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nEnemy Board: ");
    } catch (IOException ignore) {
    }

    //looping through all the rows
    drawOneBoard(currentGrid);


  }


  /**
   * Displays the fleet selection options
   * @param smallestSize the smallest of the two grid dimensions
   * @return the list of the fleet numbers
   */
  public ArrayList<Integer> fleetSelection(int smallestSize) {
    ArrayList<Integer> fleetNumbers = new ArrayList<Integer>();
    int sum = 0;
    try {
      output.append("\n-------------------------------------------------------------------------");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nPlease choose your fleet in the order " +
          "[Carrier, Battleship, Destroyer, Submarine].");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nKeep in mind, your fleet must not exceed size " + smallestSize);
    } catch (IOException ignore) {
    }

    int carriers = scan.nextInt();
    int battleShips = scan.nextInt();
    int destroyers = scan.nextInt();
    int submarines = scan.nextInt();

    sum = carriers + battleShips + destroyers + submarines;

    while( (sum > smallestSize) || (checkInvalid(carriers, battleShips, destroyers, submarines))) {
      try {
        output.append("\nYou entered invalid fleet sizes. Remember your fleet must not exceed size "
            + smallestSize);
      } catch (IOException ignore) {
      }
      try {
        output.append("\n Please choose your fleet in the order " +
            "[Carrier, Battleship, Destroyer, Submarine].");
      } catch (IOException ignore) {
      }
      carriers = scan.nextInt();
      battleShips = scan.nextInt();
      destroyers = scan.nextInt();
      submarines = scan.nextInt();
      sum = carriers + battleShips + destroyers + submarines;

    }
    //adding them to a list
    fleetNumbers.add(carriers);
    fleetNumbers.add(battleShips);
    fleetNumbers.add(destroyers);
    fleetNumbers.add(submarines);
    return fleetNumbers;

  }

  /**
   * Checks if any of the integers is 0
   * @param c number of carriers
   * @param b  number of battleships
   * @param d number of destroyers
   * @param s number of battleships
   * @return whether any of the parameters is equal to 0
   */
  public boolean checkInvalid(int c, int b, int d, int s) {
    ArrayList<Integer> allInt = new ArrayList<Integer>();
    allInt.add(c);
    allInt.add(b);
    allInt.add(d);
    allInt.add(s);

    boolean valid = false;
    for (int i = 0; i <  allInt.size(); i++) {
      int current =  allInt.get(i);
      if (current == 0) {
        valid = true;
      }
    }
    return valid;
  }

  /**
   * Prints out a 2D Array that represents the current grid
   * @param currentGrid
   */
  public void drawOneBoard(String[][] currentGrid) {
    //looping through all the rows
    for (int i = 0; i < currentGrid.length; i++) {
      //inner for getting the elements inside row
      for (int a = 0; a < currentGrid[i].length; a++) {
        String currentCell = currentGrid[i][a];
      }
      //displaying the grid
      try {
        output.append("\n"+Arrays.toString(currentGrid[i]));
      } catch (IOException ignore) {
      }

    }
  }


  /**
   * Displays the updated player grid
   * @param currentGrid
   */
  public void drawUpdatedPlayerGrid(String[][] currentGrid) {
    try {
      output.append("\n-------------------------------------------------------------------------");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nThis is your current grid:");
    } catch (IOException ignore) {
    }
    try {
      output.append(" ");
    } catch (IOException ignore) {
    }
    drawOneBoard(currentGrid);
  }


  public void drawUpdatedAIData(String[][] currentGrid) {
    try {
      output.append("\n-------------------------------------------------------------------------");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nThis is the AI Current data:");
    } catch (IOException ignore) {
    }
    try {
      output.append(" ");
    } catch (IOException ignore) {
    }
    drawOneBoard(currentGrid);
  }

  /**
   * Displays the number of shots a user can take and gets the coordinates
   * @param numOfShots the number of shots a user needs to take
   * @param width the width of the board
   * @param height the height of the board
   * @return the list of coordinate of the shots the user took
   */
  public ArrayList<Coord> askForShots(int numOfShots, int width, int height, List<Coord> previousShots) {
    try {
      output.append("\n-------------------------------------------------------------------------");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nIt's time to shoot! You have " + numOfShots +
          " shots in total to take");
    } catch (IOException ignore) {
    }
    try {
      output.append("\nEnter your shoots in this format: x and then y for a "
          + height + "x" + width + " board");
    } catch (IOException ignore) {
    }
    //initial values
    ArrayList<Coord> shotList = new ArrayList<>();

    shotsHelper(numOfShots, shotList);

    //ADD the While Loop here
    while(! (checkShots(shotList, width, height, previousShots))) {
      shotList.clear();
      try {
        output.append("\nYou entered invalid shots. Remember your shots coordinates must be " +
            "within the size of the board.");
      } catch (IOException ignore) {
      }
      try {
        output.append("\nEnter your shoots in this format: x and then y for a "
            + height + "x" + width + " board");
      } catch (IOException ignore) {
      }
      shotsHelper(numOfShots, shotList);

    }
    return shotList;
  }

  public static boolean checkShots(List<Coord> attemptedShots,  int width, int height, List<Coord> previousShots) {
    //if check shots passes then accept them
    //also make sure that the shots taken are not in the previousShots

    ArrayList<String> noList = new ArrayList<>();
    boolean passedCheck = true;
    for(int i = 0; i < attemptedShots.size(); i++) {
      Coord coordAtIndex = attemptedShots.get(i);
      int xCoord = coordAtIndex.getX();
      int yCoord = coordAtIndex.getY();
      if( ( ( !(xCoord >= 0)) || (!(xCoord <= width -1)) || (!(yCoord >= 0)) || (!(yCoord <= height - 1)) ||
          (previousShots.contains(coordAtIndex)))) {
        noList.add("NO");
      }
      }
    return (! (noList.contains("NO")));
    }



    public void shotsHelper(int numOfShots, ArrayList<Coord>  coordList) {

      for (int i = 0; i < numOfShots; i++) {
        int xCoord = scan.nextInt();
        int yCoord = scan.nextInt();
        //making a coordinate from those shots
        Coord cordCreated = new Coord(xCoord, yCoord);
        //adding it to the output list
        coordList.add(cordCreated);
      }
    }



    public void afterShotsUpdate(List<Coord> failedShots, List<Coord> damagedCoords) {

    System.out.println("\nHere are the results for this round :) : ");
   // System.out.println("Your shots that succesfully hit the other Player :D : ");
     // afterShotsHelper(succesfulShots);
    System.out.println("Your shots that did not hit the other Player :o: ");
      afterShotsHelper(failedShots);
      System.out.println("Places where the other player hit your ships :( : ");
      afterShotsHelper(damagedCoords);
    }

    public void afterShotsHelper(List<Coord> listOfCoords) {
    //take each x value of a cord
      //take each y value of a coord
      //and print them out together
      for (int i = 0; i < listOfCoords.size() ; i++) {
        Coord coordAtIndex = listOfCoords.get(i);
        int xAtIndex = coordAtIndex.getX();
        int yAtIndex = coordAtIndex.getY();
        System.out.println("[" + xAtIndex + "," + yAtIndex + "]");
      }

    }





  }





