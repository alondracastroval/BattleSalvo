package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a JSON object to represent a JoinJson
 * @param name the name of the method
 * @param gameType the nameType either SINGLE or MULTI
 */
public record joinJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") String gameType
) {
}

