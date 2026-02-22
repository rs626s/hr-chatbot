package edu.missouristate.csc615.chatbot.service;

import edu.missouristate.csc615.chatbot.dto.RegisterRequest;
import edu.missouristate.csc615.chatbot.entity.User;
import edu.missouristate.csc615.chatbot.exception.InvalidCredentialsException;
import edu.missouristate.csc615.chatbot.exception.UnauthorizedException;
import edu.missouristate.csc615.chatbot.exception.UserAlreadyExistsException;
import edu.missouristate.csc615.chatbot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_success() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPasswordHash());
        assertEquals("USER", result.getRole());
        assertTrue(result.getEnabled());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowWhenUsernameExists() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        UserAlreadyExistsException exception =
                assertThrows(UserAlreadyExistsException.class,
                        () -> userService.registerUser(request));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowWhenEmailExists() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        UserAlreadyExistsException exception =
                assertThrows(UserAlreadyExistsException.class,
                        () -> userService.registerUser(request));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_success() {

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("encodedPassword");
        user.setEnabled(true);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);

        User result = userService.authenticateUser("testuser", "password");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void authenticateUser_shouldThrowWhenUserNotFound() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        InvalidCredentialsException exception =
                assertThrows(InvalidCredentialsException.class,
                        () -> userService.authenticateUser("unknown", "password"));

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void authenticateUser_shouldThrowWhenPasswordInvalid() {

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("encodedPassword");
        user.setEnabled(true);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongpass", "encodedPassword"))
                .thenReturn(false);

        InvalidCredentialsException exception =
                assertThrows(InvalidCredentialsException.class,
                        () -> userService.authenticateUser("testuser", "wrongpass"));

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void authenticateUser_shouldThrowWhenUserDisabled() {

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("encodedPassword");
        user.setEnabled(false);

        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);

        UnauthorizedException exception =
                assertThrows(UnauthorizedException.class,
                        () -> userService.authenticateUser("testuser", "password"));

        assertEquals("User account is disabled", exception.getMessage());
    }
}