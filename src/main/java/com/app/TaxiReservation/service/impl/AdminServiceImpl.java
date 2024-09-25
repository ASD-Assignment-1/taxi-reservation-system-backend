package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.*;
import com.app.TaxiReservation.entity.Driver;
import com.app.TaxiReservation.entity.TaxiReservation;
import com.app.TaxiReservation.entity.User;
import com.app.TaxiReservation.exception.RuntimeException;
import com.app.TaxiReservation.repository.DriverRepository;
import com.app.TaxiReservation.repository.PaymentRepository;
import com.app.TaxiReservation.repository.ReservationRepository;
import com.app.TaxiReservation.repository.UserRepository;
import com.app.TaxiReservation.service.AdminService;
import com.app.TaxiReservation.service.ReservationService;
import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.DriverStatus;
import com.app.TaxiReservation.util.Status.PaymentStatus;
import com.app.TaxiReservation.util.Status.ReservationStatus;
import com.app.TaxiReservation.util.Status.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    @Transactional
    public boolean reserveTaxiManually(AdminReservationDto adminReservationDto){
        try {

            Driver byUserName = driverRepository.findByUserNameAndDriverStatusAndActive(adminReservationDto.getDriverUserName(), DriverStatus.AV, true);

            if (byUserName == null) {
                throw new RuntimeException("This Driver is not available");
            }

            User createdUser = saveUser(new UserDto(adminReservationDto.getName(), adminReservationDto.getEmail(), adminReservationDto.getMobileNumber()));

            ReservationDto reservationDto  = new ReservationDto();
            reservationDto.setUserId(createdUser.getId());
            reservationDto.setDriverUserName(byUserName.getUserName());
            reservationDto.setPickupLatitude(adminReservationDto.getPickupLatitude());
            reservationDto.setPickupLongitude(adminReservationDto.getPickupLongitude());
            reservationDto.setDropLatitude(adminReservationDto.getDropLatitude());
            reservationDto.setDropLongitude(adminReservationDto.getDropLongitude());

            reservationService.reserveTaxi(reservationDto);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private User saveUser(UserDto userDto){

        try {

            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            user.setMobileNumber(userDto.getMobileNumber());
            user.setUserStatus(UserStatus.GUEST);
            user.setActive(true);
            user.setRole(Role.USER);

            User createdUser = userRepository.save(user);
            return createdUser;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public long getAllOngoingReservations(){
       return reservationRepository.countAllByStatus(ReservationStatus.START);
    }

    @Override
    public double getAllFullTotalIncome(){
        Double totalIncome = paymentRepository.sumAmountByPaymentStatus(PaymentStatus.DONE);
        return totalIncome != null ? totalIncome : 0.0;
    }

    @Override
    public long getUserCountExcludingAdminAndDriver() {
        return userRepository.countUsersExcludingAdminAndDriver(Role.ADMIN, Role.DRIVER);
    }

    public List<ReservationDetailsDto> getLastReservation() {
        Pageable pageable = PageRequest.of(0, 5);
        List<TaxiReservation> taxiReservationList = reservationRepository.findLastReservations(pageable);
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
                        null,
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
