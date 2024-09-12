package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private String Name;
    private String email;
    private String mobileNumber;
    private String userName;
    private String password;
    private String licenseNumber;
    private String profileImage;
}
