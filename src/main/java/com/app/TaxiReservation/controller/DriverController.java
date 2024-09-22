package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/driver")
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/login")
    public ResponseUtil login(@RequestBody LoginInputDto loginInputDto) {
        return new ResponseUtil(200, "success", driverService.login(loginInputDto));
    }

    @GetMapping()
    public ResponseUtil getById(@RequestParam Integer id) {
        return new ResponseUtil(200, "success", driverService.getDriverById(id));
    }

    @GetMapping("/search")
    public ResponseUtil getById(@RequestParam String name) {
        return new ResponseUtil(200, "success", driverService.search(name));
    }

    @DeleteMapping()
    public ResponseUtil deleteDriver(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", driverService.deleteDriver(driverID));
    }

    @GetMapping("/nearestDrivers")
    public ResponseUtil deleteDriver(@RequestParam double userLatitude, @RequestParam double userLongitude) {
        return new ResponseUtil(200, "success", driverService.getNearestDrivers(userLatitude, userLongitude));
    }

    @GetMapping("/reservation")
    public ResponseUtil getLatestReservation(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", driverService.getLastReservationWithID(driverID));
    }

    @PostMapping("/update")
    public ResponseUtil login(@RequestBody DriverDto driverDto) {
        return new ResponseUtil(200, "success", driverService.updateDriver(driverDto));
    }

    @PostMapping("/updateStatus")
    public ResponseUtil login(@RequestParam Integer driverID, @RequestParam String status) {
        return new ResponseUtil(200, "success", driverService.changeDriverStatus(driverID, status));
    }

    @GetMapping("/dailyIncome")
    public ResponseUtil getDailyTotal(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", reservationService.getDailyTotal(driverID));
    }

    @GetMapping("/weeklyIncome")
    public ResponseUtil getWeeklyTotal(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", reservationService.getMonthlyTotal(driverID));
    }

    @GetMapping("/monthlyIncome")
    public ResponseUtil getMonthlyTotal(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", reservationService.getMonthlyTotal(driverID));
    }

    @GetMapping("/ongoingTrip")
    public ResponseUtil getOngoingTrip(@RequestParam Integer driverID) {
        return new ResponseUtil(200, "success", driverService.getOngoingReservation(driverID));
    }

}
