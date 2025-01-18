package org.example.tms.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtBlacklistServiceImplTest {
    private JwtBlacklistServiceImpl jwtBlacklistService;

    @BeforeEach
    public void setUp() {
        jwtBlacklistService = new JwtBlacklistServiceImpl();
    }

    @Test
    public void testAddTokenToBlacklist() {
        String token = "testToken";

        jwtBlacklistService.addTokenToBlacklist(token);

        assertTrue(jwtBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    public void testIsTokenBlacklisted_ReturnsTrueForBlacklistedToken() {
        String token = "testToken";

        jwtBlacklistService.addTokenToBlacklist(token);

        assertTrue(jwtBlacklistService.isTokenBlacklisted(token));
    }

    @Test
    public void testIsTokenBlacklisted_ReturnsFalseForNonBlacklistedToken() {
        String token = "nonBlacklistedToken";

        assertFalse(jwtBlacklistService.isTokenBlacklisted(token));
    }
}