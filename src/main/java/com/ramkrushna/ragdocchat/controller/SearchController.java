package com.ramkrushna.ragdocchat.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramkrushna.ragdocchat.dto.SearchRequest;
import com.ramkrushna.ragdocchat.service.SearchService;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://rag-doc-chat-frontend.pages.dev/"
})
@RestController
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public String search(@RequestBody SearchRequest request) {

        if (request.getDocumentId() == null) {
            throw new IllegalArgumentException("documentId is required");
        }

        return searchService.search(
                request.getQuery(),
                request.getDocumentId());
    }

}
