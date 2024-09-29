package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.*;
import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            User user = userRepository.findByIdAndActiveTrue(reservationDto.getUserId()).get();
            Driver byUserName = driverRepository.findByUserNameAndActiveTrue(reservationDto.getDriverUserName());

            taxiReservation.setUser(user);
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
                    byUserName.getUserName(),
                    byUserName.getLicenseNumber()
            ));

            reservationRepository.save(taxiReservation);
            resetDriverStatus(byUserName.getId(), DriverStatus.BUSY, taxiReservation.getDropLatitude(), taxiReservation.getDropLongitude());

            return true;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }

    }

    public void sendDriverDetails(String toEmail, DriverDto driverDto) {
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

    @Transactional
    @Override
    public void makePayments(Integer reservationID){

        try {

            TaxiReservation taxiReservation = reservationRepository.findById(reservationID).get();

            Payment payment = new Payment();
            payment.setTaxiReservation(taxiReservation);
            payment.setAmount(taxiReservation.getPaymentAmount());
            payment.setPaymentTime(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.DONE);

            resetDriverStatus(taxiReservation.getDriver().getId(), DriverStatus.AV,taxiReservation.getDropLatitude(),taxiReservation.getDropLongitude());

            paymentRepository.save(payment);

            taxiReservation.setStatus(ReservationStatus.END);
            reservationRepository.save(taxiReservation);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

     public void resetDriverStatus(Integer driverID, DriverStatus driverStatus, double dropLatitude, double dropLongitude){
        try {

            Driver driver = driverRepository.findByIdAndActive(driverID, true).get();
            driver.setDriverStatus(driverStatus);
            driver.setLatitude(dropLatitude);
            driver.setLongitude(dropLongitude);
            driverRepository.save(driver);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Double getDailyTotal(Integer driverId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        Double dailyTotal = paymentRepository.getDailyTotal(driverId, startOfDay, endOfDay);
        return dailyTotal != null ? dailyTotal : 0.0;
    }

    @Override
    public Double getWeeklyTotal(Integer driverId) {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();
        Double weeklyTotal = paymentRepository.getWeeklyTotal(driverId, startOfWeekDateTime, endOfDay);
        return weeklyTotal != null ? weeklyTotal : 0.0;
    }

    @Override
    public Double getMonthlyTotal(Integer driverId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endOfDay = LocalDateTime.now();
        Double monthlyTotal= paymentRepository.getMonthlyTotal(driverId, startOfMonthDateTime, endOfDay);
        return monthlyTotal != null ? monthlyTotal : 0.0;
    }

    @Override
    public List<ReservationDetailsDto> getOngoingReservation() {
        List<Optional<TaxiReservation>> byStatus = reservationRepository.findByStatus(ReservationStatus.START);
        return byStatus.stream()
                .map(taxiReservation -> new ReservationDetailsDto(
                        taxiReservation.get().getId(),
                        new UserDto(
                                taxiReservation.get().getUser().getId(),
                                taxiReservation.get().getUser().getName(),
                                taxiReservation.get().getUser().getEmail(),
                                taxiReservation.get().getUser().getMobileNumber(),
                                taxiReservation.get().getUser().getUserName(),
                                taxiReservation.get().getUser().getRole(),
                                taxiReservation.get().getUser().getUserStatus()
                        ),
                        new DriverDto(
                                taxiReservation.get().getDriver().getName(),
                                taxiReservation.get().getDriver().getEmail(),
                                taxiReservation.get().getDriver().getMobileNumber(),
                                taxiReservation.get().getDriver().getUserName(),
                                taxiReservation.get().getDriver().getLicenseNumber()
                        ),
                        taxiReservation.get().getReveredTime(),
                        taxiReservation.get().getPaymentAmount(),
                        taxiReservation.get().getPickupLatitude(),
                        taxiReservation.get().getPickupLongitude(),
                        taxiReservation.get().getDropLatitude(),
                        taxiReservation.get().getDropLongitude(),
                        taxiReservation.get().getStatus(),
                         null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDetailsDto> filterReservation(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        List<TaxiReservation> taxiReservationList = reservationRepository.findReservationFromDateRange(LocalDateTime.parse(fromDate, formatter),LocalDateTime.parse(toDate, formatter));
        return taxiReservationList.stream()
                .map(taxiReservation -> new ReservationDetailsDto(
                        taxiReservation.getId(),
                        new UserDto(
                                taxiReservation.getUser().getId(),
                                taxiReservation.getUser().getName(),
                                taxiReservation.getUser().getEmail(),
                                taxiReservation.getUser().getMobileNumber(),
                                taxiReservation.getUser().getUserName(),
                                taxiReservation.getUser().getRole(),
                                taxiReservation.getUser().getUserStatus()
                        ),
                        new DriverDto(
                                taxiReservation.getDriver().getName(),
                                taxiReservation.getDriver().getEmail(),
                                taxiReservation.getDriver().getMobileNumber(),
                                taxiReservation.getDriver().getUserName(),
                                taxiReservation.getDriver().getLicenseNumber()
                        ),
                        taxiReservation.getReveredTime(),
                        taxiReservation.getPaymentAmount(),
                        taxiReservation.getPickupLatitude(),
                        taxiReservation.getPickupLongitude(),
                        taxiReservation.getDropLatitude(),
                        taxiReservation.getDropLongitude(),
                        taxiReservation.getStatus(),
                        taxiReservation.getRating() != null ?
                                new RatingDto(
                                        taxiReservation.getRating().getUser().getId(),
                                        taxiReservation.getRating().getDriver().getId(),
                                        taxiReservation.getRating().getScore(),
                                        taxiReservation.getRating().getTaxiReservation().getId(),
                                        taxiReservation.getRating().getReview()
                                ) : null
                ))
                .collect(Collectors.toList());
    }

}
