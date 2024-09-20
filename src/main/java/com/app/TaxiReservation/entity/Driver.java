package com.app.TaxiReservation.entity;

import com.app.TaxiReservation.util.Status.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String name;

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
    private DriverStatus driverStatus;

    @Column(name = "licenseNumber", nullable = false)
    private String licenseNumber;

    @Column(name = "lastLogInDate")
    private LocalDateTime lastLogInDate;

    @Column(name = "lastLogOutDate")
    private LocalDateTime  lastLogOutDate;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "profileImage", columnDefinition = "TEXT")
    private String profileImage;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "driver")
    private List<TaxiReservation> taxiReservation;

    @OneToMany(mappedBy = "driver")
    private List<Rating> ratings;

}
