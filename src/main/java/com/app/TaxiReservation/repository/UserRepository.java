package com.app.TaxiReservation.repository;

import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserNameAndActiveTrue(String userName);
    Optional<User> findByIdAndActiveTrue(Integer userId);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role <> :adminRole AND u.role <> :driverRole AND u.active = true")
    long countUsersExcludingAdminAndDriver(@Param("adminRole") Role adminRole, @Param("driverRole") Role driverRole);
    List<User> findAllByActiveTrue();
}
