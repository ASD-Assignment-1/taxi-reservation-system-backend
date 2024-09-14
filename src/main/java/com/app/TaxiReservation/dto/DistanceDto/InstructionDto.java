package com.app.TaxiReservation.dto.DistanceDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstructionDto {
    private int exit_number;
    private double distance;
    private int sign;
    private boolean exited;
    private double turn_angle;
    private int[] interval;
    private String text;
    private long time;
    private String street_name;
}
