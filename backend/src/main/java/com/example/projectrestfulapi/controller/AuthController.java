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
import com.example.projectrestfulapi.mapper.AuthProviderMapper;
import com.example.projectrestfulapi.mapper.UserMapper;
import com.example.projectrestfulapi.service.*;
import com.example.projectrestfulapi.util.OTPMail.OtpUtil;
import com.example.projectrestfulapi.util.Security.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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
    private final AuthProviderService authProviderService;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtil jwtUtil, AccountService accountService,
                          UserService userService, EmailVerificationService emailVerificationService, AuthService authService,
                          UserDetailsService userDetailsService, StatusService statusService, AuthProviderService authProviderService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.statusService = statusService;
        this.authProviderService = authProviderService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody LoginAccountDTO.Login loginAccountDTO, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginAccountDTO.getUsername(), loginAccountDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        authService.handleSave(loginAccountDTO.getUsername(), refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Account account = accountService.handleLoginAccountLocal(loginAccountDTO.getUsername());
        User user = userService.handleFindEmailUsers(account.getUser().getEmail());
        String accountUuid = accountService.handleGetUuidByUserId(user.getId());
        List<UserLoginResponseDTO.providers> providers = AuthProviderMapper.providersMapper(authProviderService.handleFindByAccountId(account.getId()));
        UserLoginResponseDTO userResponseDTO = UserMapper.mapUserLoginAuthResponseDTO(accountUuid, user, account.getAvatar(), statusService.handleGetStatusByUuidAccount(accountUuid), providers, accessToken);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping("/google")
    public ResponseEntity<UserLoginResponseDTO> loginGoogle(@Valid @RequestBody LoginAccountDTO.LoginGoogle loginAccountDTO, HttpServletResponse response) {
        try {
            String tokenUrl = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("code", loginAccountDTO.getCode());
            params.add("grant_type", "authorization_code");
            params.add("redirect_uri", "postmessage");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            RestTemplate restTemplate = new RestTemplate();
            Map tokenResponse = restTemplate.postForObject(tokenUrl, request, Map.class);

            String accessTokenGoogle = (String) tokenResponse.get("access_token");
            String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessTokenGoogle);
            HttpEntity<String> userEntity = new HttpEntity<>(userHeaders);

            ResponseEntity<Map> userResponse = restTemplate.exchange(
                    userInfoUrl, HttpMethod.GET, userEntity, Map.class);
            Map userInfo = userResponse.getBody();

            Account account = authProviderService.handleLoginOrRegisterAccountGoogle(userInfo);
            List<GrantedAuthority> authorities = account.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());
            Authentication authentication = new UsernamePasswordAuthenticationToken(account.getUuid(), null, authorities);

            String accessToken = jwtUtil.createAccessToken(authentication);
            String refreshToken = jwtUtil.createRefreshToken(authentication);
            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            authService.handleSave(account.getUuid(), refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.handleFindEmailUsers(account.getUser().getEmail());
            String accountUuid = accountService.handleGetUuidByUserId(user.getId());
            List<UserLoginResponseDTO.providers> providers = AuthProviderMapper.providersMapper(authProviderService.handleFindByAccountId(account.getId()));
            UserLoginResponseDTO userResponseDTO = UserMapper.mapUserLoginAuthResponseDTO(accountUuid, user, account.getAvatar(),
                    statusService.handleGetStatusByUuidAccount(accountUuid), providers, accessToken);

            return ResponseEntity.ok().body(userResponseDTO);

        } catch (Exception e) {
            throw new InvalidException(NumberError.INTERNAL_SERVER_ERROR.getMessage(), NumberError.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/facebook")
    public ResponseEntity<UserLoginResponseDTO> loginFacebook(@Valid @RequestBody LoginAccountDTO.LoginFacebook loginAccountDTO, HttpServletResponse response) {

        RestTemplate restTemplate = new RestTemplate();
        String userUrl = "https://graph.facebook.com/me?fields=id,first_name,last_name,name,email,picture&access_token=" + loginAccountDTO.getAccessToken();
        Map userInfo = restTemplate.getForObject(userUrl, Map.class);

        Account account = authProviderService.handleLoginOrRegisterAccountFacebook(userInfo);
        List<GrantedAuthority> authorities = account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(account.getUuid(), null, authorities);

        String accessToken = jwtUtil.createAccessToken(authentication);
        String refreshToken = jwtUtil.createRefreshToken(authentication);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        authService.handleSave(account.getUsername(), refreshToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.handleFindEmailUsers(account.getUser().getEmail());
        String accountUuid = accountService.handleGetUuidByUserId(user.getId());
        List<UserLoginResponseDTO.providers> providers = AuthProviderMapper.providersMapper(authProviderService.handleFindByAccountId(account.getId()));
        UserLoginResponseDTO userResponseDTO = UserMapper.mapUserLoginAuthResponseDTO(accountUuid, user, account.getAvatar(),
                statusService.handleGetStatusByUuidAccount(accountUuid), providers, accessToken);

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

    @GetMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {
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
        if (!authService.handleExistsByUsername(username)) {
            throw new InvalidException(NumberError.INVALID_REFRESH_TOKEN.getMessage(), NumberError.INVALID_REFRESH_TOKEN);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.createAccessToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        return ResponseEntity.ok(tokens);
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