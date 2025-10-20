package com.example.projectrestfulapi.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAccountDTO {
    @NotBlank(message = "Cannot be left blank username")
    @Size(min = 6, max = 20, message = "Username must be greater than or equal to 6 and less than 20 characters")
    private String username;

    @NotBlank(message = "Cannot be left blank password")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$",
            message = "Password must be greater than or equal to 6 characters and include uppercase, lowercase and special characters"
    )
    private String password;
}
