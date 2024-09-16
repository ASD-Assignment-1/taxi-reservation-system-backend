package com.app.TaxiReservation.service.impl;


import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;
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
import com.app.TaxiReservation.util.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;

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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;


    @Override
    public boolean userRegistration(UserDto userDto) {
        try {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setMobileNumber(userDto.getMobileNumber());
            user.setUserName(userDto.getUserName());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
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
    public boolean rateDriver(RatingDto ratingDto){
        try {

            Rating rating = new Rating();
            rating.setUser(userRepository.findById(ratingDto.getUserID()).get());
            rating.setDriver(driverRepository.findById(ratingDto.getDriverID()).get());
            rating.setScore(ratingDto.getScore());
            rating.setReview(ratingDto.getReview());
            ratingRepository.save(rating);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LoginOutputDto login(LoginInputDto loginInputDto) {
        User user = userRepository.findByUserName(loginInputDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        /*UserDetails userDetails = userRepository.findByUserName(loginInputDto.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));*/

        if (!passwordEncoder.matches(loginInputDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect username or password");
        }

        user.setLastLogInDate(LocalDateTime.now());
        userRepository.save(user);

        String jwt = jwtService.generateToken(new UserDto(user.getUsername(), user.getUsername()));

        if (user.getUserStatus().equals(UserStatus.USER)) {
            return new LoginOutputDto(user.getRole().getDisplayName(), user.getUserStatus().getDisplayName() ,driverService.getNearestDrivers(loginInputDto.getLatitude(), loginInputDto.getLongitude()), jwt);
        } else {
            return new LoginOutputDto(UserStatus.ADMIN.getDisplayName(), user.getUserStatus().getDisplayName(), null, jwt);
        }
    }


}
