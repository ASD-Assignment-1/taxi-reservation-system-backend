package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DistanceCalculation distanceCalculation;

    @Override
    public boolean saveDriver(DriverDto driverDto) {
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
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public DriverDto login(LoginInputDto loginInputDto) {
        try {
            Driver byUserName = driverRepository.findByUserName(loginInputDto.getUserName());
            if (byUserName.getPassword().equals(loginInputDto.getPassword())) {
                byUserName.setLastLogInDate(LocalDateTime.now());
                byUserName.setLongitude(loginInputDto.getLongitude());
                byUserName.setLatitude(loginInputDto.getLatitude());
                driverRepository.save(byUserName);
                DriverDto driverDto = new DriverDto(
                        byUserName.getId(),
                        byUserName.getName(),
                        byUserName.getEmail(),
                        byUserName.getMobileNumber(),
                        byUserName.getUserName(),
                        byUserName.getLicenseNumber(),
                        byUserName.getProfileImage(),
                        byUserName.getDriverStatus().getDisplayName()
                );
                return driverDto;
            } else {
                throw new UserNotExistException("User not found");
            }

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public List<DriverDto> getNearestDrivers(double userLatitude, double userLongitude) {
        List<Driver> allDrivers = driverRepository.findAllByDriverStatus(DriverStatus.AV);
        List<DriverDto> nearbyDrivers = new ArrayList<>();

        for (Driver driver : allDrivers) {
            if (driver.getLongitude() != null && driver.getLatitude() != null) {
                // Get road distance using GraphHopper
                DistanceResponseDto roadDistance = distanceCalculation.getRoadDistance(userLatitude, userLongitude,
                        driver.getLatitude(), driver.getLongitude());

                double distanceKm = roadDistance.getPaths().get(0).getDistance() / 1000.0;
                // filtering the drivers within 2 km
                if (distanceKm <= 2) {
                    nearbyDrivers.add(new DriverDto(driver.getName(),
                            driver.getEmail(),
                            driver.getMobileNumber(),
                            driver.getUserName(),
                            driver.getLicenseNumber(),
                            driver.getProfileImage()));
                }
            }
        }
        return nearbyDrivers;
    }

    @Override
    public List<DriverDto> getAllDrivers(String driverStatus){
        // Convert the display name to the corresponding DriverStatus enum
        DriverStatus status = Arrays.stream(DriverStatus.values())
                .filter(s -> s.getDisplayName().equalsIgnoreCase(driverStatus))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Driver Status: " + driverStatus));

        List<Driver> all = driverRepository.findAllByDriverStatus(status);
        return all.stream()
                .map(driver -> new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUserName(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName()))
                .collect(Collectors.toList());
    }

    public DriverDto getDriverById(Integer driverID){
        Driver driver = driverRepository.findById(driverID)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverID));
        return new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUserName(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName());
    }
}
