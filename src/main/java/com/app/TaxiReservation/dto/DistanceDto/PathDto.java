package com.app.TaxiReservation.dto.DistanceDto;

import lombok.Data;

import java.util.List;

@Data
public class PathDto {
    private double distance;
    private double weight;
    private long time;
    private int transfers;
    private boolean points_encoded;
    private double points_encoded_multiplier;
    private double[] bbox;
    private String points;
    private List<InstructionDto> instructions;
    private List<LegDto> legs;
    private double ascend;
    private double descend;
    private String snapped_waypoints;
}
