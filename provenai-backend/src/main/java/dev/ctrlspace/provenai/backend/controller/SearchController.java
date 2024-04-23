package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {


    @PostMapping("/search")
    public List<SearchResult> search(@RequestParam String q, Authentication authentication) {
        return List.of();
    }

}
