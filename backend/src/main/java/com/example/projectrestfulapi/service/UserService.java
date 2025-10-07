package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleFindEmailUsers(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean handleExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> handleFindAllUsers() {
        return userRepository.findAll();
    }

    public User handleUpdateUser(@RequestBody UserUpdateRequestDTO updateUserRequestDTO) {
        User user = userRepository.findById(updateUserRequestDTO.getId())
                .orElseThrow(() -> new InvalidException(NumberError.USER_NOT_FOUND.getMessage(), NumberError.USER_NOT_FOUND));
        if (updateUserRequestDTO.getFirstname() != null) user.setFirstName(updateUserRequestDTO.getFirstname());
        if (updateUserRequestDTO.getLastname() != null) user.setLastName(updateUserRequestDTO.getLastname());
        if (updateUserRequestDTO.getPhone() != null) user.setPhone(updateUserRequestDTO.getPhone());
        if (updateUserRequestDTO.getAddress() != null) user.setAddress(updateUserRequestDTO.getAddress());
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidException(NumberError.INTERNAL_SERVER_ERROR.getMessage(), NumberError.INTERNAL_SERVER_ERROR);
        }
    }
}
