package com.app.TaxiReservation;

import com.app.TaxiReservation.dto.*;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
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
import java.util.Optional;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
 
    }

    @Test
    public void testSaveDriver_Success(){

        DriverDto driverDto = new DriverDto();
        driverDto.setName("John Doe");
        driverDto.setEmail("john@example.com");
        driverDto.setMobileNumber("1234567890");
        driverDto.setUserName("JohnDoe");
        driverDto.setLicenseNumber("ABC123");
        driverDto.setProfileImage("image.png");
        driverDto.setAverageScore(null);

        Driver driver = new Driver();
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

        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        boolean result = driverServiceImpl.saveDriver(driverDto);

        assertTrue(result);
    }

    @Test
    public void testSaveDriver_Failed(){

        DriverDto driverDto = new DriverDto();
        driverDto.setName("John");
        driverDto.setEmail("john@example.com");

        when(driverRepository.save(any(Driver.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(SQLException.class, () -> driverServiceImpl.saveDriver(driverDto));

        assertEquals("Database error", exception.getMessage());

    }

    @Test
    public void testLogin_Success(){

        LoginInputDto loginInputDto = new LoginInputDto("JohnDoe", "password", 0.0, 0.0);
        Driver driver = new Driver();
        driver.setUserName("JohnDoe");
        driver.setPassword("password");
        driver.setLatitude(0.0);
        driver.setLongitude(0.0);
        driver.setDriverStatus(DriverStatus.AV);

        when(driverRepository.findByUserNameAndActiveTrue("JohnDoe")).thenReturn(driver);
        driver.setPassword("password");

        DriverDto result = driverServiceImpl.login(loginInputDto);

        assertNotNull(result);
        assertEquals(driver.getEmail(), result.getEmail());
    }

    @Test
    public void testLogin_Failed(){
        LoginInputDto loginInputDto = new LoginInputDto("JohnDoe", "wrongpassword", 0.0, 0.0);
        Driver driver = new Driver();
        driver.setUserName("JohnDoe");
        driver.setPassword("password");
        driver.setLatitude(0.0);
        driver.setLongitude(0.0);

        when(driverRepository.findByUserNameAndActiveTrue("JohnDoe")).thenReturn(driver);

            assertThrows(SQLException.class, () -> {
            driverServiceImpl.login(loginInputDto);
        });
    }

    @Test
    public void testDeleteDrive(){
        Driver driver = new Driver();
        driver.setId(1);
        driver.setActive(true);

        when(driverRepository.findByIdAndActive(1, true)).thenReturn(java.util.Optional.of(driver));

        boolean result = driverServiceImpl.deleteDriver(1);
        assertTrue(result);
        verify(driverRepository, times(1)).save(driver);
        assertFalse(driver.isActive());
    }

    @Test
    public void testChangePassword_Success() {

        ChangePasswordDto changePasswordDto = new ChangePasswordDto(1, "currentPassword", "newPassword");
        Driver driver = new Driver();
        driver.setId(1);
        driver.setPassword("currentPassword");

        when(driverRepository.findById(changePasswordDto.getId())).thenReturn(Optional.of(driver));

        boolean result = driverServiceImpl.changePassword(changePasswordDto);

        assertTrue(result);
        assertEquals("newPassword", driver.getPassword());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    public void testChangePassword_Failed(){
        ChangePasswordDto changePasswordDto = new ChangePasswordDto(1, "wrongPassword", "newPassword");
        Driver driver = new Driver();
        driver.setId(1);
        driver.setPassword("currentPassword");

        when(driverRepository.findById(changePasswordDto.getId())).thenReturn(Optional.of(driver));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            driverServiceImpl.changePassword(changePasswordDto);
        });

        assertEquals("Current Password is invalid,Please enter your correct password", exception.getMessage());
    }

}
