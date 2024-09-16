package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    private DriverRepository driverRepository;

    @Autowired
    public CustomUserDetailsServiceImpl(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check if it's a user
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }

        // Check if it's a driver
        Optional<Driver> driverOptional = driverRepository.findByUserName(username);
        if (driverOptional.isPresent()) {
            return driverOptional.get();
        }

        throw new UsernameNotFoundException("User/Driver not found with username: " + username);
    }
}
