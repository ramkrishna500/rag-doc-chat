package com.ramkrushna.ragdocchat.dto;

public class DocumentUploadResponse {
    private Long documentId;
    private String message;

    public DocumentUploadResponse(Long documentId, String message) {
        this.documentId = documentId;
        this.message = message;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public String getMessage() {
        return message;
    }
}
