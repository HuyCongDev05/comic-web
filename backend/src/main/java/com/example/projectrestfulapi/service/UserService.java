package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.User.UserUpdateRequestDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import com.example.projectrestfulapi.repository.SQL.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User handleFindEmailUsers(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean handleExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> handleGetAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User handleUpdateUser(@RequestBody String uuid, @RequestBody UserUpdateRequestDTO updateUserRequestDTO) {
        User user = accountRepository.findByUuid(uuid)
                .map(Account::getUser)
                .flatMap(u -> userRepository.findById(u.getId()))
                .orElseThrow(() -> new InvalidException(
                        NumberError.USER_NOT_FOUND.getMessage(),
                        NumberError.USER_NOT_FOUND
                ));
        if (updateUserRequestDTO.getFirstName() != null) user.setFirstName(updateUserRequestDTO.getFirstName());
        if (updateUserRequestDTO.getLastName() != null) user.setLastName(updateUserRequestDTO.getLastName());
        if (updateUserRequestDTO.getPhone() != null) user.setPhone(updateUserRequestDTO.getPhone());
        if (updateUserRequestDTO.getEmail() != null) user.setEmail(updateUserRequestDTO.getEmail());
        if (updateUserRequestDTO.getAddress() != null) user.setAddress(updateUserRequestDTO.getAddress());
        if (updateUserRequestDTO.getAvatar() != null)
            accountRepository.updateAvatarByUuid(uuid, updateUserRequestDTO.getAvatar());
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidException(NumberError.INTERNAL_SERVER_ERROR.getMessage(), NumberError.INTERNAL_SERVER_ERROR);
        }
    }
}
