package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;
import com.app.TaxiReservation.util.Status.UserStatus;
import com.app.TaxiReservation.util.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DistanceCalculation distanceCalculation;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;


    @Override
    public boolean saveDriver(DriverDto driverDto) {
        try {
            Driver driver = new Driver();
            driver.setName(driverDto.getName());
            driver.setEmail(driverDto.getEmail());
            driver.setMobileNumber(driverDto.getMobileNumber());
            driver.setUserName(driverDto.getUserName());
            driver.setPassword(passwordEncoder.encode(driverDto.getPassword()));
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
                            driver.getUsername(),
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
                .map(driver -> new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUsername(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName()))
                .collect(Collectors.toList());
    }

    @Override
    public LoginOutputDto login(LoginInputDto loginInputDto) {
        Driver driver = driverRepository.findByUserName(loginInputDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        if (!passwordEncoder.matches(loginInputDto.getPassword(), driver.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }


        driver.setLastLogInDate(LocalDateTime.now());
        if (loginInputDto.getLongitude() != null && loginInputDto.getLatitude() != null) {
            // Update driver's location here if needed
            driver.setLongitude(loginInputDto.getLongitude());
            driver.setLatitude(loginInputDto.getLatitude());
        }
        driverRepository.save(driver);

        // Generate JWT token
        String token = jwtService.generateToken(new UserDto(driver.getName(),driver.getUsername()));

        // Prepare driver data for output
        DriverDto driverDto = new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUsername(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName());
        List<DriverDto> driverDtoList = Collections.singletonList(driverDto);

        return new LoginOutputDto(UserStatus.DRIVER.getDisplayName(), driver.getDriverStatus().toString(), driverDtoList, token);
    }
}
