package com.ramkrushna.ragdocchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramkrushna.ragdocchat.model.DocumentChunk;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
    List<DocumentChunk> findByVectorIdIn(List<String> vectorIds);

    List<DocumentChunk> findByVectorIdInAndDocumentId(
            List<String> vectorIds,
            Long documentId
    );
}
