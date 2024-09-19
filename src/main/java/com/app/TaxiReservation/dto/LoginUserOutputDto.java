package com.app.TaxiReservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserOutputDto {
    private UserDto userDto;
    private List<DriverDto> driverDtos;
}
