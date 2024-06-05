package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.adapters.GendoxQueryAdapter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SearchService {


    private DataPodService dataPodService;

    private GendoxQueryAdapter gendoxQueryAdapter;


    @Autowired
    public SearchService(DataPodService dataPodService,
                         GendoxQueryAdapter gendoxQueryAdapter) {
        this.dataPodService = dataPodService;
        this.gendoxQueryAdapter = gendoxQueryAdapter;
    }

// TODO: authentication
    public List<SearchResult> search(String question, UserProfile agentProfile) throws ProvenAiException, JsonProcessingException {
        UUID agentUserId = UUID.fromString(agentProfile.getId());
        String agentName = agentProfile.getName();

        // Step 1: Get Matching Projects from AgentId
        List<DataPod> dataPods = dataPodService.getMatchingAgentPolicyDataPods(agentName);
        // Step 2: Group ProjectIdIn by HostURL
        Map<String, List<UUID>>  dataPodsByHostUrl  = dataPodService.getDataPodsByHostUrl(dataPods);
        //TODO  Step 3: Gendox Search
        // List<SearchResults> searchResults =  semanticSearchgendox
        String searchResultJson = gendoxQueryAdapter.superAdminSearch(question, "4fd12adf-763b-4d17-a72b-df9f71b50e0d", "10");

//        return searchResults;

        return null;
    }


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
////        1. GET all Pods that have AgentID in the Allow List check
////        2. GET all Pods that have 'Pod Permission to Use' eg Education == 'Agent's  Purpose to Use' eg Education check
////        3. REMOVE all Pods that have AgentID in the Block List check
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