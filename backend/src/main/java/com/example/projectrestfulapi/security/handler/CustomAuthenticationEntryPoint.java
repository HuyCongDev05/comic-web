package com.example.projectrestfulapi.security.handler;

import com.example.projectrestfulapi.dto.response.FormatResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        this.authenticationEntryPoint.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");
        FormatResponseDTO<Void> responseFormatDTO = new FormatResponseDTO<>();
        responseFormatDTO.setSuccess(false);
        responseFormatDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
        String message = Optional.ofNullable(authException.getCause())
                .map(msg -> "Authentication Failed")
                .orElse("Invalid signature");
        responseFormatDTO.setMessage(message);
        objectMapper.writeValue(response.getWriter(), responseFormatDTO);
    }
}
