package com.app.TaxiReservation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.TaxiReservation.dto.AdminReservationDto;
import com.app.TaxiReservation.dto.ReservationDetailsDto;
import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.PaymentRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.service.impl.AdminServiceImpl;
import java.util.Collections;
import java.util.List;
import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.DriverStatus;
import com.app.TaxiReservation.util.Status.PaymentStatus;
import com.app.TaxiReservation.util.Status.ReservationStatus;

@RunWith(MockitoJUnitRunner.class)

public class AdminServiceImplTest {

    @Mock
    DriverRepository driverRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    AdminServiceImpl adminServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestreserveTaxiManually_DriverAvailable() {
        AdminReservationDto adminReservationDto = new AdminReservationDto();
        adminReservationDto.setDriverUserName("testDriver");
        adminReservationDto.setName("John Doe");
        adminReservationDto.setEmail("john@example.com");
        adminReservationDto.setMobileNumber("1234567890");

        Driver driver = new Driver();
        driver.setUserName("testDriver");
        when(driverRepository.findByUserNameAndDriverStatusAndActive("testDriver", DriverStatus.AV, true))
                .thenReturn(driver);

        User user = new User();
        user.setId(1);
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = adminServiceImpl.reserveTaxiManually(adminReservationDto);

        assertTrue(result);
        verify(reservationService, times(1)).reserveTaxi(any(ReservationDto.class));
    }

    @Test
    public void TestreserveTaxiManually_DriverNotAvailable(){

        AdminReservationDto adminReservationDto = new AdminReservationDto();
        adminReservationDto.setDriverUserName("testDriver");
        
        when(driverRepository.findByUserNameAndDriverStatusAndActive("testDriver", DriverStatus.AV, true))
        .thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class,() ->{
            adminServiceImpl.reserveTaxiManually(adminReservationDto);
        });

        assertEquals("This Driver is not available",exception.getMessage());
    }

     @Test
    public void TestgetAllOngoingReservations() {
        when(reservationRepository.countAllByStatus(ReservationStatus.START)).thenReturn(5L);

        long result = adminServiceImpl.getAllOngoingReservations();

        assertEquals(5L, result);
    }

    @Test
    public void TestgetAllFullTotalIncome() {
        when(paymentRepository.sumAmountByPaymentStatus(PaymentStatus.DONE)).thenReturn(1000.0);

        double result = adminServiceImpl.getAllFullTotalIncome();

        assertEquals(1000.0, result, 0.001);
    }

    @Test
    public void TestgetAllFullTotalIncome_NullValue(){
        when(paymentRepository.sumAmountByPaymentStatus(PaymentStatus.DONE)).thenReturn(null);

        double result = adminServiceImpl.getAllFullTotalIncome();

        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void TestgetUserCountExcludingAdminAndDriver(){
        when(userRepository.countUsersExcludingAdminAndDriver(Role.ADMIN, Role.DRIVER)).thenReturn(10L);

        long userCount = adminServiceImpl.getUserCountExcludingAdminAndDriver();

        assertEquals(10L, userCount);
    }

    @Test
    public void TestgetLastReservation(){
        Pageable pageable = PageRequest.of(0, 5);
        TaxiReservation reservation = new TaxiReservation();
        User user = new User();
        user.setId(1);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        reservation.setUser(user);
        reservation.setId(1);
        reservation.setPickupLatitude(40.7128);
        reservation.setPickupLongitude(-74.0060);
        reservation.setDropLatitude(34.0522);
        reservation.setDropLongitude(-118.2437);
        reservation.setPaymentAmount(50.0);

        when(reservationRepository.findLastReservations(pageable)).thenReturn(Collections.singletonList(reservation));

        List<ReservationDetailsDto> result = adminServiceImpl.getLastReservation();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getUserDto().getName());
        double expectedAmount = 50.0;
        double actualAmount = result.get(0).getPaymentAmount();
        double delta = 0.001; // Adjust the delta as needed
        assertEquals(expectedAmount, actualAmount, delta);
    }

}