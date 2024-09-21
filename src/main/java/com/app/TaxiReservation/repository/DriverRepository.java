package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserNameAndActiveTrue(String userName);

    @Query("SELECT driver FROM Driver driver WHERE (:status IS NULL OR driver.driverStatus = :status) AND driver.active = true  ")
    List<Driver> findAllByDriverStatus(DriverStatus status);

    Driver findByUserNameAndDriverStatusAndActive(String userName, DriverStatus driverStatus, boolean active);

    Optional<Driver> findByIdAndActive(Integer driverId, boolean active);

    @Query("SELECT d FROM Driver d " +
            "WHERE (:name IS NULL OR d.name LIKE %:name%) AND d.active = true ")
    List<Driver> findByName(@Param("name") String name);

    @Query("SELECT COUNT(d) FROM Driver d WHERE d.active = true ")
    long countAllDrivers();
}
