package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    DriverService driverService;

    @PostMapping("/register")
    public ResponseUtil registerUser(@RequestBody DriverDto driverDto) {
        return new ResponseUtil(200, "success", driverService.saveDriver(driverDto));
    }

    @GetMapping("/drivers")
    public ResponseUtil getAllDrivers() {
        return new ResponseUtil(200, "success", driverService.getAllDrivers());
    }

}
