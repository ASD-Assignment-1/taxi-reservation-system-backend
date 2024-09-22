package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.util.Status.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<TaxiReservation, Integer> {

    long countAllByStatus(ReservationStatus status);

    Optional<TaxiReservation> findById(Integer integer);

    @Query("SELECT r FROM TaxiReservation r WHERE r.driver.id = :driverId ORDER BY r.reveredTime DESC")
    List<TaxiReservation> findAllByDriverID(@Param("driverId") Integer driverId);

    @Query("SELECT r FROM TaxiReservation r WHERE r.driver.id = :driverId ORDER BY r.reveredTime DESC")
    List<TaxiReservation> findLastReservationsByDriverId(@Param("driverId") Integer driverId, Pageable pageable);

    @Query("SELECT r FROM TaxiReservation r WHERE r.user.id = :userId ORDER BY r.reveredTime DESC")
    List<TaxiReservation> findLastReservationsByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT r FROM TaxiReservation r  ORDER BY r.reveredTime DESC")
    List<TaxiReservation> findLastReservations(Pageable pageable);

    @Query("SELECT r FROM TaxiReservation r WHERE r.driver.id = :driverId AND r.status =:status ")
    TaxiReservation findOngoingTripByDriverID(@Param("driverId") Integer driverId, @Param("status") ReservationStatus status);
}
