package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a JSON message
 * @param methodName the name of the method
 * @param arguments all of the arguments provided by the server (can be nested)
 */
public record MessageJson(
    //the thing that we are expecting right here

    //use an enum to limit the messagenames that we are allowed to use

    @JsonProperty("method-name") String methodName,

    @JsonProperty("arguments") JsonNode arguments) {
}
