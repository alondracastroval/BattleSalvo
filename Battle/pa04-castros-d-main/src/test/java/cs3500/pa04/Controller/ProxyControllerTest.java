package cs3500.pa04.Controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.Json.CoordRecord;
import cs3500.pa04.Json.EndGameArgs;
import cs3500.pa04.Json.JsonUtils;
import cs3500.pa04.Json.MessageJson;
import cs3500.pa04.Json.OnlyFleet;
import cs3500.pa04.Json.UnserializedShipAdapter;
import cs3500.pa04.Model.AIPlayer;
import cs3500.pa04.Model.Coord;
import cs3500.pa04.Model.ManualPlayer;
import cs3500.pa04.Model.Mocket;
import cs3500.pa04.Model.Ship;
import cs3500.pa04.Model.ShipType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProxyControllerTest {

  Ship ship1;
  Ship ship2;
  ArrayList<Coord> occupiedLocations;
  ArrayList<Coord> allLocations;

  ProxyController proxyControllerTest;
  private ByteArrayOutputStream testLog;
  //private ProxyDealer dealer;
  private AIPlayer player;




  @BeforeEach
  public void setUp() throws IOException {
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

    //making definitions for the proxy controller
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    BattleSalvoController  battleSalvoController = new BattleSalvoController(input, output);

    Socket server = new Socket("0.0.0.0", 35001);
    proxyControllerTest = new ProxyController(server, battleSalvoController);

    this.testLog = new ByteArrayOutputStream(200000);
    assertEquals("", logToString());


  }

  @Test
  public void serializeList() {
    //takes in a list of unserializedShipA

    //beginning list of ship
    List<Ship> ogList = new ArrayList<Ship>();
    ogList.add(ship1);
    ogList.add(ship2);
    //converting it to a list of unserializedShips
    List<UnserializedShipAdapter> unserializedShips = Ship.convertShipToAdapter(ogList);

    List<JsonNode> correctList = new ArrayList<>();
    JsonNode oneNode = JsonUtils.serializeRecord(unserializedShips.get(0));
    JsonNode twoNode = JsonUtils.serializeRecord(unserializedShips.get(1));
    correctList.add(oneNode);
    correctList.add(twoNode);
    assertEquals(correctList,
        proxyControllerTest.serializeList(unserializedShips));
  }

  /*
  @Test
  public void testConvertToCoordRecord(List<Coord> shots) {
    Coord oneCoord = new Coord(0, 0);
    Coord twoCoord = new Coord(1, 2);
    shots.add(oneCoord);
    shots.add(twoCoord);

    CoordRecord c1 = new CoordRecord(0, 0);
    CoordRecord c2 = new CoordRecord(1, 2);
    List<CoordRecord> convertedList = new ArrayList<>();
    convertedList.add(c1);
    convertedList.add(c2);
    assertEquals(convertedList, proxyControllerTest.convertToCoordRecord(shots));


  }


   */

  @Test
  public void testSerializeCoordRecordList() {
    //takes in a list of CoordRecord
  }



  @Test
  public void testAddIntegers() {

    ArrayList<Integer> integerList =  new ArrayList<>();
    ArrayList<Integer> alist =  new ArrayList<>();

    OnlyFleet givenFleet = new OnlyFleet(1, 2, 2, 1);
    integerList.add(1);
    integerList.add(2);
    integerList.add(2);
    integerList.add(1);
    assertEquals(integerList, proxyControllerTest.addIntegers(alist, givenFleet));
  }

  @Test
  public void joinTest() {
    MessageJson testJoin = new MessageJson("join",
        new ObjectMapper().createObjectNode());
    JsonNode jsonArgs = JsonUtils.serializeRecord(testJoin);
    Mocket callingMocket = new Mocket(testLog,
        new ArrayList<>(Arrays.asList(jsonArgs.toString())));
    try {
      ProxyController callingProxy = new ProxyController(callingMocket,
          new BattleSalvoController(new StringReader(""), new StringBuilder()));
      callingProxy.runGame();
    } catch (IOException e) {
      fail();
    }

    assertEquals("{\"method-name\":\"join\",\"arguments\":"
        +
        "{\"name\":\"sandracasval\",\"game-type\":\"SINGLE\"}}", logToString());
  }


  /*
  @Test
  public void endGameTest() {
    EndGameArgs leArgs = new EndGameArgs("WIN", "all ships sunk");
    JsonNode le = JsonUtils.serializeRecord(leArgs);
    MessageJson testendGame = new MessageJson("end-game", le);
    JsonNode jsonArgs = JsonUtils.serializeRecord(testendGame);
    Mocket callingMocket = new Mocket(testLog,
     new ArrayList<>(Arrays.asList(jsonArgs.toString())));
    try {
      ProxyController callingProxy =
      new ProxyController(callingMocket, new BattleSalvoController(new
      StringReader(""), new StringBuilder()));
      callingProxy.runGame();
    } catch (IOException e) {
      fail();
    }
    assertEquals("", logToString());

  }

   */



  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   *
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   *
   * @param <T>      Type to try converting the current test stream to.
   *
   */
  private <T> void responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a GuessJson, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson = new MessageJson(messageName,JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }
}





