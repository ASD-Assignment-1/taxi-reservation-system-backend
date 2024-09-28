package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginInputDto {
    private String userName;
    private String password;
    private Double longitude;
    private Double latitude;
}
