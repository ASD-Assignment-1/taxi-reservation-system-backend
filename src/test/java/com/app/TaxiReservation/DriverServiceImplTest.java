package com.app.TaxiReservation;

import com.app.TaxiReservation.dto.*;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.service.impl.DriverServiceImpl;
import com.app.TaxiReservation.util.DistanceCalculation;
import com.app.TaxiReservation.util.Status.DriverStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DistanceCalculation distanceCalculation;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    private DriverDto driverDto;
    private Driver driver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        driverDto = new DriverDto("Test Name", "test@example.com", "1234567890", "testUser", "ABC123", "image.png", null);
        driver = new Driver();
        driver.setId(1);
        driver.setName(driverDto.getName());
        driver.setEmail(driverDto.getEmail());
        driver.setMobileNumber(driverDto.getMobileNumber());
        driver.setUserName(driverDto.getUserName());
        driver.setPassword(driverDto.getPassword());
        driver.setLicenseNumber(driverDto.getLicenseNumber());
        driver.setProfileImage(driverDto.getProfileImage());
        driver.setLastLogInDate(LocalDateTime.now());
        driver.setActive(true);
 
    }

    @Test
    public void testSaveDriver_Success(){

        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        boolean result = driverServiceImpl.saveDriver(driverDto);

        assertTrue(result);
    }

    @Test
    public void testSaveDriver_Failed(){
        when(driverRepository.save(any(Driver.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(SQLException.class, () -> driverServiceImpl.saveDriver(driverDto));

        assertEquals("Database error", exception.getMessage());

    }

    @Test
    public void testLogin_Success(){

        when(driverRepository.findByUserNameAndActiveTrue("testUser")).thenReturn(driver);
        driver.setPassword("password");

        LoginInputDto loginInputDto = new LoginInputDto("testUser", "password", 0.0, 0.0);
        DriverDto result = driverServiceImpl.login(loginInputDto);

        assertNotNull(result);
        assertEquals(driver.getEmail(), result.getEmail());
    }

}
