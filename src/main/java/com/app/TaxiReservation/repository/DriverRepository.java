package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByUserName(String userName);
}
