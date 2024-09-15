package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Payment;
import com.app.TaxiReservation.util.Status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = :status")
    Double sumAmountByPaymentStatus(PaymentStatus status);

}
