package edu.missouristate.csc615.chatbot.controller;

import edu.missouristate.csc615.chatbot.dto.AuthResponse;
import edu.missouristate.csc615.chatbot.dto.LoginRequest;
import edu.missouristate.csc615.chatbot.dto.RegisterRequest;
import edu.missouristate.csc615.chatbot.entity.User;
import edu.missouristate.csc615.chatbot.security.JwtTokenProvider;
import edu.missouristate.csc615.chatbot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);
        String token = jwtTokenProvider.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = userService.authenticateUser(
                request.getUsername(),
                request.getPassword()
        );

        String token = jwtTokenProvider.generateToken(user.getUsername());
        AuthResponse response = new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.status(201).body(response);
    }
}