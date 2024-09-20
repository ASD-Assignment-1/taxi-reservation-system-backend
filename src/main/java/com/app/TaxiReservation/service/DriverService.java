package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;

import java.util.List;

public interface DriverService {
    boolean saveDriver(DriverDto driverDto);
    DriverDto login(LoginInputDto loginInputDto);
    List<DriverDto> getNearestDrivers(double userLatitude, double userLongitude);
    List<DriverDto> getAllDrivers(String driverStatus);
    DriverDto getDriverById(Integer driverID);
}
