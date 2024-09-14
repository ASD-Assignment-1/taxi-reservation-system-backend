package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.TaxiReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<TaxiReservation, Integer> {
}
