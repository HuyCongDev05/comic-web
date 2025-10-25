package com.example.projectrestfulapi.dto.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String avatar;
}
