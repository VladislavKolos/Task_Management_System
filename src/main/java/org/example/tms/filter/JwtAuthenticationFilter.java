package org.example.tms.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.BlacklistedTokenAccessDeniedException;
import org.example.tms.exception.JwtAuthenticationException;
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

/**
 * JWT Authentication Filter for intercepting and validating JWT tokens in HTTP requests.
 * This filter extracts the JWT token from the Authorization header, validates its authenticity
 * and sets the authentication in the Spring Security context if the token is valid.
 * It also checks if the token is blacklisted and denies access if necessary.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    /**
     * This method intercepts the HTTP request to validate the JWT token in the Authorization header.
     * If the token is valid and not blacklisted, it extracts the username and sets the authentication
     * in the Spring Security context.
     * <p>
     * If the token is invalid or blacklisted, the request proceeds without setting the authentication.
     * In case of any exceptions, an error response is sent and the error is logged.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain for the request
     */
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

            throw new JwtAuthenticationException(JwtAuthenticationException.ErrorType.JWT_AUTH_IO_ERROR, e);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            throw new JwtAuthenticationException(JwtAuthenticationException.ErrorType.JWT_AUTH_UNEXPECTED_ERROR, e);
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the HTTP request
     * @return the extracted JWT token, or null if the header is not present or does not start with "Bearer "
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    /**
     * Denies access by sending a forbidden status (HTTP 403) and an error message in the response.
     *
     * @param response the HTTP response
     */
    private void denyAccess(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter()
                    .write("Access Denied: Blacklisted token.");
        } catch (IOException e) {
            log.error("Error while denying access: {}", e.getMessage(), e);

            throw new BlacklistedTokenAccessDeniedException(e);
        }
    }

    /**
     * Sets the authentication in the Spring Security context using the provided user details.
     *
     * @param userDetails the User details object containing user information
     * @param request     the HTTP request
     */
    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext()
                .setAuthentication(authToken);
    }
}


