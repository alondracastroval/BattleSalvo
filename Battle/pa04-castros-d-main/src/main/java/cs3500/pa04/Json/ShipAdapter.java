package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa04.Model.Coord;

/**
 * A JSON obect representing a ShipAdapter
 * @param coord the starting coord of the ship
 * @param length the length of the ship
 * @param direction either vertical or horizontal
 */
public record ShipAdapter(


@JsonProperty("coord") JsonNode coord,

@JsonProperty("length") int length,

@JsonProperty("direction") String direction) {

  //we will be adding any desired methods here
}
