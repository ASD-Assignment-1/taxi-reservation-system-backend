package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String userName);
    Optional<User> findById(Integer userId);
}
