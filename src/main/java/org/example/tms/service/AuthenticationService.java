package org.example.tms.service;

import lombok.RequiredArgsConstructor;
import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.dto.responses.AuthenticationResponseDto;
import org.example.tms.exception.custom.InvalidRefreshTokenException;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for handling User authentication and registration.
 * Provides methods for registering a User, authenticating a User and refreshing authentication tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new User and generates authentication tokens.
     *
     * @param request the {@link RegisterRequestDto} containing User registration details
     * @return the {@link AuthenticationResponseDto} containing the generated tokens
     */
    @Transactional
    public AuthenticationResponseDto register(RegisterRequestDto request) {
        User user = buildUser(request);
        userService.save(user);

        return createAuthenticationResponse(user);
    }

    /**
     * Authenticates a User by email and password and generates authentication tokens.
     *
     * @param request the {@link AuthenticationRequestDto} containing login details
     * @return the {@link AuthenticationResponseDto} containing the generated tokens
     */
    @Transactional
    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userService.getUserByEmail(request.getEmail());

        return createAuthenticationResponse(user);
    }

    /**
     * Refreshes the authentication tokens using a valid refresh token.
     *
     * @param refreshToken the refresh token to validate and use for generating new tokens
     * @return the {@link AuthenticationResponseDto} containing the new tokens
     * @throws InvalidRefreshTokenException if the refresh token is invalid
     */
    @Transactional
    public AuthenticationResponseDto refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken, jwtService.getRefreshSecretKey());
        User user = userService.getUserByEmail(username);

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        return createAuthenticationResponse(user);
    }

    /**
     * Creates authentication response containing access and refresh tokens.
     *
     * @param user the authenticated {@link User} to generate tokens for
     * @return the {@link AuthenticationResponseDto} containing access and refresh tokens
     */
    private AuthenticationResponseDto createAuthenticationResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Builds a new {@link User} entity from the registration request DTO.
     *
     * @param request the {@link RegisterRequestDto} containing User details
     * @return the constructed {@link User} entity
     */
    private User buildUser(RegisterRequestDto request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
