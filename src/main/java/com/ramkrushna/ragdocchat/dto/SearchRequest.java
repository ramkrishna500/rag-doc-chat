package com.ramkrushna.ragdocchat.dto;

public class SearchRequest {

    private String query;
    private Long documentId;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}

