package com.ramkrushna.ragdocchat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramkrushna.ragdocchat.dto.SearchRequest;
import com.ramkrushna.ragdocchat.service.SearchService;

@RestController
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public String search(@RequestBody SearchRequest query) {
        return searchService.search(query.getQuery());
    }

}
