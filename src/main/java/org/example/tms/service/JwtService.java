package org.example.tms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.example.tms.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for managing JSON Web Tokens (JWTs) for User authentication and authorization.
 * Provides methods to generate, validate and extract claims from access and refresh tokens.
 */
@Getter
@Setter
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String accessSecretKey;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretKey;

    /**
     * Generates an access token for the provided User details.
     *
     * @param userDetails the {@link UserDetails} of the User to generate the token for
     * @return the generated JWT access token
     */
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ConstantUtil.ACCESS_TOKEN_EXPIRATION, accessSecretKey);
    }

    /**
     * Generates a refresh token for the provided User details.
     *
     * @param userDetails the {@link UserDetails} of the User to generate the token for
     * @return the generated JWT refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, ConstantUtil.REFRESH_TOKEN_EXPIRATION, refreshSecretKey);
    }

    /**
     * Validates if the provided access token is valid.
     *
     * @param token the access token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, accessSecretKey);
    }

    /**
     * Validates if the provided refresh token is valid.
     *
     * @param token the refresh token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean isRefreshTokenValid(String token) {
        return isTokenValid(token, refreshSecretKey);
    }

    /**
     * Extracts the username (subject) from the token.
     *
     * @param token the JWT token
     * @param key   the secret key to verify the token
     * @return the username (subject) from the token
     */
    public String extractUsername(String token, String key) {
        return extractClaim(token, Claims::getSubject, key);
    }

    /**
     * Checks if the provided token is valid by verifying its expiration.
     *
     * @param token the JWT token to validate
     * @param key   the secret key to verify the token
     * @return {@code true} if the token is valid, {@code false} if expired
     */
    private boolean isTokenValid(String token, String key) {
        return !isTokenExpired(token, key);
    }

    /**
     * Checks if the token has expired.
     *
     * @param token the JWT token to check
     * @param key   the secret key to verify the token
     * @return {@code true} if the token has expired, {@code false} otherwise
     */
    private boolean isTokenExpired(String token, String key) {
        try {
            return extractExpiration(token, key).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token the JWT token
     * @param key   the secret key to verify the token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token, String key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    /**
     * Extracts a specific claim from the token.
     *
     * @param token          the JWT token
     * @param claimsResolver the function to extract the claim
     * @param key            the secret key to verify the token
     * @param <T>            the type of the claim
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String key) {
        Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token the JWT token
     * @param key   the secret key to verify the token
     * @return the claims from the token
     */
    private Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Returns the signing key derived from the provided key.
     *
     * @param key the secret key used for signing the JWT
     * @return the key used to sign the JWT
     */
    private Key getSignInKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a token with the specified claims and expiration time.
     *
     * @param extraClaims      additional claims to add to the token
     * @param userDetails      the {@link UserDetails} of the User
     * @param expirationMillis the expiration time of the token in milliseconds
     * @param key              the secret key to sign the token
     * @return the generated JWT token
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, int expirationMillis,
                                 String key) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignInKey(key), SignatureAlgorithm.HS256)
                .compact();
    }
}

