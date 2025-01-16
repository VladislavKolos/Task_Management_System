package org.example.tms.service;

import org.springframework.stereotype.Component;

@Component
public interface JwtBlacklistService {
    void addTokenToBlacklist(String token);

    boolean isTokenBlacklisted(String token);
}
