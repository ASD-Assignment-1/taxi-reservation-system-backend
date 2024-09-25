package com.app.TaxiReservation.dto;

import com.app.TaxiReservation.util.Status.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAgainstUserDto {
    private Integer id;
    private String driverName;
    private String driverImage;
    private Integer driverId;
    private RatingDto rating;
    private LocalDateTime reveredTime;
    private double distance;
    private double pickupLatitude;
    private double pickupLongitude;
    private double dropLatitude;
    private double dropLongitude;
    private double amount;
    private ReservationStatus status;
}
