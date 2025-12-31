package com.ramkrushna.ragdocchat.service;

import org.springframework.web.multipart.MultipartFile;

import com.ramkrushna.ragdocchat.dto.DocumentUploadResponse;

public interface DocumentService {
    DocumentUploadResponse uploadDocument(MultipartFile file);
    
}
