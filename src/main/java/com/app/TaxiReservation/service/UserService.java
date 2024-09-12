package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.UserDto;

public interface UserService {
    boolean userRegistration(UserDto userDto);
    boolean login(LoginInputDto loginInputDto);
}
