package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.util.Status.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<TaxiReservation, Integer> {

    long countAllByStatus(ReservationStatus status);

    Optional<TaxiReservation> findById(Integer integer);

}
