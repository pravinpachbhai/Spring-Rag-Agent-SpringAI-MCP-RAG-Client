package com.pravin.railway.client;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/railway")
public class RailwayAiController {

    private final RailwayAiClient railwayAiClient;

    public RailwayAiController(RailwayAiClient railwayAiClient) {
        this.railwayAiClient = railwayAiClient;
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question must not be empty");
        }
        String answer = railwayAiClient.askRailwayQuestion(question);
        return Map.of("answer", answer);
    }

    // Read MCP notification policy directly
    @GetMapping("/resources/notification-policy")
    public Map<String, String> getNotificationPolicy() {
        String policy = railwayAiClient.getNotificationPolicy();
        return Map.of("policy", policy);
    }
}
