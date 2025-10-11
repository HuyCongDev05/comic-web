package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.Role;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.account.RegisterAccountDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.AccountRepository;
import com.example.projectrestfulapi.repository.SQL.RoleRepository;
import com.example.projectrestfulapi.repository.SQL.StatusRepository;
import com.example.projectrestfulapi.repository.SQL.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(UserRepository userRepository, AccountRepository accountRepository, StatusRepository statusRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.statusRepository = statusRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account handleLoginAccount(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Wrong username or password"));
    }

    @Transactional
    public Account handleRegisterAccount(@RequestBody RegisterAccountDTO registerAccountDTO) {
        if (!accountRepository.existsByUsername(registerAccountDTO.getUsername())) {
            User user = new User();
            Account account = new Account();
            String password = passwordEncoder.encode(registerAccountDTO.getPassword());
            user.setEmail(registerAccountDTO.getEmail());
            userRepository.save(user);
            account.setUsername(registerAccountDTO.getUsername());
            account.setPassword(password);
            account.setUser(user);
            account.setStatus(statusRepository.findByStatus("NORMAL").get());
            accountRepository.save(account);
            Role role = roleRepository.findByRoleName("USER").orElseThrow();
            account.getRoles().add(role);
            return account;
        } else throw new InvalidException(NumberError.CONFLICT_USER.getMessage(), NumberError.CONFLICT_USER);
    }

    @Transactional
    public boolean handleDeleteAccount(String uuid) {
        if (accountRepository.existsByUuid(uuid)) {
            Account account = accountRepository.findByUuid(uuid).get();
            account.getRoles().clear();
            accountRepository.save(account);
            accountRepository.deleteAllByUuid(uuid);
            userRepository.deleteAllById(account.getUser().getId());
            return true;
        }
        return false;
    }
}
