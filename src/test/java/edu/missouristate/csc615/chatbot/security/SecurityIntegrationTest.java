package edu.missouristate.csc615.chatbot.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testPasswordEncoding() {
        String rawPassword = "mypassword123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded);
        assertTrue(encoded.startsWith("$2a$"));  // BCrypt prefix
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    void testJwtGeneration() {
        String token = jwtUtil.generateToken("testuser");

        assertNotNull(token);
        assertEquals("testuser", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token, "testuser"));
    }
}