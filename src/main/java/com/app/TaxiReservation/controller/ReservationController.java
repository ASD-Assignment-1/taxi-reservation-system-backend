package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reserve")
@CrossOrigin
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping
    public ResponseUtil reserveTaxi(@RequestBody ReservationDto reservationDto){
        return new ResponseUtil(200, "success", reservationService.reserveTaxi(reservationDto));
    }

}
