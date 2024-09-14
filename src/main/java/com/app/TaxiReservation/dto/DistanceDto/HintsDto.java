package com.app.TaxiReservation.dto.DistanceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HintsDto {
    @JsonProperty("visited_nodes.sum")
    private Integer visited_nodes_sum;
    @JsonProperty("visited_nodes.average")
    private Double visited_nodes_average;
}
