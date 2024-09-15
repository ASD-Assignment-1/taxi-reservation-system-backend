package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
