package com.app.TaxiReservation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.hibernate.annotations.processing.SQL;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.TaxiReservation.dto.DriverDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.service.impl.DriverServiceImpl;

@RunWith(MockitoJUnitRunner.class)

public class DriverServiceImplTest {

    @Mock
    DriverRepository driverRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestSaveDriver_Success(){
        DriverDto driverDto = new DriverDto();
        driverDto.setName("John Doe");
        driverDto.setEmail("john@example.com");
        driverDto.setMobileNumber("1234567890");
        driverDto.setUserName("johndoe");
        driverDto.setPassword("password");
        driverDto.setLicenseNumber("ABC123");

        Driver driver = new Driver();
        when(passwordEncoder.encode(driverDto.getPassword())).thenReturn("encodedPassword");
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        boolean result = driverServiceImpl.saveDriver(driverDto);

        assertTrue(result);
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    public void TestSaveDriver_Faild(){
        DriverDto driverDto = new DriverDto();
        when(driverRepository.save(any(Driver.class))).thenThrow(new RuntimeException("Error Saving driver"));

        Exception exception = assertThrows(com.app.TaxiReservation.exception.SQLException.class, () ->{
            driverServiceImpl.saveDriver(driverDto);
        });

        assertEquals("Error Saving driver", exception.getMessage());
    }
}
