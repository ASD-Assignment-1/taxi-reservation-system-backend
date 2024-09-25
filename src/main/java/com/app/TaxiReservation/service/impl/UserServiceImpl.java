package com.app.TaxiReservation.service.impl;


import com.app.TaxiReservation.dto.*;
import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.Rating;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.RatingRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.service.UserService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final double AmountPerKm = 50;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private DistanceCalculation distanceCalculation;

    @Override
    public boolean userRegistration(UserDto userDto) {
        try {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setMobileNumber(userDto.getMobileNumber());
            user.setUserName(userDto.getUserName());
            user.setPassword(userDto.getPassword());
            user.setRole(userDto.getRole());
            user.setUserStatus(UserStatus.USER);
            user.setLastLogInDate(LocalDateTime.now());
            user.setActive(true);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }


    @Override
    public LoginUserOutputDto login(LoginInputDto loginInputDto) {
        try {
            User byUserName = userRepository.findByUserNameAndActiveTrue(loginInputDto.getUserName());
            if (byUserName.getPassword().equals(loginInputDto.getPassword())) {
                byUserName.setLastLogInDate(LocalDateTime.now());
                userRepository.save(byUserName);
                if (byUserName.getUserStatus().equals(UserStatus.USER)) {
                    return new LoginUserOutputDto(new UserDto(
                            byUserName.getId(),
                            byUserName.getName(),
                            byUserName.getEmail(),
                            byUserName.getMobileNumber(),
                            byUserName.getUserName(),
                            byUserName.getRole(),
                            byUserName.getUserStatus()

                    ), driverService.getNearestDrivers(loginInputDto.getLatitude(), loginInputDto.getLongitude()));

                }

                return new LoginUserOutputDto(new UserDto(
                        byUserName.getId(),
                        byUserName.getName(),
                        byUserName.getEmail(),
                        byUserName.getMobileNumber(),
                        byUserName.getUserName(),
                        byUserName.getRole(),
                        byUserName.getUserStatus()
                ), null);


            } else {
                throw new UserNotExistException("User not found");
            }

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean rateDriver(RatingDto ratingDto) {
        try {

            Rating rating = new Rating();
            User user = userRepository.findByIdAndActiveTrue(ratingDto.getUserID())
                    .orElseThrow(() -> new RuntimeException("cannot find user"));
            rating.setUser(user);
            Driver driver = driverRepository.findById(ratingDto.getDriverID())
                    .orElseThrow(() -> new RuntimeException("cannot find the driver"));
            rating.setDriver(driver);
            TaxiReservation taxiReservation = reservationRepository.findById(ratingDto.getReservationID())
                    .orElseThrow(() -> new RuntimeException("cannot find the reservation"));
            rating.setTaxiReservation(taxiReservation);
            rating.setScore(ratingDto.getScore());
            rating.setReview(ratingDto.getReview());
            ratingRepository.save(rating);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserDto> getAllActiveUsers() {
        return userRepository.findAllByUserStatusAndActiveTrue(UserStatus.USER).stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getMobileNumber(),
                        user.getUserName(),
                        user.getRole(),
                        user.getLastLogInDate(),
                        user.getLastLogInDate()
                )).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> search(String userName) {
        try {
            List<User> users = userRepository.findByName(userName);
            return users.stream()
                    .map(user -> new UserDto(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getMobileNumber(),
                            user.getUserName(),
                            user.getRole(),
                            user.getLastLogInDate(),
                            user.getLastLogInDate()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean deleteUser(Integer userID) {
        User user = userRepository.findByIdAndActiveTrue(userID)
                .orElseThrow(() -> new RuntimeException("Cannot find user " + userID));
        user.setActive(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<ReservationAgainstUserDto> getLastReservationWithID(Integer userId) {
        try {
            Pageable pageable = PageRequest.of(0, 5);
            List<TaxiReservation> taxiReservationList = reservationRepository.findLastReservationsByUserId(userId, pageable);

            return taxiReservationList.stream()
                    .map(taxiReservation -> new ReservationAgainstUserDto(
                            taxiReservation.getId(),
                            taxiReservation.getDriver().getName(),
                            taxiReservation.getDriver().getProfileImage(),
                            taxiReservation.getDriver().getId(),
                            taxiReservation.getRating() != null ? new RatingDto(
                                    taxiReservation.getRating().getUser().getId(),
                                    taxiReservation.getRating().getDriver().getId(),
                                    taxiReservation.getRating().getScore(),
                                    null,
                                    taxiReservation.getRating().getReview()
                            ) : null,
                            taxiReservation.getReveredTime(),
                            getDistanceBetweenPoints(taxiReservation.getPickupLatitude(), taxiReservation.getPickupLongitude(), taxiReservation.getDropLatitude(), taxiReservation.getDropLongitude()),
                            taxiReservation.getPickupLatitude(), taxiReservation.getPickupLongitude(), taxiReservation.getDropLatitude(), taxiReservation.getDropLongitude(),
                            taxiReservation.getPaymentAmount(),
                            taxiReservation.getStatus()

                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public double calculateAmountWithDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        try {
            double betweenPoints = getDistanceBetweenPoints(latitude1, longitude1, latitude2, longitude2);
            double fullAmount = Math.round((betweenPoints * AmountPerKm) * 100.00) / 100.00;
            return fullAmount;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private double getDistanceBetweenPoints(double latitude1, double longitude1, double latitude2, double longitude2) {
        DistanceResponseDto roadDistance = distanceCalculation.getRoadDistance(latitude1, longitude1, latitude2, longitude2);
        double distanceKm = roadDistance.getPaths().get(0).getDistance() / 1000.0;
        return distanceKm;
    }

    public List<ReservationAgainstUserDto> getAllReservationWithID(Integer userId) {
        try {
            List<TaxiReservation> taxiReservationList = reservationRepository.findAllReservationsByUserId(userId);
            if (taxiReservationList.isEmpty()) {
                throw new RuntimeException("cannot find the reservation for this user");
            }

            return taxiReservationList.stream()
                    .map(taxiReservation -> new ReservationAgainstUserDto(
                            taxiReservation.getId(),
                            taxiReservation.getDriver().getName(),
                            taxiReservation.getDriver().getProfileImage(),
                            taxiReservation.getDriver().getId(),
                            taxiReservation.getRating() != null ?
                                    new RatingDto(
                                            taxiReservation.getRating().getUser().getId(),
                                            taxiReservation.getRating().getDriver().getId(),
                                            taxiReservation.getRating().getScore(),
                                            null,
                                            taxiReservation.getRating().getReview()
                                    ) : null,
                            taxiReservation.getReveredTime(),
                            getDistanceBetweenPoints(taxiReservation.getPickupLatitude(), taxiReservation.getPickupLongitude(), taxiReservation.getDropLatitude(), taxiReservation.getDropLongitude()),
                            taxiReservation.getPickupLatitude(), taxiReservation.getPickupLongitude(), taxiReservation.getDropLatitude(), taxiReservation.getDropLongitude(),
                            taxiReservation.getPaymentAmount(),
                            taxiReservation.getStatus()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean updateUser(UserDto userDto) {

        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("cannot find user " + userDto.getId()));

        if (userDto.getName() != null && !userDto.getName().trim().isEmpty()) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().trim().isEmpty()) {
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getMobileNumber() != null && !userDto.getMobileNumber().trim().isEmpty()) {
            existingUser.setMobileNumber(userDto.getMobileNumber());
        }

        userRepository.save(existingUser);
        return true;
    }

    @Override
    public boolean changePassword(ChangePasswordDto changePasswordDto) {
        User existingUser = userRepository.findById(changePasswordDto.getId())
                .orElseThrow(() -> new RuntimeException("cannot find user " + changePasswordDto.getId()));

        if (existingUser.getPassword().equals(changePasswordDto.getCurrentPassword())){
            existingUser.setPassword(changePasswordDto.getNewPassword());
            userRepository.save(existingUser);
            return true;
        }else {
            throw new RuntimeException("Current Password is invalid,Please enter your correct password");
        }
    }


}
