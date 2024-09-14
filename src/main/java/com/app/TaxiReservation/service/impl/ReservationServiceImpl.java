package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DistanceCalculation distanceCalculation;

    @Autowired
    private ReservationRepository reservationRepository;

    private final double AmountPerKm = 50;

    @Override
    public boolean reserveTaxi(ReservationDto reservationDto){

        try {

            TaxiReservation taxiReservation = new TaxiReservation();
            taxiReservation.setUser(userRepository.findById(reservationDto.getUserId()).get());
            taxiReservation.setDriver(driverRepository.findByUserName(reservationDto.getDriverUserName()));
            taxiReservation.setReveredTime(LocalDateTime.now());

            DistanceResponseDto roadDistance = distanceCalculation.getRoadDistance(
                    reservationDto.getPickupLatitude(),
                    reservationDto.getPickupLongitude(),
                    reservationDto.getDropLatitude(),
                    reservationDto.getDropLongitude());

            double distanceKm = roadDistance.getPaths().get(0).getDistance() / 1000.0;
            BigDecimal roundedValue = new BigDecimal(distanceKm * AmountPerKm).setScale(2, RoundingMode.HALF_UP);
            double roundedAmount = roundedValue.doubleValue();

            taxiReservation.setPaymentAmount(roundedAmount);
            taxiReservation.setPickupLatitude(reservationDto.getPickupLatitude());
            taxiReservation.setPickupLongitude(reservationDto.getPickupLongitude());
            taxiReservation.setDropLatitude(reservationDto.getDropLatitude());
            taxiReservation.setDropLongitude(reservationDto.getDropLongitude());
            taxiReservation.setStatus(ReservationStatus.START);

            reservationRepository.save(taxiReservation);

            return true;

        }catch (Exception e){

            throw new RuntimeException(e.getMessage());

        }

    }
}
