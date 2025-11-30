package com.example.projectrestfulapi.dto.request.Comic;

import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateStatusComicRequest {
    @NotBlank(message = "Cannot be left blank accountUuid")
    private String uuidComics;

    @NotBlank(message = "Cannot be left blank accountUuid")
    private String status;

    public String getStatus() {
        if (status.equals("Đang cập nhật") || status.equals("Đã hoàn thành")) {
            return status;
        }
        throw  new InvalidException(NumberError.INVALID_STATUS.getMessage(), NumberError.INVALID_STATUS);
    }
}
