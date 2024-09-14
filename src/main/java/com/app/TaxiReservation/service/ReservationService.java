package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.ReservationDto;

public interface ReservationService {
    boolean reserveTaxi(ReservationDto reservationDto);
}
