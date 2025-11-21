package com.example.projectrestfulapi.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class ChangeAccountDTO {
    @Getter
    @Setter
    public static class changeStatusAccounts {
        @NotBlank(message = "Cannot be left blank status")
        private String status;
    }
}
