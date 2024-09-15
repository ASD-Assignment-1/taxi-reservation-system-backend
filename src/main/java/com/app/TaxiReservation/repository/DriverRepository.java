package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.util.Status.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserName(String userName);
    List<Driver> findAllByDriverStatus(DriverStatus status);
    Driver findByUserNameAndDriverStatus(String userName, DriverStatus driverStatus);
    Optional<Driver> findById(Integer driverId);
}
