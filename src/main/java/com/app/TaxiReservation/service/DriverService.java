package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;

import java.util.List;

public interface DriverService {
    boolean saveDriver(DriverDto driverDto);
    LoginOutputDto login(LoginInputDto loginInputDto);
    List<DriverDto> getNearestDrivers(double userLatitude, double userLongitude);
    List<DriverDto> getAllDrivers(String driverStatus);
}
