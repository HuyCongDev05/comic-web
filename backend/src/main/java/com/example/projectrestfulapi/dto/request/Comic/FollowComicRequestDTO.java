package com.example.projectrestfulapi.dto.request.Comic;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowComicRequestDTO {
    @NotBlank(message = "Cannot be left blank accountUuid")
    private String accountUuid;

    @NotBlank(message = "Cannot be left blank comicUuid")
    private String comicUuid;
}
