package org.example.tms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    private static final String ACCESS_SECRET_KEY = Base64.getEncoder()
            .encodeToString(
                    Keys.secretKeyFor(SignatureAlgorithm.HS256)
                            .getEncoded());

    private static final String REFRESH_SECRET_KEY = Base64.getEncoder()
            .encodeToString(
                    Keys.secretKeyFor(SignatureAlgorithm.HS256)
                            .getEncoded());

    private static final String USERNAME = "user@example.com";

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        jwtService.setAccessSecretKey(ACCESS_SECRET_KEY);
        jwtService.setRefreshSecretKey(REFRESH_SECRET_KEY);
    }

    @Test
    public void testGenerateAccessToken_ShouldGenerateToken() {
        when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtService.generateAccessToken(userDetails);

        assertNotNull(token);
        Claims claims = parseToken(token, ACCESS_SECRET_KEY);
        assertEquals(USERNAME, claims.getSubject());
        assertNotNull(claims.getExpiration());
    }

    @Test
    public void testGenerateRefreshToken_ShouldGenerateToken() {
        when(userDetails.getUsername()).thenReturn(USERNAME);

        String token = jwtService.generateRefreshToken(userDetails);

        assertNotNull(token);
        Claims claims = parseToken(token, REFRESH_SECRET_KEY);
        assertEquals(USERNAME, claims.getSubject());
        assertNotNull(claims.getExpiration());
    }

    @Test
    public void testIsAccessTokenValid_ShouldReturnTrue_ForValidToken() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateAccessToken(userDetails);

        boolean isValid = jwtService.isAccessTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    public void testIsRefreshTokenValid_ShouldReturnTrue_ForValidToken() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateRefreshToken(userDetails);

        boolean isValid = jwtService.isRefreshTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    public void testExtractUsername_ShouldReturnCorrectUsername() {
        when(userDetails.getUsername()).thenReturn(USERNAME);
        String token = jwtService.generateAccessToken(userDetails);

        String username = jwtService.extractUsername(token, ACCESS_SECRET_KEY);

        assertEquals(USERNAME, username);
    }

    @Test
    public void testIsTokenExpired_ShouldReturnTrue_ForExpiredToken() {
        String expiredToken = generateExpiredToken();

        boolean isExpired = jwtService.isAccessTokenValid(expiredToken);

        assertFalse(isExpired);
    }

    private Claims parseToken(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateExpiredToken() {
        Date now = new Date();
        Date expired = new Date(now.getTime() - 1000);

        return Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(ACCESS_SECRET_KEY)), SignatureAlgorithm.HS256)
                .compact();
    }
}
