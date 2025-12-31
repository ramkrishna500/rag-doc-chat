package com.ramkrushna.ragdocchat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramkrushna.ragdocchat.model.Document;

public interface DocumetRepository extends JpaRepository<Document, Long> {
   
}
