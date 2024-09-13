package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.UserService;
import com.app.TaxiReservation.util.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

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


    public boolean login(LoginInputDto loginInputDto) {
        try {
            User byUserName = userRepository.findByUserName(loginInputDto.getUserName());
            if (byUserName.getPassword().equals(loginInputDto.getPassword())) {
                // TODO UPDATE THE LOGIN SESSION TIME
                return true;
            } else {
                throw new UserNotExistException("User not found");
            }

        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }


}
