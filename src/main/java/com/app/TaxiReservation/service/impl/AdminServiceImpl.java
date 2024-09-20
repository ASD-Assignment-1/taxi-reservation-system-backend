package com.app.TaxiReservation.service.impl;

import com.app.TaxiReservation.dto.AdminReservationDto;
import com.app.TaxiReservation.dto.ReservationDto;
import com.app.TaxiReservation.dto.UserDto;
import com.app.TaxiReservation.entity.Driver;
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
import org.springframework.stereotype.Service;

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

    public double getAllFullTotalIncome(){
        return paymentRepository.sumAmountByPaymentStatus(PaymentStatus.DONE);
    }

}
