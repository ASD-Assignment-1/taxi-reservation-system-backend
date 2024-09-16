package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutputDto {
    private String role;
    private String status;
    private List<DriverDto> driverDtos;
    private String token;
}
