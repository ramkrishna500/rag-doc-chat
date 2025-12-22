package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GoogleEmbeddingService {

    private final WebClient webClient;

    public GoogleEmbeddingService(
            @Value("${google.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    public List<Double> getEmbedding(String text) {

        Map<String, Object> body = Map.of(
                "content", Map.of(
                        "parts", List.of(
                                Map.of("text", text))));

        Map response = webClient.post()
                .uri("/models/text-embedding-004:embedContent")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map embedding = (Map) response.get("embedding");
        return (List<Double>) embedding.get("values");
    }
}
