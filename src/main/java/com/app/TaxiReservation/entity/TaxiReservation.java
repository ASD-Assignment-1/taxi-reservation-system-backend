package com.app.TaxiReservation.entity;

import com.app.TaxiReservation.util.Status.ReservationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne(cascade = CascadeType.ALL)
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

}
