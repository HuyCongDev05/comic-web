package com.example.projectrestfulapi.mapper;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.response.user.UserLoginResponseDTO;
import com.example.projectrestfulapi.dto.response.user.UserRegisterResponseDTO;

public class UserMapper {
    public static UserLoginResponseDTO mapUserLoginAuthResponseDTO(String uuid, User user, String accessToken) {
        if (user == null) return null;
        UserLoginResponseDTO userResponse = new UserLoginResponseDTO();
        userResponse.setUuid(uuid);
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setAddress(user.getAddress());
        userResponse.setAccessToken(accessToken);
        return userResponse;
    }

    public static UserRegisterResponseDTO mapUserRegisterResponseDTO(Account account) {
        if (account == null) return null;
        UserRegisterResponseDTO userResponse = new UserRegisterResponseDTO();
        userResponse.setUuid(account.getUuid());
        userResponse.setUsername(account.getUsername());
        userResponse.setEmail(account.getUser().getEmail());
        userResponse.setCreated(account.getCreated());
        return userResponse;
    }
}
