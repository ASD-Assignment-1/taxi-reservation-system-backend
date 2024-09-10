package com.app.TaxiReservation.entity;

import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driverID", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false)
    private String Name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mobileNumber", unique = true, nullable = false)
    private String mobileNumber;

    @Column(name = "userName", unique = true, nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "licenseNumber", nullable = false)
    private String licenseNumber;

    @Column(name = "lastLogInDate")
    private Instant lastLogInDate;

    @Column(name = "lastLogOutDate")
    private Instant lastLogOutDate;


}
