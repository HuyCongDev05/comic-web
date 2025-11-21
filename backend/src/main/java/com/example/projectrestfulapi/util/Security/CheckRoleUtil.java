package com.example.projectrestfulapi.util.Security;

import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CheckRoleUtil {
    private final JwtUtil jwtUtil;

    public CheckRoleUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void checkRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidException(NumberError.VERIFICATION.getMessage(), NumberError.VERIFICATION);
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.extractRole(token).equals("ADMIN")) throw new InvalidException(NumberError.VERIFICATION.getMessage(), NumberError.VERIFICATION);
    }
}
