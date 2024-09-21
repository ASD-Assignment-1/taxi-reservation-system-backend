package com.app.TaxiReservation.entity;

import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mobileNumber", unique = true, nullable = false)
    private String mobileNumber;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "lastLogInDate")
    private LocalDateTime lastLogInDate;

    @Column(name = "lastLogOutDate")
    private LocalDateTime lastLogOutDate;

    @OneToOne(mappedBy = "user")
    private TaxiReservation taxiReservation;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

}
