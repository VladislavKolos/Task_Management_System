package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.service.JwtBlacklistService;
import org.example.tms.util.ConstantUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl implements JwtBlacklistService {

    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    @Override
    public void addTokenToBlacklist(String token) {
        tokenBlacklist.put(token, System.currentTimeMillis() + TimeUnit.HOURS.toMillis(ConstantUtil.TIME_OUT));
    }

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
