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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthenticationResponseDto authResponseDTO = authenticationService.register(request);

        log.info("User registered successfully: {}", request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @Valid @RequestBody AuthenticationRequestDto request) {
        AuthenticationResponseDto authResponseDTO = authenticationService.authenticate(request);

        log.info("User successfully authenticated");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String tokenValue = token.substring(7);

        jwtBlacklistService.addTokenToBlacklist(tokenValue);

        log.info("The User has successfully logged out. Token added to blacklist");

        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken) {
        AuthenticationResponseDto authResponseDTO = authenticationService.refreshToken(refreshToken);

        log.info("Successfully refreshed token for User.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authResponseDTO);
    }
}
