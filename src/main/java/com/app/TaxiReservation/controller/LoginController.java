package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.service.UserService;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private DriverService driverService;

    @PostMapping(value = "/user")
    public ResponseUtil createAuthenticationTokenUser(
            @RequestBody LoginInputDto loginInputDto) {

        return new ResponseUtil(200, "success", userService.login(loginInputDto));
    }

    @PostMapping(value = "/driver")
    public ResponseUtil createAuthenticationTokenDriver(
            @RequestBody LoginInputDto loginInputDto) {

        return new ResponseUtil(200, "success", driverService.login(loginInputDto));
    }

}
