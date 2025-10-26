package com.example.projectrestfulapi.dto.request.message;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class GroupRequestDTO {

    @Getter
    @Setter
    public static class createGroupRequestDTO {

        @NotBlank(message = "Cannot be left blank group name")
        private String groupName;

        @NotBlank(message = "Cannot be left blank avatar")
        private String groupAvatar;

        @NotBlank(message = "Cannot be left blank accountUuid")
        private String accountUuid;

        @NotBlank(message = "Cannot be left blank accountAvatar")
        private String accountAvatar;

        public Group.AccountJoin getAccountUuid() {
            Group.AccountJoin groupUserJoin = new Group.AccountJoin();
            groupUserJoin.setAccountUuid(accountUuid);
            groupUserJoin.setAvatar(accountAvatar);
            groupUserJoin.setRole("ADMIN");
            groupUserJoin.setJoined(Instant.now());
            return groupUserJoin;
        }

    }

    @Getter
    @Setter
    public static class joinGroupRequestDTO {
        @NotBlank(message = "Cannot be left blank users")
        private String users;

        @NotBlank(message = "Cannot be left blank messages")
        private String groupName;
    }
}
