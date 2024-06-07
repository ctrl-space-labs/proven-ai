package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.adapters.GendoxQueryAdapter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.DocumentDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DocumentInstanceSectionDTO;
import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SearchService {


    private DataPodService dataPodService;

    private GendoxQueryAdapter gendoxQueryAdapter;

    private OrganizationsService organizationService;


    @Autowired
    public SearchService(DataPodService dataPodService,
                         GendoxQueryAdapter gendoxQueryAdapter,
                         OrganizationsService organizationService) {
        this.dataPodService = dataPodService;
        this.gendoxQueryAdapter = gendoxQueryAdapter;
        this.organizationService = organizationService;

    }

    // TODO: authentication
    public List<SearchResult> search(String question, UserProfile agentProfile) throws Exception {
        String agentUsername = agentProfile.getUserName();
        List<DataPod> dataPods = dataPodService.getAccessibleDataPodsForAgent(agentUsername);
        Map<String, List<UUID>> dataPodsByHostUrl = dataPodService.getDataPodsByHostUrl(dataPods);

        List<SearchResult> searchResults = new ArrayList<>();

        for (Map.Entry<String, List<UUID>> entry : dataPodsByHostUrl.entrySet()) {
            String hostUrl = entry.getKey();
            List<UUID> dataPodIds = entry.getValue();

            for (UUID dataPodId : dataPodIds) {
                List<DocumentInstanceSectionDTO> sectionDetails = gendoxQueryAdapter.superAdminSearch(question, dataPodId.toString(), hostUrl, "10");
                DataPod dataPod = dataPodService.getDataPodById(dataPodId);
                Organization organization = organizationService.getOrganizationById(dataPod.getOrganizationId());

                // Map section details to SearchResult objects
                for (DocumentInstanceSectionDTO section : sectionDetails) {

                    SearchResult searchResult = SearchResult.builder()
                            .documentSectionId(section.getId().toString())
                            .documentId(section.getDocumentDTO().getId().toString())
//                            .iscc(section.getIscc())
                            .text(section.getSectionValue())
                            .ownerName(organization.getName())
                            .build();

                    searchResults.add(searchResult);
                }
            }
        }

        return searchResults;
    }


}
