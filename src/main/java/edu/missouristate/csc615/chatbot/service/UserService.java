package edu.missouristate.csc615.chatbot.service;

import edu.missouristate.csc615.chatbot.dto.RegisterRequest;
import edu.missouristate.csc615.chatbot.entity.User;

public interface UserService {
    User registerUser(RegisterRequest request);
    User authenticateUser(String username, String password);
}