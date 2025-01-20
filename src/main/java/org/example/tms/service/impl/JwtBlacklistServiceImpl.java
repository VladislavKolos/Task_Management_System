package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.service.JwtBlacklistService;
import org.example.tms.util.ConstantUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Service implementation for managing a blacklist of JWT tokens.
 * Provides functionality to blacklist tokens and check their status.
 */
@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    /**
     * Adds a JWT token to the blacklist with an expiration time.
     *
     * @param token the JWT token to blacklist
     */
    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlacklist.put(token, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(ConstantUtil.TIME_OUT));
    }

    /**
     * Checks if a JWT token is blacklisted.
     * If the token's expiration time has passed, it is removed from the blacklist.
     *
     * @param token the JWT token to check
     * @return {@code true} if the token is blacklisted, {@code false} otherwise
     */
    @Override
    public boolean isTokenBlacklisted(String token) {
        Long expirationTime = tokenBlacklist.get(token);

        if (expirationTime != null && expirationTime < System.currentTimeMillis()) {
            tokenBlacklist.remove(token);
            return false;
        }
        return expirationTime != null;
    }
}
