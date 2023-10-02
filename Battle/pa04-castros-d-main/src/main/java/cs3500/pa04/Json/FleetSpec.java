package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * a Hashmap of shipAdapter with its specicified amount
 * @param fleetSpec a hash map of shipAdapter with its respective integer
 */
public record FleetSpec(
    @JsonProperty("fleet-spec") Map<ShipAdapter, Integer> fleetSpec
) {
}
