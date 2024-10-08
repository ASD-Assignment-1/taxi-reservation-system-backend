package com.app.TaxiReservation.dto;

import com.app.TaxiReservation.util.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public UserDto(String name, String email, String mobileNumber) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public UserDto(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }
}
