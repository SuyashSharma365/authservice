package com.suyash.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void shouldCreateTokenSuccessfully() {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtService.createToken(claims, "suyash");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }


    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.createToken(new HashMap<>(), "suyash");

        String username = jwtService.extractUsername(token);

        assertEquals("suyash", username);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = jwtService.createToken(new HashMap<>(), "suyash");

        var userDetails = User
                .withUsername("suyash")
                .password("password")
                .authorities("USER")
                .build();

        boolean result = jwtService.validateToken(token, userDetails);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenUsernameMismatch() {
        String token = jwtService.createToken(new HashMap<>(), "suyash");

        var userDetails = User
                .withUsername("otherUser")
                .password("password")
                .authorities("USER")
                .build();

        boolean result = jwtService.validateToken(token, userDetails);

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(invalidToken);
        });
    }


    @Test
    void shouldExtractExpirationFromToken() {
        String token = jwtService.createToken(new HashMap<>(), "suyash");

        assertNotNull(jwtService.extractExpiration(token));
    }
}