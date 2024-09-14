package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Integer userId;
    private String driverUserName;
    private double pickupLatitude;
    private double pickupLongitude;
    private double dropLatitude;
    private double dropLongitude;

}
