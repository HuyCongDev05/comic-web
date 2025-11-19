package com.example.projectrestfulapi.security.detail;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.service.AccountService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountDetailService implements UserDetailsService {
    private final AccountService accountService;

    public AccountDetailService(AccountService accountService) {
        this.accountService = accountService;
    }

    // thêm transactional khi nhiều quyền
    @Override
    public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
        Account account = accountService.handleLoginAccountLocal(data);
        if (account != null) {
            List<GrantedAuthority> authorities = account.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(
                    account.getUuid(),
                    account.getPassword(),
                    authorities
            );
        } else {
            Account accountOauth = accountService.handleLoginAccountOauth(data);
            List<GrantedAuthority> authorities = accountOauth.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(
                    accountOauth.getUuid(),
                    "",
                    authorities
            );
        }
    }
}
