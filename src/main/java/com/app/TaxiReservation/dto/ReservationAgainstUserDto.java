package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAgainstUserDto {
    private String driverName;
    private RatingDto rating;
    private LocalDateTime reveredTime;
    private double distance;
    private double amount;
}
