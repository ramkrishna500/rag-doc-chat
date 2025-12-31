package com.ramkrushna.ragdocchat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ramkrushna.ragdocchat.dto.DocumentUploadResponse;
import com.ramkrushna.ragdocchat.service.DocumentService;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://rag-doc-chat-frontend.pages.dev/"
})
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        DocumentUploadResponse response = documentService.uploadDocument(file);
        return ResponseEntity.ok(response);
    }
}
