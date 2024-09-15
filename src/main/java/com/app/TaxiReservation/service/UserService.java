package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;
import com.app.TaxiReservation.dto.UserDto;

import java.util.List;

public interface UserService {
    boolean userRegistration(UserDto userDto);
    LoginOutputDto login(LoginInputDto loginInputDto);
}
