package edu.missouristate.csc615.chatbot.repository;

import edu.missouristate.csc615.chatbot.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUsername should return user when username exists")
    void findByUsername_success() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");

        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @DisplayName("findByUsername should return empty when user does not exist")
    void findByUsername_notFound() {

        Optional<User> result = userRepository.findByUsername("unknown");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("existsByUsername should return true when username exists")
    void existsByUsername_true() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("testuser");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail should return true when email exists")
    void existsByEmail_true() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByUsername should return false when username does not exist")
    void existsByUsername_false() {

        boolean exists = userRepository.existsByUsername("unknown");

        assertFalse(exists);
    }
}