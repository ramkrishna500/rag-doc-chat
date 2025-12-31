package com.ramkrushna.ragdocchat.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ramkrushna.ragdocchat.dto.DocumentUploadResponse;
import com.ramkrushna.ragdocchat.model.Document;
import com.ramkrushna.ragdocchat.model.DocumentChunk;
import com.ramkrushna.ragdocchat.repository.DocumentChunkRepository;
import com.ramkrushna.ragdocchat.repository.DocumetRepository;
import com.ramkrushna.ragdocchat.util.TextExtractor;
import com.ramkrushna.ragdocchat.util.TextSplitter;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumetRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;

    private final VectorStoreService vectorStoreService;

    // If OpenAI API keys available then usefull.
    private final EmbeddingService embeddingService;

    // For google Gemini
    private final GoogleEmbeddingService googleEmbeddingService;

    public DocumentServiceImpl(DocumetRepository documentRepository, DocumentChunkRepository documentChunkRepository,
            EmbeddingService embeddingService, VectorStoreService vectorStoreService,
            GoogleEmbeddingService googleEmbeddingService) {
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.embeddingService = embeddingService;
        this.vectorStoreService = vectorStoreService;
        this.googleEmbeddingService = googleEmbeddingService;
    }

    @Override
    public DocumentUploadResponse uploadDocument(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFileType(getFileType(file.getOriginalFilename()));

        Document savedDocument = documentRepository.save(document);

        String extractedText = TextExtractor.extractText(file);

        if (extractedText.length() > 1_500_000) {
            throw new IllegalArgumentException("Document too large for in-memory chunking");
        }

        AtomicInteger chunkCount = new AtomicInteger(0);
        TextSplitter.splitAndConsume(extractedText, chunkText -> {
            try {
                // OpenAI
                // List<Double> embeddings = embeddingService.getEmbeddings(chunkText);

                List<Double> embeddings = googleEmbeddingService.getEmbedding(chunkText);
                String vectorId = vectorStoreService.saveVector(embeddings);

                DocumentChunk chunk = new DocumentChunk();
                chunk.setDocument(savedDocument);
                chunk.setTextChunk(chunkText);
                chunk.setVectorId(vectorId);
                documentChunkRepository.save(chunk);
                chunkCount.incrementAndGet();

                Thread.sleep(300); // Add a small delay to avoid rate limiting from OpenAI

            } catch (Exception e) {
                throw new RuntimeException("Embedding failed", e);
            }
        });

        System.out.println("Saved " + chunkCount.get() + " chunks for documentId="
                + savedDocument.getId());

        return new DocumentUploadResponse(savedDocument.getId(), "Document uploaded successfully");
    }

    private String getFileType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return "PDF";
        } else if (fileName.endsWith(".doc")) {
            return "DOC";
        } else if (fileName.endsWith(".docx")) {
            return "DOCX";
        } else {
            return "UNKNOWN";
        }
    }
}
