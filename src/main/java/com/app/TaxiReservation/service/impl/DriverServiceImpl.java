package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.util.DriverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public boolean saveDriver(DriverDto driverDto){
        try {
            Driver driver = new Driver();
            driver.setName(driverDto.getName());
            driver.setEmail(driverDto.getEmail());
            driver.setMobileNumber(driverDto.getMobileNumber());
            driver.setUserName(driverDto.getUserName());
            driver.setPassword(driverDto.getPassword());
            driver.setLicenseNumber(driverDto.getLicenseNumber());
            driver.setProfileImage(driverDto.getProfileImage());
            driver.setDriverStatus(DriverStatus.AV);
            driver.setLastLogInDate(LocalDateTime.now());
            driverRepository.save(driver);
            return true;
        }catch (Exception e){
            throw new SQLException(e.getMessage());
        }
    }

    public boolean login(LoginInputDto loginInputDto) {
        try {
            Driver byUserName = driverRepository.findByUserName(loginInputDto.getUserName());
            if (byUserName.getPassword().equals(loginInputDto.getPassword())) {
                byUserName.setLastLogInDate(LocalDateTime.now());
                byUserName.setLongitude(loginInputDto.getLongitude());
                byUserName.setLatitude(loginInputDto.getLatitude());
                driverRepository.save(byUserName);
                return true;
            } else {
                throw new UserNotExistException("User not found");
            }

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }
}
