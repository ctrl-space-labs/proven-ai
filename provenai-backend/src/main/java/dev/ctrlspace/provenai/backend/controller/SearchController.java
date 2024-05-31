package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import dev.ctrlspace.provenai.backend.services.DataPodService;
import dev.ctrlspace.provenai.backend.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class SearchController {

    private SearchService searchService;


    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public List<SearchResult> search(@RequestBody String question) throws ProvenAiException, JsonProcessingException {
        List<SearchResult> searchResults = searchService.search(question);

        return searchResults;

    }

}
