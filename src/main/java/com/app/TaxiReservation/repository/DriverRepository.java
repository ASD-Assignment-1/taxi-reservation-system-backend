package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.util.Status.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserName(String userName);
    List<Driver> findAllByDriverStatus(DriverStatus status);
}
