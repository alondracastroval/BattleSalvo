package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A JSON object representing the arguments for the setup handler in the
 * proxy controller
 * @param width the chosen width by the server
 * @param height the chosen height by the server
 * @param fleeSpec the fleetSpec chosen by the server
 */
public record setupArgs(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") FleetSpec fleeSpec
) {
}
