package com.example.projectrestfulapi.exception;

import lombok.Getter;

@Getter
public enum NumberError {
    CREATED(201, "Created successfully"),
    MISING_DATA(400, "Mising data"),
    INCORRECT_DATA(400, "Incorrect data"),
    USER_NOT_FOUND(400, "User not found"),
    ACCOUNT_NOT_FOUND(400, "Account not found"),
    COMIC_NOT_FOUND(400, "Comic not found"),
    GROUP_NOT_FOUND(400, "Group not found"),
    DUPLICATE_ACCOUNT(400, "The sending account cannot be the same as the receiving account"),
    VERIFICATION(400, "Verification Failed"),
    FOLLOW_FAILED(400, "Follow Failed"),
    UNFOLLOW_FAILED(400, "Follow Failed"),
    UNAUTHORIZED(401, "Wrong username or password"),
    NO_REFRESH_TOKEN(401, "No refresh token found"),
    INVALID_REFRESH_TOKEN(401, "Invalid refresh token"),
    INVALID_STATUS(401, "Invalid status"),
    UNAUTHORIZED_EMAIL(401, "Unverified email"),
    FORBIDDEN(403, "Account banded"),
    NOT_FOUND(404, "no data"),
    CONFLICT_USER(409, "Username has been registered"),
    CONFLICT_EMAIL(409, "Email has been registered"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int status;
    private final String message;

    NumberError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
