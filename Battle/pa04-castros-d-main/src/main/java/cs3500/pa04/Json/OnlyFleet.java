package cs3500.pa04.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * a record representing a fleet and its speciic number
 * @param carrierNum the number of carrier ships
 * @param battleShipNum the number of battleships
 * @param destroyerNum the number of destroyer ships
 * @param submarineNum the number of submarine ships
 */
public record OnlyFleet(
    @JsonProperty("CARRIER") int carrierNum,
    @JsonProperty("BATTLESHIP") int battleShipNum,
    @JsonProperty("DESTROYER") int destroyerNum,
    @JsonProperty("SUBMARINE") int submarineNum

) {
}
