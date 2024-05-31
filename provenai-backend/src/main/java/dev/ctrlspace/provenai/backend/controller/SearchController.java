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

//    @PostMapping("/search")
//    public List<SearchResult> search(@RequestParam String question, Authentication authentication) {
//        // find data pod IDs whose ACL policies match with Agents' policies
//        // get Data Pods ids
//        // REST call to gendox -> semantic search in data Pods IDs
//        // prepare Permission of use VC, with sections ISCC
//
//        UUID agentID = UUID.fromString("3432-kj453-534kl56-65b4");
//
//        // match agent's policies with data pods' policies
////        1. GET all Pods that have AgentID in the Allow List
////        2. GET all Pods that have 'Pod Permission to Use' eg Education == 'Agent's  Purpose to Use' eg Education
////        3. REMOVE all Pods that have AgentID in the Block List
////      List<DataPod> dataPods = dataPodService.getDataPods(agentID);
//
////        get list of podIDs from dataPods (per hostURL) which are the Gendox project Ids eg ids = ["424235", "65tg54", "654546"]
//
//        // Do semantic search with criteria projectIdIn = ids
//
////        Get the results and prepare the [Responce DTO, text, ISCC, list of Usage Permition, owner name], etc
//
////        return the results
//
//        return List.of();
//    }

}
