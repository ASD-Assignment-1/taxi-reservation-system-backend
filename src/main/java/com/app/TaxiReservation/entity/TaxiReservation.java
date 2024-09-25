package com.app.TaxiReservation.entity;

import com.app.TaxiReservation.util.Status.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TaxiReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationID", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "driverID", nullable = false)
    private Driver driver;

    @Column(name = "reveredTime", nullable = false)
    private LocalDateTime reveredTime;

    @Column(name = "paymentAmount", nullable = false)
    private double paymentAmount;

    @Column(name = "pickupLatitude", nullable = false)
    private double pickupLatitude;

    @Column(name = "pickupLongitude", nullable = false)
    private double pickupLongitude;

    @Column(name = "dropLatitude", nullable = false)
    private double dropLatitude;

    @Column(name = "dropLongitude", nullable = false)
    private double dropLongitude;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @OneToOne(mappedBy = "taxiReservation")
    private Payment payment;

    @OneToOne(mappedBy = "taxiReservation")
    private Rating rating;

}
