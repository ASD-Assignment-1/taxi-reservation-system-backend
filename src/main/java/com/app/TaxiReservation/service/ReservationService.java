package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.ReservationAgainstUserDto;
import com.app.TaxiReservation.dto.ReservationDetailsDto;
import com.app.TaxiReservation.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    boolean reserveTaxi(ReservationDto reservationDto);

    void makePayments(Integer reservationID);

    Double getDailyTotal(Integer driverId);

    Double getWeeklyTotal(Integer driverId);

    Double getMonthlyTotal(Integer driverId);

    List<ReservationDetailsDto> getOngoingReservation();
}
