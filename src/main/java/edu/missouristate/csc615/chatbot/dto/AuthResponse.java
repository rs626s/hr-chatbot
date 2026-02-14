package edu.missouristate.csc615.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private String role;
}