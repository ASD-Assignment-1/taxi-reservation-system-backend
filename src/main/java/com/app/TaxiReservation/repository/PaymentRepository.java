package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Payment;
import com.app.TaxiReservation.util.Status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = :status")
    Double sumAmountByPaymentStatus(PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "JOIN p.taxiReservation tr " +
            "WHERE tr.driver.id = :driverId " +
            "AND p.paymentStatus = 'DONE' " +
            "AND p.paymentTime >= :startOfDay " +
            "AND p.paymentTime < :endOfDay")
    Double getDailyTotal(@Param("driverId") Integer driverId,
                         @Param("startOfDay") LocalDateTime startOfDay,
                         @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "JOIN p.taxiReservation tr " +
            "WHERE tr.driver.id = :driverId " +
            "AND p.paymentStatus = 'DONE' " +
            "AND p.paymentTime >= :startOfWeek " +
            "AND p.paymentTime < :endOfDay")
    Double getWeeklyTotal(@Param("driverId") Integer driverId,
                          @Param("startOfWeek") LocalDateTime startOfWeek,
                          @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "JOIN p.taxiReservation tr " +
            "WHERE tr.driver.id = :driverId " +
            "AND p.paymentStatus = 'DONE' " +
            "AND p.paymentTime >= :startOfMonth " +
            "AND p.paymentTime < :endOfDay")
    Double getMonthlyTotal(@Param("driverId") Integer driverId,
                           @Param("startOfMonth") LocalDateTime startOfMonth,
                           @Param("endOfDay") LocalDateTime endOfDay);

}
