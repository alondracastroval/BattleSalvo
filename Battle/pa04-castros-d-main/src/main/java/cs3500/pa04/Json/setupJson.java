package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The arguments needed for the setupJson Handler
 * @param width the chosen width by the server
 * @param height the chosen height by the server
 * @param aFleet the fleet specifications given by the server
 */
public record setupJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") OnlyFleet aFleet
) {
}
