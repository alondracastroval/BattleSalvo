package cs3500.pa04.Json;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @param githubUser the github username of someone
 * @param gameType either Multi or single player
 */
public record joinArgs(
    @JsonProperty("name") String githubUser,
    @JsonProperty("game-type") String gameType
) {
}