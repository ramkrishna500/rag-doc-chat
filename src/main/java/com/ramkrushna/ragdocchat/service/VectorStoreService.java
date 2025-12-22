package com.ramkrushna.ragdocchat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VectorStoreService {

        private final WebClient webClient;

        public VectorStoreService() {
                this.webClient = WebClient.builder()
                                .baseUrl("http://localhost:6333")
                                .build();
        }

        public String saveVector(List<Double> vector) {

                String vectorId = UUID.randomUUID().toString();

                Map<String, Object> payload = Map.of(
                                "points", List.of(
                                                Map.of(
                                                                "id", vectorId,
                                                                "vector", vector)));

                webClient.put()
                                .uri("/collections/doc_chunks/points")
                                .bodyValue(payload)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .block();

                return vectorId;
        }

        public List<String> searchSimilarVectors(List<Double> queryVector, int topK) {

                Map<String, Object> body = Map.of(
                                "vector", queryVector,
                                "limit", topK);

                Map response = webClient.post()
                                .uri("/collections/doc_chunks/points/search")
                                .bodyValue(body)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .block();

                List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("result");

                List<String> vectorIds = new ArrayList<>();
                for (Map<String, Object> r : results) {
                        vectorIds.add(r.get("id").toString());
                }
                return vectorIds;
        }
}
