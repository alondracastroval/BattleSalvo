package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.Model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * represents the gitHubUserName of somoene
 * @param fleet a hashmap of ShipType to integrer
 */
//FLEET-SPEC
public record gitUserName
    (@JsonProperty("name") Map<ShipType, Integer> fleet) {
}
