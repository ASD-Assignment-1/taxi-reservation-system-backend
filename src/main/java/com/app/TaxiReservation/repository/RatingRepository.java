package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
