package org.example.tms.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tms.exception.custom.JwtAuthenticationException;
import org.example.tms.service.JwtBlacklistService;
import org.example.tms.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    private static final String ACCESS_TOKEN = "validAccessToken";
    private static final String USERNAME = "testUser";

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtBlacklistService jwtBlacklistService;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_InvalidToken() throws Exception {
        when(jwtService.isAccessTokenValid(ACCESS_TOKEN)).thenReturn(false);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + ACCESS_TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_BlacklistedToken() throws Exception {
        when(jwtService.isAccessTokenValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtBlacklistService.isTokenBlacklisted(ACCESS_TOKEN)).thenReturn(true);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + ACCESS_TOKEN);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(writer).write(
                "Access Denied: Blacklisted token.");
        verifyNoInteractions(filterChain);
    }


    @Test
    void testDoFilterInternal_ValidTokenAndNoAuthentication() throws Exception {
        when(jwtService.isAccessTokenValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtBlacklistService.isTokenBlacklisted(ACCESS_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(ACCESS_TOKEN, jwtService.getAccessSecretKey())).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + ACCESS_TOKEN);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = mockStatic(
                SecurityContextHolder.class)) {
            SecurityContext context = mock(SecurityContext.class);
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext)
                    .thenReturn(context); // Мокируем статический метод

            when(context.getAuthentication()).thenReturn(null);

            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);

            verify(context).setAuthentication(argThat(auth -> auth instanceof UsernamePasswordAuthenticationToken &&
                    auth.getPrincipal()
                            .equals(userDetails)));
        }
    }

    @Test
    public void testDoFilterInternal_ExceptionDuringProcessing() {
        when(jwtService.isAccessTokenValid(ACCESS_TOKEN)).thenReturn(true);
        when(jwtBlacklistService.isTokenBlacklisted(ACCESS_TOKEN)).thenReturn(false);
        when(jwtService.extractUsername(ACCESS_TOKEN, jwtService.getAccessSecretKey())).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenThrow(new RuntimeException("Unexpected Error"));
        when(request.getHeader("Authorization")).thenReturn("Bearer " + ACCESS_TOKEN);

        JwtAuthenticationException exception = assertThrows(JwtAuthenticationException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        assertEquals("Unexpected error during JWT authentication", exception.getMessage());
    }
}