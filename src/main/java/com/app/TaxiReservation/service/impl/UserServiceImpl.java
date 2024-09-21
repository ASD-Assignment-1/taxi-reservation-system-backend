package com.app.TaxiReservation.service.impl;


import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginUserOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.Rating;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.RatingRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.service.UserService;
import com.app.TaxiReservation.util.Status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RatingRepository ratingRepository;

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
                            byUserName.getRole()
                    ), driverService.getNearestDrivers(loginInputDto.getLatitude(), loginInputDto.getLongitude()));

                }

                return new LoginUserOutputDto(new UserDto(
                        byUserName.getId(),
                        byUserName.getName(),
                        byUserName.getEmail(),
                        byUserName.getMobileNumber(),
                        byUserName.getUserName(),
                        byUserName.getRole()
                ), null);


            } else {
                throw new UserNotExistException("User not found");
            }

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Override
    public boolean rateDriver(RatingDto ratingDto){
        try {

            Rating rating = new Rating();
            rating.setUser(userRepository.findByIdAndActiveTrue(ratingDto.getUserID()).get());
            rating.setDriver(driverRepository.findById(ratingDto.getDriverID()).get());
            rating.setScore(ratingDto.getScore());
            rating.setReview(ratingDto.getReview());
            ratingRepository.save(rating);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserDto> getAllActiveUsers(){
        return userRepository.findAllByActiveTrue().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getMobileNumber(),
                        user.getUserName(),
                        user.getRole()
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
                            user.getRole()))
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}
