package com.app.TaxiReservation.dto;

import com.app.TaxiReservation.util.Status.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDetailsDto {
    private Integer id;
    private UserDto userDto;
    private DriverDto driverDto;
    private LocalDateTime reveredTime;
    private double paymentAmount;
    private double pickupLatitude;
    private double pickupLongitude;
    private double dropLatitude;
    private double dropLongitude;
    private ReservationStatus status;
}