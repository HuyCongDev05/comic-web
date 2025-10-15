package com.example.projectrestfulapi.dto.response.message;

import com.example.projectrestfulapi.domain.NOSQL.Group;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

public class GroupResponseDTO {
    @Getter
    @Setter
    public static class createGroup{
        private String groupName;
        private String avatar_url;
        private Instant created;
        private List<Group.UserJoin> users;
    }

    @Getter
    @Setter
    public static class ListGroupJoinResponseDTO {
        private String groupName;
        private String avatar_url;
    }
}
