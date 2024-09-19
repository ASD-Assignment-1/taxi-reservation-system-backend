package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginUserOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;


public interface  UserService {
    boolean userRegistration(UserDto userDto);
    LoginUserOutputDto login(LoginInputDto loginInputDto);
    boolean rateDriver(RatingDto ratingDto);
}
