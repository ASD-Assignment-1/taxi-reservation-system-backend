package com.app.TaxiReservation.controller;

import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/currentOngoingTrip")
    public ResponseUtil getCurrentOngoingTrip() {
        return new ResponseUtil(200, "success", reservationService.getOngoingReservation());
    }

    @GetMapping("/filteredReservation")
    public ResponseUtil filterReservation(@RequestParam String fromDate, @RequestParam String toDate) {
        return new ResponseUtil(200, "success", reservationService.filterReservation(fromDate, toDate));
    }

}
