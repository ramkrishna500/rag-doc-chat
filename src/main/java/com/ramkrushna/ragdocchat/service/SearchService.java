package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ramkrushna.ragdocchat.model.DocumentChunk;
import com.ramkrushna.ragdocchat.repository.DocumentChunkRepository;

@Service
public class SearchService {

    private final GoogleEmbeddingService googleEmbeddingService;
    private final VectorStoreService vectorStoreService;
    private final DocumentChunkRepository documentChunkRepository;
    private final GeminiAnswerService geminiAnswerService;

    public SearchService(GoogleEmbeddingService googleEmbeddingService,
            VectorStoreService vectorStoreService,
            DocumentChunkRepository documentChunkRepository,
            GeminiAnswerService geminiAnswerService) {
        this.googleEmbeddingService = googleEmbeddingService;
        this.vectorStoreService = vectorStoreService;
        this.documentChunkRepository = documentChunkRepository;
        this.geminiAnswerService = geminiAnswerService;
    }

    public String search(String query) {
        List<Double> queryEmbeddings = googleEmbeddingService.getEmbedding(query);
        List<String> vectorIds = vectorStoreService.searchSimilarVectors(queryEmbeddings, 3);
        List<DocumentChunk> chunks = documentChunkRepository.findByVectorIdIn(vectorIds);

        // STEP 10.5 — build context
        String context = chunks.stream()
                .map(DocumentChunk::getTextChunk)
                .collect(Collectors.joining("\n\n"));

        // STEP 10.6 — generate answer using Gemini
        return geminiAnswerService.generateAnswer(context, query);
    }

}
