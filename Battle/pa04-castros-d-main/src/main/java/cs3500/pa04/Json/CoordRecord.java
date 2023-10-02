package cs3500.pa04.Json;

//has an x and a y coord

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a coord as  JSON object

 * @param x an x coordinate
 * @param y a y coordinate
 */
public record CoordRecord(
    @JsonProperty("x") int x,
    @JsonProperty("y") int y) {
}
