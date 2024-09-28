package com.app.TaxiReservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.app.TaxiReservation.dto.LoginInputDto;
import com.app.TaxiReservation.dto.LoginUserOutputDto;
import com.app.TaxiReservation.dto.RatingDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.Rating;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.SQLException;
import com.app.TaxiReservation.exception.UserNotExistException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.RatingRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.DriverService;
import com.app.TaxiReservation.service.impl.UserServiceImpl;
import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.UserStatus;

@RunWith(MockitoJUnitRunner.class)

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private DriverService driverService;

     @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserRegistration(){
        
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setMobileNumber("1234567890");
        userDto.setUserName("JohnDoe");
        userDto.setPassword("password");
        userDto.setRole(Role.USER);
        userDto.setUserStatus(UserStatus.USER);

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = userService.userRegistration(userDto);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    public void testLogin_Success(){

        LoginInputDto loginInputDto = new LoginInputDto("johnDoe", "password", 0.0, 0.0);

        User user = new User();
        user.setId(1);
        user.setPassword("password");
        user.setUserStatus(UserStatus.USER);

        when(userRepository.findByUserNameAndActiveTrue(loginInputDto.getUserName())).thenReturn(user);

        LoginUserOutputDto result = userService.login(loginInputDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUserDto().getId());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    public void testLogin_UserNotFound(){

        LoginInputDto loginInputDto = new LoginInputDto("johnDoe", "password", 0.0, 0.0);

        when(userRepository.findByUserNameAndActiveTrue(loginInputDto.getUserName())).thenReturn(null);

        assertThrows(UserNotExistException.class, () -> userService.login(loginInputDto));

    }

    @Test
    public void testRateDriver(){

        RatingDto ratingDto = new RatingDto(1, 1, 5, null, "Great Ride");
        when(userRepository.findByIdAndActiveTrue(ratingDto.getUserID())).thenReturn(Optional.of(new User()));
        when(driverRepository.findById(ratingDto.getDriverID())).thenReturn(Optional.of(new Driver()));
        when(reservationRepository.findById(ratingDto.getReservationID())).thenReturn(Optional.of(new TaxiReservation()));

        boolean result = userService.rateDriver(ratingDto);

        assertTrue(result);
        verify(ratingRepository, times(1)).save(any(Rating.class));

    }

    @Test
    public void testGetAllActiveUsers() {
        when(userRepository.findAllByUserStatusAndActiveTrue(UserStatus.USER)).thenReturn(List.of(new User()));

        List<UserDto> result = userService.getAllActiveUsers();

        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findAllByUserStatusAndActiveTrue(UserStatus.USER);
    }

    @Test
    public void testDeleteUser() {

        User user = new User();
        user.setId(1);
        when(userRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser() {

        UserDto userDto = new UserDto();

        User existingUser = new User();
        existingUser.setId(userDto.getId());
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(existingUser));

        boolean result = userService.updateUser(userDto);

        assertTrue(result);
        verify(userRepository, times(1)).save(existingUser);
    }

}
