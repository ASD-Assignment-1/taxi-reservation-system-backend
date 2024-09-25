package com.app.TaxiReservation.dto;

import com.app.TaxiReservation.entity.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private Integer id;
    private String name;
    private String email;
    private String mobileNumber;
    private String userName;
    private String password;
    private String licenseNumber;
    private String profileImage;
    private String status;
    private LocalDateTime lastLogInDate;
    private LocalDateTime lastLogOutDate;
    private Double averageScore;

    public DriverDto(String name, String email, String mobileNumber, String userName, String licenseNumber, String profileImage,Double score) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
        this.profileImage = profileImage;
        this.averageScore = score;
    }

    public DriverDto(String name, String email, String mobileNumber, String userName, String licenseNumber) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
    }


    public DriverDto(Integer id, String name, String email, String mobileNumber, String userName, String licenseNumber, String profileImage, String status, LocalDateTime lastLogInDate, LocalDateTime lastLogOutDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.licenseNumber = licenseNumber;
        this.profileImage = profileImage;
        this.status = status;
        this.lastLogInDate = lastLogInDate;
        this.lastLogOutDate = lastLogOutDate;
    }
}
