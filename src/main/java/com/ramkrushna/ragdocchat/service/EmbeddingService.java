package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@ConditionalOnProperty(name = "openai.enabled", havingValue = "true")
public class EmbeddingService {
        private final WebClient webClient;

        public EmbeddingService(
                        @Value("${openai.api.key}") String apiKey) {
                this.webClient = WebClient.builder()
                                .baseUrl("https://api.openai.com/v1")
                                .defaultHeader("Authorization", "Bearer " + apiKey)
                                .defaultHeader("Content-Type", "application/json")
                                .build();
        }

        public List<Double> getEmbeddings(String text) {
                Map<String, Object> requestBody = Map.of(
                                "model", "text-embedding-3-small",
                                "input", text);
                Map response = webClient.post()
                                .uri("/embeddings")
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .block();

                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                return (List<Double>) data.get(0).get("embedding");
        }
}
