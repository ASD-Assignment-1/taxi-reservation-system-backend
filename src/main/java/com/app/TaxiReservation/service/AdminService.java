package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.AdminReservationDto;
import com.app.TaxiReservation.dto.ReservationDetailsDto;

import java.util.List;

public interface AdminService {
    boolean reserveTaxiManually(AdminReservationDto adminReservationDto);
    long getAllOngoingReservations();
    double getAllFullTotalIncome();
    long getUserCountExcludingAdminAndDriver();
    List<ReservationDetailsDto> getLastReservation();
}
