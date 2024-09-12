package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;

public interface DriverService {
    boolean saveDriver(DriverDto driverDto);
    boolean login(LoginInputDto loginInputDto);
}
