package com.example.projectrestfulapi.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


public class VerificationEmailRequestDTO {
    @Getter
    @Setter
    public static class SendEmailGetOtp {
        @NotBlank(message = "Cannot be left blank email")
        @Email(message = "Invalid email format")
        private String email;
    }

    @Getter
    @Setter
    public static class VerificationEmail {
        @NotBlank(message = "Cannot be left blank email")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Cannot be left blank OTP")
        @Size(min = 6, max = 6, message = "The verification code is only 6 characters long")
        private String otp;
    }
}