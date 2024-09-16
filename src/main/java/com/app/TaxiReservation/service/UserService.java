package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;


public interface UserService {
    boolean userRegistration(UserDto userDto);
    LoginOutputDto login(LoginInputDto loginInputDto);
    boolean rateDriver(RatingDto ratingDto);
}
