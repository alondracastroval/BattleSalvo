package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa04.Model.Ship;
import java.util.List;

/**
 * represents a list of JsonNode Ships
 * @param fleet a list of JsonNode which should be a ship
 */
public record FleetJson(
    //fleet is an array of ships
 @JsonProperty("fleet") List<JsonNode> fleet) {

}
