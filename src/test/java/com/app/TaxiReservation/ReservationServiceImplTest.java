package com.app.TaxiReservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.dto.DistanceDto.DistanceResponseDto;
import com.app.TaxiReservation.dto.DistanceDto.PathDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.Payment;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.PaymentRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.impl.ReservationServiceImpl;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DistanceCalculation distanceCalculation;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendDriverDetails(){
        DriverDto driverDto = new DriverDto("John Doe", "john@example.com", "123456789", "JohnDoe", "LIC123");
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        reservationServiceImpl.sendDriverDetails("user@example.com", driverDto);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testResetDriverStatus(){

        Driver driver = new Driver();
        driver.setId(1);
        driver.setLatitude(0.0);
        driver.setLongitude(0.0);

        when(driverRepository.findByIdAndActive(1, true)).thenReturn(Optional.of(driver));

        reservationServiceImpl.resetDriverStatus(1, DriverStatus.AV, 45.0, 90.0); 

        assertEquals(45.0, driver.getLatitude());
        assertEquals(90.0, driver.getLongitude());
        assertEquals(DriverStatus.AV, driver.getDriverStatus());

        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    public void testGetDailyTotal() {
        when(paymentRepository.getDailyTotal(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(500.0);

        Double total = reservationServiceImpl.getDailyTotal(1);

        assertEquals(500.0, total);
    }

    @Test
    public void testGetWeeklyTotal() {
        when(paymentRepository.getWeeklyTotal(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1500.0);

        Double total = reservationServiceImpl.getWeeklyTotal(1);

        assertEquals(1500.0, total);
    }

    @Test
    public void testGetMonthlyTotal() {
        when(paymentRepository.getMonthlyTotal(anyInt(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(3000.0);

        Double total = reservationServiceImpl.getMonthlyTotal(1);

        assertEquals(3000.0, total);
    }

}
