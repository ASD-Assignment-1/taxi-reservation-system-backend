package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private String name;
    private String email;
    private String mobileNumber;
    private String userName;
    private String password;
    private String licenseNumber;
    private String profileImage;
    private String status;

    public DriverDto(String name, String email, String mobileNumber, String userName, String licenseNumber, String profileImage) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
        this.profileImage = profileImage;
    }

    public DriverDto(String name, String email, String mobileNumber, String userName, String licenseNumber) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
    }

    public DriverDto(String name, String email, String mobileNumber, String userName, String licenseNumber, String profileImage, String status) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
        this.profileImage = profileImage;
        this.status = status;
    }
}
