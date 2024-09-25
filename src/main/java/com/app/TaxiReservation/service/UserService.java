package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.*;

import java.util.List;


public interface UserService {
    boolean userRegistration(UserDto userDto);

    LoginUserOutputDto login(LoginInputDto loginInputDto);

    boolean rateDriver(RatingDto ratingDto);

    List<UserDto> getAllActiveUsers();

    List<UserDto> search(String userName);

    boolean deleteUser(Integer userID);

    List<ReservationAgainstUserDto> getLastReservationWithID(Integer userId);

    double calculateAmountWithDistance(double latitude1, double longitude1, double latitude2, double longitude2);

    List<ReservationAgainstUserDto> getAllReservationWithID(Integer userId);

    boolean updateUser(UserDto userDto);

    boolean changePassword(ChangePasswordDto changePasswordDto);
}
