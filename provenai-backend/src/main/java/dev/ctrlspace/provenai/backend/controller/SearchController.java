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
    public List<SearchResult> search(@RequestParam String question, Authentication authentication) {
        // find data pod IDs whose ACL policies match with Agents' policies
        // get Data Pods ids
        // REST call to gendox -> semantic search in data Pods IDs
        // prepare Permission of use VC, with sections ISCC
        return List.of();
    }

}
