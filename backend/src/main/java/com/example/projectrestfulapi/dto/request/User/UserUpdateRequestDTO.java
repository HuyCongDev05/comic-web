package com.example.projectrestfulapi.dto.request.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {

    @NotBlank(message = "Cannot be left blank firstname")
    private String firstName;

    @NotBlank(message = "Cannot be left blank lastname")
    private String lastName;

    private String phone;

    @NotBlank(message = "Cannot be left blank phone")
    private String email;

    private String address;

    private String avatar;
}
