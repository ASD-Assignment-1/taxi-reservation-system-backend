package com.app.TaxiReservation.service;


import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;


public interface UserService {
    boolean userRegistration(UserDto userDto);
    boolean rateDriver(RatingDto ratingDto);
    LoginOutputDto login(LoginInputDto loginInputDto);
}
