package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import dev.ctrlspace.provenai.backend.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private SearchService searchService;



    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public List<SearchResult> search(@RequestBody String question, Authentication authentication) throws Exception {

        UserProfile agentProfile = (UserProfile) authentication.getPrincipal();
        List<SearchResult> searchResults = searchService.search(question, agentProfile);

        return searchResults;

    }

}
