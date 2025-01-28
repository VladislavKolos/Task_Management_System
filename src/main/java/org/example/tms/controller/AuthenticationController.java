package org.example.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Authentication", description = "Endpoints for User authentication and token management")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtBlacklistService jwtBlacklistService;

    @Operation(
            summary = "User registration",
            description = "Registers a new User and returns an authentication response.",
            tags = {"Authentication"}
    )
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        AuthenticationResponseDto response = authenticationService.register(request);
        log.info("User registered successfully: {}", request.getEmail());

        return response;
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a User and returns an authentication response.",
            tags = {"Authentication"}
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto authenticate(
            @Valid @RequestBody AuthenticationRequestDto request) {
        AuthenticationResponseDto response = authenticationService.authenticate(request);
        log.info("User successfully authenticated");

        return response;
    }

    @Operation(
            summary = "User logout",
            description = "Logs out the User by adding the token to the blacklist.",
            tags = {"Authentication"}
    )
    @Parameter(
            name = "Authorization",
            description = "JWT token of the authenticated User",
            required = true,
            example = "Bearer <your_token>"
    )
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String token) {
        String tokenValue = token.substring(7);

        jwtBlacklistService.addTokenToBlacklist(tokenValue);
        log.info("The User has successfully logged out. Token added to blacklist");
    }

    @Operation(
            summary = "Refresh token",
            description = "Refreshes the User's JWT using a valid refresh token.",
            tags = {"Authentication"}
    )
    @Parameter(
            name = "X-Refresh-Token",
            description = "The refresh token for the User",
            required = true,
            example = "<your_refresh_token>"
    )
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken) {
        AuthenticationResponseDto response = authenticationService.refreshToken(refreshToken);
        log.info("Successfully refreshed token for User.");

        return response;
    }
}
