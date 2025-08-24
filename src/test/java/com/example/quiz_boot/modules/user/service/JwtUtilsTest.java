package com.example.quiz_boot.modules.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.quiz_boot.modules.shared.utils.JwtUtils;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set the secret using reflection to avoid database dependencies
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret",
                "testSecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void testGenerateAndValidateToken() {
        String username = "testuser";

        // Generate token
        String token = jwtUtils.generateJwtToken(username);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Validate token
        assertTrue(jwtUtils.validateJwtToken(token));

        // Extract username
        String extractedUsername = jwtUtils.getUsernameFromJwtToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}
