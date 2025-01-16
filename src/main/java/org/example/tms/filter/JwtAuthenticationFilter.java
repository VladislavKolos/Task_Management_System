package org.example.tms.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.custom.BlacklistedTokenAccessDeniedException;
import org.example.tms.exception.custom.JwtAuthenticationException;
import org.example.tms.service.JwtBlacklistService;
import org.example.tms.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        try {
            String accessToken = extractToken(request);

            if (accessToken == null || !jwtService.isAccessTokenValid(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtBlacklistService.isTokenBlacklisted(accessToken)) {
                denyAccess(response);
                return;
            }

            String username = jwtService.extractUsername(accessToken, jwtService.getAccessSecretKey());
            if (username != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                setAuthentication(userDetails, request);
            }

            filterChain.doFilter(request, response);
        } catch (IOException e) {
            log.error("I/O Error during JWT authentication: {}", e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            throw new JwtAuthenticationException("I/O Error during JWT authentication", e);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            throw new JwtAuthenticationException("Unexpected error during JWT authentication", e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    private void denyAccess(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter()
                    .write("Access Denied: Blacklisted token.");
        } catch (IOException e) {
            log.error("Error while denying access: {}", e.getMessage(), e);

            throw new BlacklistedTokenAccessDeniedException("Error while denying access due to blacklisted token", e);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext()
                .setAuthentication(authToken);
    }
}


