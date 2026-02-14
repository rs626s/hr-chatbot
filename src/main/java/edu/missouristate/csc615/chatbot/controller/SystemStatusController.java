package edu.missouristate.csc615.chatbot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SystemStatusController {

    @Value("${spring.application.name:HR Chatbot API}")
    private String appName;

    @Value("${spring.profiles.active:local}")
    private String environment;

    @Value("${CHROMADB_URL}")
    private String chromaUrl;

    @Value("${OLLAMA_BASE_URL}")
    private String ollamaUrl;

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    public SystemStatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public Map<String, Object> status() {

        Map<String, Object> response = new HashMap<>();
        response.put("service", appName);
        response.put("status", "running");
        response.put("message", "API is up and healthy");
        response.put("timestamp", Instant.now());

        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {

        Map<String, Object> response = new HashMap<>();
        response.put("application", appName);
        response.put("environment", environment);
        response.put("timestamp", Instant.now());

        return response;
    }

    @GetMapping("/system")
    public Map<String, Object> systemStatus() {

        Map<String, Object> response = new HashMap<>();

        response.put("application", appName);
        response.put("status", "UP");

        // Database check
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("database", "UP");
        } catch (Exception e) {
            response.put("database", "DOWN");
        }

        // ChromaDB check
        try {
            restTemplate.getForObject(chromaUrl + "/api/v2/heartbeat", String.class);
            response.put("chromadb", "UP");
        } catch (Exception e) {
            response.put("chromadb", "DOWN");
        }

        // Ollama check
        try {
            restTemplate.getForObject(ollamaUrl + "/api/tags", String.class);
            response.put("ollama", "UP");
        } catch (Exception e) {
            response.put("ollama", "DOWN");
        }

        response.put("timestamp", Instant.now());

        return response;
    }
}
