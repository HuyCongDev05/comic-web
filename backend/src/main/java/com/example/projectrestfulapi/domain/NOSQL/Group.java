package com.example.projectrestfulapi.domain.NOSQL;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("groups")
public class Group {
    @Id
    private String id;

    private String groupName;

    private String avatar;

    private Instant created;
    private List<AccountJoin> accounts = new ArrayList<>();

    public Group(String groupName, String avatar, AccountJoin accountJoin) {
        this.groupName = groupName;
        this.avatar = avatar;
        this.accounts.add(accountJoin);
        this.created = Instant.now();
    }

    @Getter
    @Setter
    public static class AccountJoin {
        private String accountUuid;
        private String avatar;
        private String role;
        private Instant joined;
    }
}
