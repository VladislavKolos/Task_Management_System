package org.example.tms.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.custom.SecurityFilterConfigurationException;
import org.example.tms.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth ->
                            auth.requestMatchers("/api/auth/**")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.POST, "/api/tasks")
                                    .hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.DELETE, "/api/tasks/**")
                                    .hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/tasks/**")
                                    .authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/tasks/**")
                                    .authenticated()
                                    .requestMatchers(HttpMethod.POST, "/api/tasks-assignees/**")
                                    .hasRole("ADMIN")
                                    .requestMatchers(HttpMethod.GET, "/api/comments/**")
                                    .authenticated()
                                    .requestMatchers(HttpMethod.POST, "/api/comments")
                                    .authenticated()
                                    .requestMatchers(HttpMethod.DELETE, "/api/comments/**")
                                    .authenticated()
                                    .anyRequest()
                                    .authenticated())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        } catch (Exception e) {
            log.error("Error configuring security filter chain: {}", e.getMessage(), e);

            throw new SecurityFilterConfigurationException("Error configuring security filter chain", e);
        }
    }
}
