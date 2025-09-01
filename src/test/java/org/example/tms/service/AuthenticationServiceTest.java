package org.example.tms.service;

import org.example.tms.dto.requests.AuthenticationRequestDto;
import org.example.tms.dto.requests.RegisterRequestDto;
import org.example.tms.dto.responses.AuthenticationResponseDto;
import org.example.tms.exception.InvalidRefreshTokenException;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "password123";
    private static final String REFRESH_TOKEN = "refreshToken";

    private static final User user = User.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .role(UserRole.ROLE_USER)
            .createdAt(LocalDateTime.now())
            .build();
    private static final AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
    private static final RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
            .email(EMAIL)
            .password(PASSWORD)
            .build();
    private static final AuthenticationResponseDto authenticationResponseDto = AuthenticationResponseDto.builder()
            .token("accessToken")
            .refreshToken("refreshToken")
            .build();

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void testRegister_Success() {

        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        doNothing().when(userService)
                .save(argThat(user -> user.getEmail()
                        .equals(EMAIL) && user.getPassword()
                        .equals(PASSWORD)));
        when(jwtService.generateAccessToken(eq(user))) // заменили any() на eq(user)
                .thenReturn("accessToken");
        when(jwtService.generateRefreshToken(eq(user))) // заменили any() на eq(user)
                .thenReturn("refreshToken");

        AuthenticationResponseDto result = authenticationService.register(registerRequestDto);

        assertEquals(authenticationResponseDto, result);
        verify(userService).save(argThat(user -> user.getEmail()
                .equals(EMAIL) && user.getPassword()
                .equals(PASSWORD)));
        verify(jwtService).generateAccessToken(eq(user)); // проверяем с конкретным объектом
        verify(jwtService).generateRefreshToken(eq(user)); // аналогично
    }

    @Test
    public void testAuthenticate_Success() {
        when(authenticationManager.authenticate(argThat(argument ->
                argument instanceof UsernamePasswordAuthenticationToken &&
                        argument.getPrincipal()
                                .equals(EMAIL) &&
                        argument.getCredentials()
                                .equals(PASSWORD))))
                .thenReturn(mock(Authentication.class)); // Mock successful authentication
        when(userService.getUserByEmail(EMAIL)).thenReturn(user);
        when(jwtService.generateAccessToken(eq(user))) // заменили any() на eq(user)
                .thenReturn("accessToken");
        when(jwtService.generateRefreshToken(eq(user))) // заменили any() на eq(user)
                .thenReturn("refreshToken");

        AuthenticationResponseDto result = authenticationService.authenticate(authenticationRequestDto);

        assertEquals(authenticationResponseDto, result);
        verify(authenticationManager).authenticate(argThat(argument ->
                argument instanceof UsernamePasswordAuthenticationToken &&
                        argument.getPrincipal()
                                .equals(EMAIL) &&
                        argument.getCredentials()
                                .equals(PASSWORD)));

        verify(userService).getUserByEmail(EMAIL);
        verify(jwtService).generateAccessToken(eq(user)); // проверяем с конкретным объектом
        verify(jwtService).generateRefreshToken(eq(user)); // аналогично
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        when(authenticationManager.authenticate(argThat(argument ->
                argument instanceof UsernamePasswordAuthenticationToken &&
                        argument.getPrincipal()
                                .equals(EMAIL) &&
                        argument.getCredentials()
                                .equals(PASSWORD))))
                .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(authenticationRequestDto));
        verify(authenticationManager).authenticate(argThat(argument ->
                argument instanceof UsernamePasswordAuthenticationToken &&
                        argument.getPrincipal()
                                .equals(EMAIL) &&
                        argument.getCredentials()
                                .equals(PASSWORD)));
    }

    @Test
    public void testRefreshToken_Success() {
        when(jwtService.getRefreshSecretKey()).thenReturn(
                "yourMockedSecretKey"); // Замокайте возвращаемое значение для getRefreshSecretKey

        when(jwtService.extractUsername(eq(REFRESH_TOKEN),
                eq("yourMockedSecretKey"))) // Используйте замоканное значение
                .thenReturn(EMAIL);
        when(jwtService.isRefreshTokenValid(eq(REFRESH_TOKEN))).thenReturn(true);
        when(userService.getUserByEmail(eq(EMAIL))).thenReturn(user);
        when(jwtService.generateAccessToken(eq(user))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(eq(user))).thenReturn("refreshToken");

        AuthenticationResponseDto result = authenticationService.refreshToken(REFRESH_TOKEN);

        assertEquals(authenticationResponseDto, result);
        verify(jwtService).extractUsername(eq(REFRESH_TOKEN), eq("yourMockedSecretKey"));
        verify(jwtService).isRefreshTokenValid(eq(REFRESH_TOKEN));
        verify(userService).getUserByEmail(eq(EMAIL));
        verify(jwtService).generateAccessToken(eq(user));
        verify(jwtService).generateRefreshToken(eq(user));
    }


    @Test
    public void testRefreshToken_Invalid() {
        when(jwtService.isRefreshTokenValid(eq(REFRESH_TOKEN))).thenReturn(false);

        assertThrows(InvalidRefreshTokenException.class, () -> authenticationService.refreshToken(REFRESH_TOKEN));
        verify(jwtService).isRefreshTokenValid(eq(REFRESH_TOKEN));
    }
}