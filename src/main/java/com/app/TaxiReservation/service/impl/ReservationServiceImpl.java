package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.Payment;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.PaymentRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;
import com.app.TaxiReservation.util.Status.PaymentStatus;
import com.app.TaxiReservation.util.Status.ReservationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final double AmountPerKm = 50;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DistanceCalculation distanceCalculation;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public boolean reserveTaxi(ReservationDto reservationDto) {

        try {

            TaxiReservation taxiReservation = new TaxiReservation();
            User user = userRepository.findById(reservationDto.getUserId()).get();
            taxiReservation.setUser(user);

            Driver byUserName = driverRepository.findByUserName(reservationDto.getDriverUserName()).get();

            taxiReservation.setDriver(byUserName);
            taxiReservation.setReveredTime(LocalDateTime.now());

            DistanceResponseDto roadDistance = distanceCalculation.getRoadDistance(reservationDto.getPickupLatitude(), reservationDto.getPickupLongitude(), reservationDto.getDropLatitude(), reservationDto.getDropLongitude());

            double distanceKm = roadDistance.getPaths().get(0).getDistance() / 1000.0;
            BigDecimal roundedValue = new BigDecimal(distanceKm * AmountPerKm).setScale(2, RoundingMode.HALF_UP);
            double roundedAmount = roundedValue.doubleValue();

            taxiReservation.setPaymentAmount(roundedAmount);
            taxiReservation.setPickupLatitude(reservationDto.getPickupLatitude());
            taxiReservation.setPickupLongitude(reservationDto.getPickupLongitude());
            taxiReservation.setDropLatitude(reservationDto.getDropLatitude());
            taxiReservation.setDropLongitude(reservationDto.getDropLongitude());
            taxiReservation.setStatus(ReservationStatus.START);

             sendDriverDetails(user.getEmail(), new DriverDto(
                    byUserName.getName(),
                    byUserName.getEmail(),
                    byUserName.getMobileNumber(),
                    byUserName.getUsername(),
                    byUserName.getLicenseNumber()
            ));

            reservationRepository.save(taxiReservation);
            resetDriverStatus(byUserName.getId(), DriverStatus.BUSY);

            return true;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }

    }

    private void sendDriverDetails(String toEmail, DriverDto driverDto) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Driver Details");

            String emailBody = "Driver Details:\n" +
                    "Name: " + driverDto.getName() + "\n" +
                    "Email: " + driverDto.getEmail() + "\n" +
                    "Mobile Number: " + driverDto.getMobileNumber() + "\n" +
                    "Username: " + driverDto.getUserName() + "\n" +
                    "License Number: " + driverDto.getLicenseNumber();

            message.setText(emailBody);
            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void makePayments(Integer reservationID){

        try {

            TaxiReservation taxiReservation = reservationRepository.findById(reservationID).get();

            Payment payment = new Payment();
            payment.setTaxiReservation(taxiReservation);
            payment.setAmount(taxiReservation.getPaymentAmount());
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.DONE);

            resetDriverStatus(taxiReservation.getDriver().getId(), DriverStatus.AV);

            paymentRepository.save(payment);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    private void resetDriverStatus(Integer driverID , DriverStatus driverStatus){
        try {

            Driver driver = driverRepository.findById(driverID).get();
            driver.setDriverStatus(driverStatus);
            driverRepository.save(driver);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
