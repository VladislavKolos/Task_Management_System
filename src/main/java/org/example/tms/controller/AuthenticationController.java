package org.example.tms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.dto.responses.AuthenticationResponseDto;
import org.example.tms.service.AuthenticationService;
import org.example.tms.service.JwtBlacklistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        log.info("Incoming request for user registration. Email: {}", request.getEmail());

        AuthenticationResponseDto response = authenticationService.register(request);
        log.info("User registered successfully: {}", request.getEmail());

        return response;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(
            @Valid @RequestBody AuthenticationRequestDto request) {
        log.info("Incoming authentication request for email: {}", request.getEmail());

        AuthenticationResponseDto response = authenticationService.authenticate(request);
        log.info("User successfully authenticated");

        return response;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String token) {
        log.info("Incoming logout request. Token: {}", token.substring(0, 7) + "...");

        String tokenValue = token.substring(7);

        jwtBlacklistService.addTokenToBlacklist(tokenValue);
        log.info("The User has successfully logged out. Token added to blacklist");
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken) {
        log.info("Incoming refresh token request. Token: {}", refreshToken.substring(0, 7) + "...");

        AuthenticationResponseDto response = authenticationService.refreshToken(refreshToken);
        log.info("Successfully refreshed token for User.");

        return response;
    }
}
