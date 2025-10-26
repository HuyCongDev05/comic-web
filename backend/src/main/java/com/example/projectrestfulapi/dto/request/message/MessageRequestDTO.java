package com.example.projectrestfulapi.dto.request.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDTO {

    @Getter
    @Setter
    public static class MessagePrivate {
        @NotBlank(message = "Cannot be left blank accountUuidSend")
        private String accountUuidSend;

        @NotBlank(message = "Cannot be left blank fullNameAccountSend")
        private String fullNameAccountSend;

        private String avatarAccountSend;

        @NotBlank(message = "Cannot be left blank accountUuidReceive")
        private String accountUuidReceive;

        @NotBlank(message = "Cannot be left blank message")
        private String message;
    }

    @Getter
    @Setter
    public static class MessageGroup {
        @NotBlank(message = "Cannot be left blank accountUuidSend")
        private String accountUuidSend;

        @NotBlank(message = "Cannot be left blank fullNameAccountSend")
        private String fullNameAccountSend;

        private String avatarAccountSend;

        @NotBlank(message = "Cannot be left blank message")
        private String message;
    }
}
