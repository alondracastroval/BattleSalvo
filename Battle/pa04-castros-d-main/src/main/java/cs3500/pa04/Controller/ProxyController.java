package cs3500.pa04.Controller;

import static java.lang.System.err;
import static java.lang.System.lineSeparator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.Json.CoordRecord;
import cs3500.pa04.Json.CoordinatesJson;
import cs3500.pa04.Json.Direction;
import cs3500.pa04.Json.EmptyArgs;
import cs3500.pa04.Json.EndGameArgs;
import cs3500.pa04.Json.EndGameJson;
import cs3500.pa04.Json.FleetJson;
import cs3500.pa04.Json.FleetSpec;
import cs3500.pa04.Json.JsonUtils;
import cs3500.pa04.Json.MessageJson;
import cs3500.pa04.Json.OnlyCoordinatesJson;
import cs3500.pa04.Json.OnlyFleet;
import cs3500.pa04.Json.ShipAdapter;
import cs3500.pa04.Json.TakeShotsJson;
import cs3500.pa04.Json.UnserializedShipAdapter;
import cs3500.pa04.Json.joinArgs;
import cs3500.pa04.Json.joinJson;
import cs3500.pa04.Json.setupArgs;
import cs3500.pa04.Json.setupJson;
import cs3500.pa04.Model.AIPlayer;
import cs3500.pa04.Model.BattleGrid;
import cs3500.pa04.Model.BattleSalvoModel;
import cs3500.pa04.Model.Coord;
import cs3500.pa04.Model.GameResult;
import cs3500.pa04.Model.ManualPlayer;
import cs3500.pa04.Model.Ship;
import cs3500.pa04.Model.ShipType;
import cs3500.pa04.View.BattleView;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deals with running a game of BattleSalvo with a server
 */
public class ProxyController implements Controller {
  private AIPlayer player;
  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private final BattleSalvoController controller;
  private final ObjectMapper mapper = new ObjectMapper();

  private static final JsonNode VOID_RESPONSE =
      new ObjectMapper().getNodeFactory().textNode("void");


  /**
   * Used for instansiating the proxy controller in the driver class
   *
   * @param server the socket
   * @param controller the battle salvo controller
   * @throws IOException when the proxyController can't be instansiated
   */
  public ProxyController(Socket server, BattleSalvoController  controller) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.controller = controller;

  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  @Override
  public void runGame() throws IOException {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
      err.println("couldn't connect to the server");
    }

  }


  /**
   * Determines the type of request the server has sent ("guess" or "win") and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) {
    String name = message.methodName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      join(arguments);
    } else if ("setup".equals(name)) {
      setup(arguments);
    } else if ("take-shots".equals(name)) {
      takeShots(arguments);
    } else if ("report-damage".equals(name)) {
      reportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      succesfulHits(arguments);
    } else if ("end-game".equals(name)) {
      endGame(arguments);
    }
  }


  /**
   * Joins the Game of Battle salvo and delivers a join message to the server
   *
   * @param arguments the message given by the server
   */
  private void join(JsonNode arguments) {
    //width and height are just there
    //fleet-spec
    joinJson joinJrgs = this.mapper.convertValue(arguments, joinJson.class);
    //System.out.println(joinJArgs);

    String name = "join";
    joinArgs args = new joinArgs("sandracasval", "SINGLE");
    //turning joinArgs into a Json Node
    JsonNode jsonArgs = JsonUtils.serializeRecord(args);
    MessageJson jsonJoin = new MessageJson(name, jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonJoin);
    this.out.print(jsonResponse);

  }

  /**
   * Creates a setup for the board based on the dimensions provided by the fleet
   *
   * @param arguments the dimensions, and fleet specifications
   */
  private void setup(JsonNode arguments) {

    setupJson getStuff = this.mapper.convertValue(arguments, setupJson.class);
    int height = getStuff.height();
    int width = getStuff.width();
    OnlyFleet leFleet = getStuff.aFleet();

    ArrayList<Integer> integerList = new ArrayList<>();
    integerList = addIntegers(integerList, leFleet);


    //make needed information to instanciate a player
    BattleGrid aiGrid = new BattleGrid(height, width);
    BattleGrid serverGrid = new BattleGrid(height, width);
    aiGrid.makeUserGrid();
    serverGrid.makeUserGrid();
    String[][] aiPlayerGrid = aiGrid.getGrid();
    String[][] serverPlayerGrid = serverGrid.getGrid();
    BattleSalvoModel bms = new BattleSalvoModel(serverGrid, aiGrid);

    Map<ShipType, Integer> neededHash = controller.initFleetHashMap(integerList);
    player = new AIPlayer("AI", neededHash, bms, new ArrayList<Coord>(),
        new ArrayList<Coord>(), new ArrayList<Coord>());
    ManualPlayer serverPlayer = new ManualPlayer("server", neededHash, bms,
        new ArrayList<Coord>(), new ArrayList<Coord>(), new ArrayList<Coord>());

    controller.aiPlayer = player;
    controller.manPlayer = serverPlayer;

    //now we add the players to the model
    bms.setP1(serverPlayer);
    bms.setP2(player);
    //also calling setup on the serverPlayer
    serverPlayer.setup(height, width, neededHash);

    List<Ship> ogNonJsonShipList = controller.aiPlayer.setup(height, width, neededHash);

    BattleView viewInstance = new BattleView(controller.input, controller.output);
    viewInstance.drawUpdatedAIData(aiGrid.getGrid());


    //NOW WE NEED TO CREATE A LIST<SHIPADAPTER> FROM THE LIST<SHIP> we do so here:
    List<UnserializedShipAdapter> listToBeReturned = Ship.convertShipToAdapter(ogNonJsonShipList);
    //NOW JUST SERIALIZE IT
  //  List<ShipAdapter> serializedList = serializeCoords(listToBeReturned);
    List<JsonNode> endList = serializeList(listToBeReturned);
    FleetJson fleetUpdated = new FleetJson(endList);
    //serialize it into a json node
    JsonNode jsonArgs = JsonUtils.serializeRecord(fleetUpdated);
    MessageJson jsonSetup = new MessageJson("setup", jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonSetup);
    this.out.println(jsonResponse);

  }


  /**
   * Serializes a list of UnserializedShipAdapter into a list of JsonNode
   * @param listOfShip the list to be serialized
   * @return the list that has been serialized
   */
  public List<JsonNode> serializeList(List<UnserializedShipAdapter> listOfShip){
    List<JsonNode> endList = new ArrayList<JsonNode>();
    for(UnserializedShipAdapter s: listOfShip) {
      JsonNode madeNode = JsonUtils.serializeRecord(s);
      endList.add(madeNode);
    }
    return endList;
  }

  /**
   * Creates a list of integers based on the fleet
   * @param integerList the list of integers to be added to
   * @param fleet the game's fleet
   * @return the final list of integers
   */
  public ArrayList<Integer> addIntegers(ArrayList<Integer> integerList, OnlyFleet fleet) {
    //integerList = new ArrayList<>();
    integerList.add(fleet.carrierNum());
    integerList.add(fleet.battleShipNum());
    integerList.add(fleet.destroyerNum());
    integerList.add(fleet.submarineNum());

    return integerList;

  }


  /**
   * Creates a list of valid shot that a player takes during a game of battle salvo
   * @param arguments the message from the server to be parsed
   */
  private void takeShots(JsonNode arguments) {

    String name = "take-shots";
    //call the takeShots method for the ai (depending on the number of ships)
    //return a list of volleys,coords
    controller.aiPlayer = player;
    //let the player take the shots and get the list of those coords
    List<Coord> aIPlayerShots = player.takeShots();
    //now we convert that list into a list of CoordRecord
    List<CoordRecord> aIShotsUpdated = convertToCoordRecord(aIPlayerShots);
    //now let's make that list a list of JsonNode
    List<JsonNode> serializedCoords = serializeCoordRecordList(aIShotsUpdated);
    CoordinatesJson coordinatesJson = new CoordinatesJson(serializedCoords);

    //making it one JsonNode
    JsonNode jsonArgs = JsonUtils.serializeRecord(coordinatesJson);
    MessageJson jsonShots = new MessageJson(name, jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonShots);
    this.out.println(jsonResponse);


  }

  /**
   * Converts a list of Coord into a list of CoordRecord
   * @param shots the list to be parsed
   * @return the parsed list
   */
  public List<CoordRecord> convertToCoordRecord(List<Coord> shots){
    List<CoordRecord> convertedCoords = new ArrayList<CoordRecord>();
    for(Coord c: shots) {
      Coord currentCoord = c;
      int x  = c.getX();
      int y = c.getY();
      CoordRecord convertedCoord = new CoordRecord(x, y);
      convertedCoords.add(convertedCoord);
    }
    return convertedCoords;
  }

  /**
   * Serializes a list of CoordRecord into a list of JsonNode
   * @param listOfCoord the list to be serialized
   * @return the serialized list
   */
  public List<JsonNode> serializeCoordRecordList(List<CoordRecord> listOfCoord){
    List<JsonNode> endList = new ArrayList<JsonNode>();
    for(CoordRecord c: listOfCoord) {
      JsonNode madeNode = JsonUtils.serializeRecord(c);
      endList.add(madeNode);
    }
    return endList;
  }


  /**
   * Reports the damage a player has recieved during the round
   * @param arguments the message from the server
   */
  private void reportDamage(JsonNode arguments) {
    //call reportDamage on the player and return it back as a serialized list of Json
    String name = "report-damage";
    controller.aiPlayer = player;

    //get the opponent shots on board
    OnlyCoordinatesJson getStuff = this.mapper.convertValue(arguments, OnlyCoordinatesJson.class);
   List<CoordRecord> coordRecordList = getStuff.coordinates();
   //convert that list to a list of coords and pass it in to the player
    ArrayList<Coord> opponentShots = coordRecordtoCoord(coordRecordList);

    List<Coord> playerDamage = player.reportDamage(opponentShots);
    //now we convert that damage into a list of coordRecord
    List<CoordRecord> playerDamageUpdated = convertToCoordRecord(playerDamage);
    //now let's make that list a list of JsonNode
    List<JsonNode> serializedDamage = serializeCoordRecordList(playerDamageUpdated);
    CoordinatesJson officialCoords = new CoordinatesJson(serializedDamage);


    //making the list one JsonNode
    JsonNode jsonArgs = JsonUtils.serializeRecord(officialCoords);
    MessageJson jsonDamage = new MessageJson("report-damage", jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonDamage);
    this.out.println(jsonResponse);

  }

  /**
   * Converts a list of coordRecord to a Coord
   * @param coordRecordList the list to be converted
   * @return the converted list
   */
  private ArrayList<Coord> coordRecordtoCoord(List<CoordRecord> coordRecordList) {
    ArrayList<Coord> endList = new ArrayList<Coord>();
    for(CoordRecord c: coordRecordList){
      int x = c.x();
      int y = c.y();
      Coord createdCoord = new Coord(x, y);
      endList.add(createdCoord);
    }
    return endList;
  }

  /**
   * Returns the amount of successful hits a player has hit
   * @param arguments the message from the server
   */
  private void succesfulHits(JsonNode arguments) {


    String name = "succesful-hits";
    //get the player
    controller.aiPlayer = player;
    List<Coord> listOfSuccessfulShots = player.goodShots;
    //getting the coordinates (so our shots that hit the other Ship)
    OnlyCoordinatesJson getStuff = this.mapper.convertValue(arguments, OnlyCoordinatesJson.class);
    //your succesful hits
    List<CoordRecord> coordRecordList = getStuff.coordinates();
    //convert that list to a list of coords and pass it in to the player
    ArrayList<Coord> succesfulHits = coordRecordtoCoord(coordRecordList);
    //maybe take this out if it funks up the code
    player.goodShots = succesfulHits;

    EmptyArgs leEmpty = new EmptyArgs();
    //create the empty message
    JsonNode jsonArgs = JsonUtils.serializeRecord(leEmpty);
    MessageJson jsonSuccesfulHits = new MessageJson(name, jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonSuccesfulHits);
    this.out.println(jsonResponse);


  }

  /**
   * Handles the ending of a battle salvo game
   * @param arguments the message from the server
   */
  private void endGame(JsonNode arguments) {
    String name = "end-game";
    EndGameArgs getStuff = this.mapper.convertValue(arguments, EndGameArgs.class);
    String result = getStuff.result();
    String reason = getStuff.reason();

    GameResult resultToEnum = turnIntoEnum(result);
    controller.aiPlayer = player;

    player.endGame(resultToEnum, reason);

    EmptyArgs leEmpty = new EmptyArgs();
    //create the empty message
    JsonNode jsonArgs = JsonUtils.serializeRecord(leEmpty);
    MessageJson jsonEndGame = new MessageJson(name, jsonArgs);
    JsonNode jsonResponse = JsonUtils.serializeRecord(jsonEndGame);
    this.out.println(jsonResponse);

   // System.out.println("congratafuckalations");



  }

  /**
   * Turns the string from the server message into the corresponding enum
   * @param result the string to be converted to enum
   * @return the enum result
   */

  private GameResult turnIntoEnum(String result) {
    GameResult oneEnum = GameResult.WIN;
    if (result.equals("WIN")) {
      oneEnum = GameResult.WIN;

    } else if (result.equals("LOSE")) {
      oneEnum = GameResult.LOSE;
    } else if (result.equals("DRAW")) {
      oneEnum = GameResult.DRAW;
    }
    return oneEnum;
  }

}


