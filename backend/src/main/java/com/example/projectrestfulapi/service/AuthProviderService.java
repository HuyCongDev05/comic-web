package com.example.projectrestfulapi.service;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.AuthProvider;
import com.example.projectrestfulapi.domain.SQL.Role;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.repository.SQL.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthProviderService {
    private final AuthProviderRepository authProviderRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    public AuthProviderService(AuthProviderRepository authProviderRepository, UserRepository userRepository, StatusRepository statusRepository, AccountRepository accountRepository, RoleRepository roleRepository) {
        this.authProviderRepository = authProviderRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public List<AuthProvider> handleFindByAccountId(Long accountId) {
        return authProviderRepository.findByAccountIdIs(accountId);
    }

    @Transactional
    public Account handleLoginOrRegisterAccount(String providerAccountId, String firstname, String lastname, String email, String avatar) {
        if (authProviderRepository.existsByProviderAccountId(providerAccountId)) {
            Long accountId = authProviderRepository.findAccountIdByProviderAccountId(providerAccountId);
            return accountRepository.findById(accountId).orElseThrow(() -> new InvalidException(NumberError.ACCOUNT_NOT_FOUND.getMessage(), NumberError.ACCOUNT_NOT_FOUND));
        }else {
            User user = new User();
            user.setFirstName(firstname);
            user.setLastName(lastname);
            user.setEmail(email);
            userRepository.save(user);
            Account account = new Account();
            account.setUser(user);
            account.setUsername(email);
            account.setAvatar(avatar);
            account.setStatus(statusRepository.findByStatus("NORMAL").get());
            accountRepository.save(account);
            AuthProvider authProvider = new AuthProvider();
            authProvider.setAccount(account);
            authProvider.setProvider("Google");
            authProvider.setProviderAccountId(providerAccountId);
            authProviderRepository.save(authProvider);
            Role role = roleRepository.findByRoleName("USER").orElseThrow();
            account.getRoles().add(role);
            return account;
        }
    }
}
