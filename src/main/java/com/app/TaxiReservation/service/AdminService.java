package com.app.TaxiReservation.service;

import com.app.TaxiReservation.dto.AdminReservationDto;

public interface AdminService {
    boolean reserveTaxiManually(AdminReservationDto adminReservationDto);
    long getAllOngoingReservations();
    double getAllFullTotalIncome();
    long getUserCountExcludingAdminAndDriver();
}
