package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ramkrushna.ragdocchat.model.DocumentChunk;
import com.ramkrushna.ragdocchat.repository.DocumentChunkRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public String search(String query, Long documentId) {
        List<Double> queryEmbeddings = googleEmbeddingService.getEmbedding(query);
        log.info("Search vector size: {}", queryEmbeddings.size());
        List<String> vectorIds = vectorStoreService.searchSimilarVectors(queryEmbeddings, 3);
        List<DocumentChunk> chunks = documentChunkRepository.findByVectorIdInAndDocumentId(
                vectorIds,
                documentId);

        // STEP 10.5 — build context
        String context = chunks.stream()
                .map(DocumentChunk::getTextChunk)
                .collect(Collectors.joining("\n\n"));

        log.info("Vector IDs from vector store: {}", vectorIds);

        // STEP 10.6 — generate answer using Gemini
        return geminiAnswerService.generateAnswer(context, query);
    }

}
