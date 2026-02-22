package edu.missouristate.csc615.chatbot.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "myVerySecureSecretKeyForJWTTokensThatIsAtLeast256BitsLong");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testuser");

        assertNotNull(token);
        assertTrue(token.length() > 20);
        assertTrue(token.startsWith("eyJ"));  // JWT starts with eyJ
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testuser");
        String username = jwtUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testuser");
        Boolean isValid = jwtUtil.validateToken(token, "testuser");

        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWrongUser() {
        String token = jwtUtil.generateToken("testuser");
        Boolean isValid = jwtUtil.validateToken(token, "wronguser");

        assertFalse(isValid);
    }
}