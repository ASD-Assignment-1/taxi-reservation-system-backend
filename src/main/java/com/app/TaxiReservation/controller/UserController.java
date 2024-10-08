package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.service.UserService;
import com.app.TaxiReservation.util.ResponseUtil;
import com.app.TaxiReservation.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ReservationService reservationService;

    @GetMapping("/roles")
    public ResponseUtil getAllRoles() {
        return new ResponseUtil(200, "success", Arrays.stream(Role.values()).collect(Collectors.toMap(Role::name, Role::getDisplayName)));
    }

    @PostMapping("/register")
    public ResponseUtil registerUser(@RequestBody UserDto userDto) {
        return new ResponseUtil(200, "success", userService.userRegistration(userDto));
    }

    /*@PostMapping("/login")
    public ResponseUtil login(@RequestBody LoginInputDto loginInputDto) {
        return new ResponseUtil(200, "success", userService.login(loginInputDto));
    }*/

    @GetMapping("/pay")
    public ResponseUtil makePayment(@RequestParam Integer reservationId) {
        reservationService.makePayments(reservationId);
        return new ResponseUtil(200, "success","Payment success");
    }

    @PostMapping("/rate")
    public ResponseUtil rateDriver(@RequestBody RatingDto ratingDto) {
        return new ResponseUtil(200, "success", userService.rateDriver(ratingDto));
    }

}
