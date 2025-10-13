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
import com.example.projectrestfulapi.service.AccountService;
import com.example.projectrestfulapi.service.EmailVerificationService;
import com.example.projectrestfulapi.service.UserService;
import com.example.projectrestfulapi.util.OTPMail.OtpUtil;
import com.example.projectrestfulapi.util.Security.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil, AccountService accountService, UserService userService, EmailVerificationService emailVerificationService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken() {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody LoginAccountDTO loginAccountDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginAccountDTO.getUsername(), loginAccountDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = accountService.handleLoginAccount(loginAccountDTO.getUsername());
        User user = userService.handleFindEmailUsers(account.getUser().getEmail());
        UserLoginResponseDTO userResponseDTO = UserMapper.mapUserLoginAuthResponseDTO(accountService.handleGetUuid(user.getId()), user, accessToken, refreshToken);
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
        if (!emailVerificationService.handleVerification(verificationEmail.getEmail(), OtpUtil.generateOTP()))
            throw new InvalidException(NumberError.VERIFICATION.getMessage(), NumberError.VERIFICATION);
        return ResponseEntity.ok().build();
    }
}