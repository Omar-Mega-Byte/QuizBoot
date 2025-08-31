package com.example.quiz_boot.modules.shared.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Quiz Boot application
 * Configures authentication, authorization, JWT, and password encoding
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for REST API (enable if using forms)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to authentication endpoints
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Allow public access to categories (needed for quiz creation forms)
                        .requestMatchers("/api/categories", "/api/categories/all").permitAll()

                        // Allow public access to GET quizzes (for testing - should be secured in
                        // production)
                        .requestMatchers(HttpMethod.GET, "/api/quizzes", "/api/quizzes/**").permitAll()

                        // Allow public access to health check and documentation
                        .requestMatchers("/actuator/health", "/api-docs/**", "/swagger-ui/**").permitAll()

                        // Require authentication for all other endpoints
                        .anyRequest().authenticated())

                // Configure session management (stateless for REST API)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                org.springframework.security.config.http.SessionCreationPolicy.STATELESS))

                // Add JWT filter
                .addFilterBefore(jwtAuthenticationEntryPoint, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
