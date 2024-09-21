package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.ReservationDetailsDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JavaMailSender javaMailSender;

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
            Driver createdDriver = driverRepository.save(driver);
            sendDriverDetails(createdDriver);
            return true;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    private void sendDriverDetails(Driver driver) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(driver.getEmail());
            message.setSubject("Driver Details");

            String emailBody = "Driver Details:\n" +
                    "Name: " + driver.getName() + "\n" +
                    "Email: " + driver.getEmail() + "\n" +
                    "Mobile Number: " + driver.getMobileNumber() + "\n" +
                    "Username: " + driver.getUserName() + "\n" +
                    "password: " + driver.getPassword() + "\n" +
                    "License Number: " + driver.getLicenseNumber();

            message.setText(emailBody);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DriverDto login(LoginInputDto loginInputDto) {
        try {
            Driver byUserName = driverRepository.findByUserNameAndActiveTrue(loginInputDto.getUserName());
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
                        byUserName.getDriverStatus().getDisplayName(),
                        byUserName.getLastLogInDate(),
                        byUserName.getLastLogOutDate()
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
    public List<DriverDto> getAllDrivers(String driverStatus) {
        // Convert the display name to the corresponding DriverStatus enum
        DriverStatus status;
        if (driverStatus.equalsIgnoreCase("All")) {
            status = null;
        } else {
            status = Arrays.stream(DriverStatus.values())
                    .filter(s -> s.getDisplayName().equalsIgnoreCase(driverStatus))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Driver Status: " + driverStatus));
        }


        List<Driver> all = driverRepository.findAllByDriverStatus(status);
        return all.stream()
                .map(driver -> new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUserName(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName(), driver.getLastLogInDate(), driver.getLastLogOutDate()))
                .collect(Collectors.toList());
    }

    @Override
    public DriverDto getDriverById(Integer driverID) {
        Driver driver = driverRepository.findByIdAndActive(driverID, true)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found with ID: " + driverID));
        return new DriverDto(driver.getId(), driver.getName(), driver.getEmail(), driver.getMobileNumber(), driver.getUserName(), driver.getLicenseNumber(), driver.getProfileImage(), driver.getDriverStatus().getDisplayName(), driver.getLastLogInDate(), driver.getLastLogOutDate());
    }

    @Override
    public List<DriverDto> search(String userName) {
        List<Driver> drivers = driverRepository.findByName(userName);
        return drivers.stream()
                .map(driver -> new DriverDto(
                        driver.getId(),
                        driver.getName(),
                        driver.getEmail(),
                        driver.getMobileNumber(),
                        driver.getUserName(),
                        driver.getLicenseNumber(),
                        driver.getProfileImage(),
                        driver.getDriverStatus().getDisplayName(),
                        driver.getLastLogInDate(),
                        driver.getLastLogOutDate()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteDriver(Integer driverID) {
        try {
            driverRepository.deactivateDriver(driverID);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ReservationDetailsDto> getLastReservationWithID(Integer driverId) {
        Pageable pageable = PageRequest.of(0, 5);
        List<TaxiReservation> taxiReservationList = reservationRepository.findLastReservationsByDriverId(driverId, pageable);
        return taxiReservationList.stream()
                .map(taxiReservation -> new ReservationDetailsDto(
                        taxiReservation.getId(),
                        new UserDto(
                                taxiReservation.getUser().getId(),
                                taxiReservation.getUser().getName(),
                                taxiReservation.getUser().getEmail(),
                                taxiReservation.getUser().getMobileNumber(),
                                taxiReservation.getUser().getUserName(),
                                taxiReservation.getUser().getRole()
                        ),
                        taxiReservation.getReveredTime(),
                        taxiReservation.getPaymentAmount(),
                        taxiReservation.getPickupLatitude(),
                        taxiReservation.getPickupLongitude(),
                        taxiReservation.getDropLatitude(),
                        taxiReservation.getDropLongitude(),
                        taxiReservation.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean updateDriver(DriverDto driverDto) {
        Driver existingDriver = driverRepository.findById(driverDto.getId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Update non-null fields
        if (driverDto.getName() != null && !driverDto.getName().trim().isEmpty()) {
            existingDriver.setName(driverDto.getName());
        }
        if (driverDto.getEmail() != null && !driverDto.getEmail().trim().isEmpty()) {
            existingDriver.setEmail(driverDto.getEmail());
        }
        if (driverDto.getMobileNumber() != null && !driverDto.getMobileNumber().trim().isEmpty()) {
            existingDriver.setMobileNumber(driverDto.getMobileNumber());
        }
        if (driverDto.getLicenseNumber() != null && !driverDto.getLicenseNumber().trim().isEmpty()) {
            existingDriver.setLicenseNumber(driverDto.getLicenseNumber());
        }
        if (driverDto.getProfileImage() != null && !driverDto.getProfileImage().trim().isEmpty()) {
            existingDriver.setProfileImage(driverDto.getProfileImage());
        }
        if (driverDto.getStatus() != null && !driverDto.getStatus().trim().isEmpty()) {
            String statusString = driverDto.getStatus().trim();
            try {
                existingDriver.setDriverStatus(DriverStatus.fromDisplayName(statusString));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (driverDto.getLastLogInDate() != null) {
            existingDriver.setLastLogInDate(driverDto.getLastLogInDate());
        }
        if (driverDto.getLastLogOutDate() != null) {
            existingDriver.setLastLogOutDate(driverDto.getLastLogOutDate());
        }

        driverRepository.save(existingDriver);

        return true;
    }


}
