package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserName(String userName);
    Optional<User> findById(Integer userId);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role <> :adminRole AND u.role <> :driverRole")
    long countUsersExcludingAdminAndDriver(@Param("adminRole") Role adminRole, @Param("driverRole") Role driverRole);
}
