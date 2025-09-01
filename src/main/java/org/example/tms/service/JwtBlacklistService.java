package org.example.tms.service;

public interface JwtBlacklistService {
    void addTokenToBlacklist(String token);

    boolean isTokenBlacklisted(String token);
}
