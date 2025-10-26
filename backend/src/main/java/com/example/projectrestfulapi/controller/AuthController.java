package com.example.projectrestfulapi.controller;

import com.example.projectrestfulapi.domain.SQL.Account;
import com.example.projectrestfulapi.domain.SQL.User;
import com.example.projectrestfulapi.dto.request.account.LoginAccountDTO;
import com.example.projectrestfulapi.dto.request.account.RegisterAccountDTO;
import com.example.projectrestfulapi.dto.request.account.VerificationEmailRequestDTO;
import com.example.projectrestfulapi.dto.response.user.UserLoginResponseDTO;
import com.example.projectrestfulapi.dto.response.user.UserRegisterResponseDTO;
import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import com.example.projectrestfulapi.mapper.UserMapper;
import com.example.projectrestfulapi.service.*;
import com.example.projectrestfulapi.util.OTPMail.OtpUtil;
import com.example.projectrestfulapi.util.Security.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;
    private final UserDetailsService userDetailsService;
    private final StatusService statusService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil, AccountService accountService, UserService userService, EmailVerificationService emailVerificationService, AuthService authService, UserDetailsService userDetailsService, StatusService statusService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.statusService = statusService;
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<Map<String,String>> refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidException(NumberError.NO_REFRESH_TOKEN.getMessage(), NumberError.NO_REFRESH_TOKEN);
        }
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new InvalidException(NumberError.NO_REFRESH_TOKEN.getMessage(), NumberError.NO_REFRESH_TOKEN));
        String username = jwtUtil.extractUsername(refreshToken);
        if(!authService.handleExistsByUsername(username)) {
            throw new InvalidException(NumberError.INVALID_REFRESH_TOKEN.getMessage(), NumberError.INVALID_REFRESH_TOKEN);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.createAccessToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        Map<String,String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody LoginAccountDTO loginAccountDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginAccountDTO.getUsername(), loginAccountDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        authService.handleSave(loginAccountDTO.getUsername(), refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = accountService.handleLoginAccount(loginAccountDTO.getUsername());
        User user = userService.handleFindEmailUsers(account.getUser().getEmail());
        String accountUuid = accountService.handleGetUuidByUserId(user.getId());
        UserLoginResponseDTO userResponseDTO = UserMapper.mapUserLoginAuthResponseDTO(accountUuid, user, account.getAvatar(), statusService.handleGetStatusByUuidAccount(accountUuid), accessToken);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> register(@Valid @RequestBody RegisterAccountDTO registerAccountDTO) {
        if (!emailVerificationService.handleExistsInVerifiedEmails(registerAccountDTO.getEmail()))
            throw new InvalidException(NumberError.UNAUTHORIZED_EMAIL.getMessage(), NumberError.UNAUTHORIZED_EMAIL);
        Account account = accountService.handleRegisterAccount(registerAccountDTO);
        UserRegisterResponseDTO userResponseDTO = UserMapper.mapUserRegisterResponseDTO(account);
        return ResponseEntity.status(NumberError.CREATED.getStatus()).body(userResponseDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidException(NumberError.NO_REFRESH_TOKEN.getMessage(), NumberError.NO_REFRESH_TOKEN);
        }
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new InvalidException(NumberError.NO_REFRESH_TOKEN.getMessage(), NumberError.NO_REFRESH_TOKEN));
        String username = jwtUtil.extractUsername(refreshToken);
        authService.handleDelete(username);
        return ResponseEntity.ok().body(Collections.emptyMap());
    }

    @PostMapping("/email/send-otp")
    public ResponseEntity<Void> sendOTP(@Valid @RequestBody VerificationEmailRequestDTO.SendEmailGetOtp sendEmailGetOtp) throws MessagingException {
        if (userService.handleExistsByEmail(sendEmailGetOtp.getEmail()))
            throw new InvalidException(NumberError.CONFLICT_EMAIL.getMessage(), NumberError.CONFLICT_EMAIL);
        if (emailVerificationService.handleExistsByEmail(sendEmailGetOtp.getEmail()))
            throw new RuntimeException("You must wait before requesting a new OTP");
        String otp = OtpUtil.generateOTP();
        emailVerificationService.handleSave(sendEmailGetOtp.getEmail(), otp);
        emailVerificationService.sendOTP(sendEmailGetOtp.getEmail(), otp);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyOTP(@Valid @RequestBody VerificationEmailRequestDTO.VerificationEmail verificationEmail) {
        if (!emailVerificationService.handleVerification(verificationEmail.getEmail(), verificationEmail.getOtp()))
            throw new InvalidException(NumberError.VERIFICATION.getMessage(), NumberError.VERIFICATION);
        return ResponseEntity.ok().build();
    }
}