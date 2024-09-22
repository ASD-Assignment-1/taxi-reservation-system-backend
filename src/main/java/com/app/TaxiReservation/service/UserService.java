package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginUserOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.ReservationAgainstUserDto;
import com.app.TaxiReservation.dto.ReservationDetailsDto;
import com.app.TaxiReservation.dto.UserDto;

import java.util.List;


public interface UserService {
    boolean userRegistration(UserDto userDto);

    LoginUserOutputDto login(LoginInputDto loginInputDto);

    boolean rateDriver(RatingDto ratingDto);

    List<UserDto> getAllActiveUsers();

    List<UserDto> search(String userName);

    boolean deleteUser(Integer userID);

    List<ReservationAgainstUserDto> getLastReservationWithID(Integer userId);
}
