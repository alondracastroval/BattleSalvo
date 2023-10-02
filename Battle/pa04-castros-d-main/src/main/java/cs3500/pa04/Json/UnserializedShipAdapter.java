package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A JSON object representing an unserialized ship Adapter
 * @param coord the beginning coord
 * @param length the length of the ship
 * @param direction the direction of the ship
 */
public record UnserializedShipAdapter (
  @JsonProperty("coord") CoordRecord coord,

  @JsonProperty("length") int length,

  @JsonProperty("direction") Direction direction) {



}
