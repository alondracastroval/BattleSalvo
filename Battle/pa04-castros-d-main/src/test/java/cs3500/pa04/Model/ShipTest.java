package cs3500.pa04.Model;

import static org.junit.jupiter.api.Assertions.*;

import cs3500.pa04.Json.CoordRecord;
import cs3500.pa04.Json.UnserializedShipAdapter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipTest {

  Ship ship1;
  Ship ship2;
  ArrayList<Coord> occupiedLocations;
  ArrayList<Coord> allLocations;


  @BeforeEach
  public void setUp() {
    //creating definitions of ship for tests
    occupiedLocations = new ArrayList<Coord>();
    allLocations = new ArrayList<Coord>();
    ArrayList<Coord> destroyedLocations = new ArrayList<Coord>();
    //adding element
    Coord c1 = new Coord(2, 1);
    Coord c2 = new Coord(0, 0);
    Coord c3 = new Coord(1, 1);
    occupiedLocations.add(c1);
    occupiedLocations.add(c2);
    occupiedLocations.add(c2);

    //adding to the other one
    allLocations.add(c1);
    allLocations.add(c2);
    allLocations.add(c3);

    ship1 = new Ship(ShipType.SUBMARINE, occupiedLocations, destroyedLocations,
        false, 3);

    ArrayList<Coord> ship2Locations = new ArrayList<Coord>();
    ship2Locations.add(new Coord(3, 3));
    ship2Locations.add(new Coord(2, 2));
    ship2Locations.add(new Coord(5, 5));



    ship2 = new Ship(ShipType.SUBMARINE, ship2Locations, destroyedLocations, false, 3);

  }

  @Test
  public void testMakeOneShip() {

    ArrayList<Ship> result = new ArrayList<Ship>();
    result.add(ship2);
    //makes sure it makes a list of only size1
    assertEquals(result, Ship.makeAShip(ShipType.SUBMARINE, 1));

    //let's try again for a bigger list
    ArrayList<Ship> result1 = new ArrayList<Ship>();
    Ship firstShipCarrier = new Ship(ShipType.CARRIER, 6, false);
    Ship secondShipCarrier = new Ship(ShipType.CARRIER, 6, false);
    result1.add(firstShipCarrier);
    result1.add(secondShipCarrier);


    //now lets call the method and see that it equals this
    assertEquals(result1, Ship.makeAShip(ShipType.CARRIER, 2));

    //let's try with a list of destroyers now
    ArrayList<Ship> result2 = new ArrayList<Ship>();
    Ship firstShipDestroyer = new Ship(ShipType.DESTROYER, 4, false);
    Ship secondShipDestroyer = new Ship(ShipType.DESTROYER, 4, false);
    result2.add(firstShipDestroyer);
    result2.add(secondShipDestroyer);
    assertEquals(result2, Ship.makeAShip(ShipType.DESTROYER, 2));

    //now let's try with a list of battleship
    ArrayList<Ship> result3 = new ArrayList<Ship>();
    Ship firstShipBattleship = new Ship(ShipType.BATTLESHIP, 5, false);
    Ship secondShipBattleship = new Ship(ShipType.BATTLESHIP, 5, false);
    Ship thirdShipBattleship = new Ship(ShipType.BATTLESHIP, 5, false);

    result3.add(firstShipBattleship);
    result3.add(secondShipBattleship);
    result3.add(thirdShipBattleship);
    assertEquals(result3, Ship.makeAShip(ShipType.BATTLESHIP, 3));



  }

  @Test
  public void testGetType() {

    assertEquals(ShipType.SUBMARINE, ship2.getType());
  }

  @Test
  public void testIsThereRepeats() {
    assertTrue(ship1.isThereRepeats(allLocations, occupiedLocations));
    assertFalse(ship2.isThereRepeats(allLocations, (ArrayList<Coord>) ship2.occupiedLocations));

  }

  @Test
  public void testGenerateMiddle() {
    //making the values to pass into the method
    Coord beginningCoord = new Coord(0, 0);
    Coord horizontalEnd = new Coord(2, 0);
    ArrayList<Coord> endList = new ArrayList<Coord>();
    endList.add(beginningCoord);
    endList.add(horizontalEnd);
    //the end list should then equal this list
    ArrayList<Coord> endListCheck = new ArrayList<Coord>();
    endListCheck.add(beginningCoord);
    endListCheck.add(horizontalEnd);
    endListCheck.add(new Coord(0, 1));
    Coord actualEnd = new Coord(2, 0);
    ship1.generateMiddle(new Coord(0, 0), new Coord(0, 2), new Coord(2, 0), endList, actualEnd);
    assertEquals(endListCheck, endList);
    //NOW LETS TRY generating another end
    Coord beginningCoord1 = new Coord(0, 0);
    Coord verticalEnd1 = new Coord(2, 0);
    Coord horizontalEnd1 = new Coord(0, 2);
    ArrayList<Coord> endList1 = new ArrayList<Coord>();
    endList1.add(beginningCoord1);
    endList1.add(horizontalEnd1);
    //the end list should then equal this list
    ArrayList<Coord> endListCheck1 = new ArrayList<Coord>();
    endListCheck1.add(beginningCoord1);
    endListCheck1.add(horizontalEnd1);
    endListCheck1.add(new Coord(2, 0));
    Coord actualEnd1 = new Coord(2, 0);
    ship1.generateMiddle(beginningCoord1, verticalEnd1, horizontalEnd1, endList1, actualEnd1);
  }

  @Test
  public void testIsShipDestoryed() {
    assertFalse(ship1.isShipDestroyed());
    assertFalse(ship2.isShipDestroyed());

    //now lets add a destroyed location 3 times and it should
    //come out to true now
    ship1.addADestroyedLocation();
    ship1.addADestroyedLocation();
    ship1.addADestroyedLocation();

    //now it should be true
    assertTrue(ship1.isShipDestroyed());

    //lets try it on ship2
    ship2.addADestroyedLocation();
    ship2.addADestroyedLocation();
    ship2.addADestroyedLocation();

    //now it should be true again
    assertTrue(ship2.isShipDestroyed());

  }

  @Test
  public void testAddDestroyedLocation() {
    //first a ship has no destroyed locations
    assertEquals(0, ship1.destroyedLocationsCount);
    assertEquals(0, ship2.destroyedLocationsCount);

    //but not if we add 1 and 2 they both change!
    ship1.addADestroyedLocation();

    //for ship2
    ship2.addADestroyedLocation();
    ship2.addADestroyedLocation();

    assertEquals(1, ship1.destroyedLocationsCount);
    assertEquals(2, ship2.destroyedLocationsCount);

  }

  @Test
  public void testSetOccupiedLocations() {
    //at first the occupied locations are the ones we set in setup Method
    assertEquals(occupiedLocations, ship1.occupiedLocations);

    //but now if we change it to another one, it should change
    ArrayList<Coord> updatedCoords = new ArrayList<Coord>();
    updatedCoords.add(new Coord(3, 4));
    updatedCoords.add(new Coord(3, 3));
    updatedCoords.add(new Coord(1, 1));

    //let's mutate!
    ship1.setOccupiedLocations(updatedCoords);
    //should equal the updatedCoords list now
    assertEquals(updatedCoords, ship1.occupiedLocations);
  }

  @Test
  public void testConvertShipToAdapter() {
    //let's convert a list of size 2
    List<Ship> listOfShips = new ArrayList<Ship>();
    listOfShips.add(ship1);
    listOfShips.add(ship2);

    //the end result should be this list
    List<UnserializedShipAdapter> listOfShipsAdapts = new ArrayList<UnserializedShipAdapter>();
    CoordRecord ship1Coord = new CoordRecord(ship1.getOccupiedLocations().get(0).getX(),
        ship1.getOccupiedLocations().get(0).getY());
    CoordRecord ship2Coord = new CoordRecord(ship2.getOccupiedLocations().get(0).getX(),
        ship2.getOccupiedLocations().get(0).getY());

    UnserializedShipAdapter ship1Adapted = new UnserializedShipAdapter(ship1Coord, ship1.size,
        ship1.direction);
    UnserializedShipAdapter ship2Adapted = new UnserializedShipAdapter(ship2Coord, ship2.size,
        ship2.direction);

    listOfShipsAdapts.add(ship1Adapted);
    listOfShipsAdapts.add(ship2Adapted);

    //now lets make the list and make sure it's the same thing!
    assertEquals(listOfShipsAdapts, Ship.convertShipToAdapter(listOfShips));

  }

  @Test
  public void testEquals() {
    //testing if we overrode equals correctly
    List<Coord> empty = new ArrayList<Coord>();
    Ship shipForTest = new Ship(ShipType.DESTROYER, occupiedLocations, empty,
        false, 6);
    Ship shipForTest2 = new Ship(ShipType.CARRIER, occupiedLocations, empty,
        false, 4);

    assertTrue(ship1.equals(ship2));
    assertNotEquals(shipForTest, shipForTest2);


  }






}