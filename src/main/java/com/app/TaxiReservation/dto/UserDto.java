package com.app.TaxiReservation.dto;

import com.app.TaxiReservation.util.Role;
import com.app.TaxiReservation.util.Status.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String email;
    private String mobileNumber;
    private String userName;
    private String password;
    private Role role;
    private UserStatus userStatus;
    private LocalDateTime lastLogInDate;
    private LocalDateTime lastLogOutDate;

    public UserDto(String name, String email, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public UserDto(Integer id, String name, String email, String mobileNumber, String userName, Role role,UserStatus userStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.role = role;
        this.userStatus = userStatus;
    }

    public UserDto(Integer id, String name, String email, String mobileNumber, String userName, Role role,LocalDateTime lastLogInDate,LocalDateTime lastLogOutDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.role = role;
        this.lastLogInDate = lastLogInDate;
        this.lastLogOutDate = lastLogOutDate;
    }
}
