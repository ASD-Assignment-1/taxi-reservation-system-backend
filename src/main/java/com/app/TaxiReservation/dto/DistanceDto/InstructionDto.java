package com.app.TaxiReservation.dto.DistanceDto;

import lombok.Data;

@Data
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
