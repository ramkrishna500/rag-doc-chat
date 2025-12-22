package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiAnswerService {

    private final WebClient webClient;

    public GeminiAnswerService(@Value("${google.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    public String generateAnswer(String context, String question) {

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of(
                                                "text",
                                                "Answer the question using the context below.\n\n"
                                                        + "Context:\n" + context
                                                        + "\n\nQuestion:\n" + question)))));

        Map response = webClient.post()
                .uri("/models/gemini-2.5-flash:generateContent")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");

        return ((Map) ((List) ((Map) candidates.get(0)
                .get("content")).get("parts")).get(0))
                .get("text").toString();
    }
}
