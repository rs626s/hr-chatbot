package edu.missouristate.csc615.chatbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SystemInfoController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.profiles.active:local}")
    private String environment;

    @GetMapping("/info")
    public Map<String, Object> info() {

        Map<String, Object> response = new HashMap<>();
        response.put("application", appName);
        response.put("environment", environment);
        response.put("timestamp", Instant.now());

        return response;
    }

    @GetMapping("/")
    public Map<String, Object> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "HR Chatbot API");
        response.put("status", "running");
        response.put("message", "API is up and healthy");

        return response;
    }
}