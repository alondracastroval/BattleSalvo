package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * represents a JSON for the list of coordinates
 * @param coordinates a list of coordinates
 */
public record CoordinatesJson (
    @JsonProperty ("coordinates") List<JsonNode> coordinates){

}
