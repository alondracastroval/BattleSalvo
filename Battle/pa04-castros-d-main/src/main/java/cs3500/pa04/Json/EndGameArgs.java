package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A jSON Object representing the end of a hame
 * @param result 
 * @param reason
 */
public record EndGameArgs(
    @JsonProperty("result") String result,
    @JsonProperty("reason") String reason
) {
}
