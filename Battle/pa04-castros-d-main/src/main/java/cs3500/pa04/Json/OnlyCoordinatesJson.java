package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * A JSON object representing a list of coordinateRecords
 * @param coordinates
 */
public record OnlyCoordinatesJson (
    @JsonProperty("coordinates") List<CoordRecord> coordinates){

}
