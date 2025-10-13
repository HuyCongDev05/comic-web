package com.example.projectrestfulapi.util.Security;

import com.example.projectrestfulapi.exception.InvalidException;
import com.example.projectrestfulapi.exception.NumberError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.Instant;

@Component
public class JwtUtil {
    public static final MacAlgorithm macAlgorithm = MacAlgorithm.HS256;
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenLifeTime;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenLifeTime;

    @Value("${jwt.base64-secret}")
    private String jwtKey;

    public JwtUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plusSeconds(accessTokenLifeTime);
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("authorities", authentication)
                .claim("type", "access_token")
                .build();
        JwsHeader jwsHeader = JwsHeader.with(macAlgorithm).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }
    public String createRefreshToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plusSeconds(refreshTokenLifeTime);
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("authorities", authentication)
                .claim("type", "refresh_token")
                .build();
        JwsHeader jwsHeader = JwsHeader.with(macAlgorithm).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw new InvalidException("Expired signature", NumberError.UNAUTHORIZED);
        } catch (RuntimeException e) {
            throw new InvalidException("Invalid signature", NumberError.UNAUTHORIZED);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (!username.equals(userDetails.getUsername())) {
            throw new InvalidException("Signature has been modified", NumberError.UNAUTHORIZED);
        }
        return true;
    }
}
