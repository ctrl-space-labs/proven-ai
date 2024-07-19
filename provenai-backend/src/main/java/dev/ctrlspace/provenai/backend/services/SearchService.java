package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.adapters.GendoxQueryAdapter;
import dev.ctrlspace.provenai.backend.converters.AgentPurposeOfUsePoliciesConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.*;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.DocumentDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DocumentInstanceSectionDTO;
import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import id.walt.credentials.vc.vcs.W3CVC;
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

    private AuditPermissionOfUseVcService auditPermissionOfUseVcService;

    private AgentService agentService;

    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;

    private AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter;


    @Autowired
    public SearchService(DataPodService dataPodService,
                         GendoxQueryAdapter gendoxQueryAdapter,
                         OrganizationsService organizationService,
                         AuditPermissionOfUseVcService auditPermissionOfUseVcService,
                         AgentService agentService,
                         AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                         AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter) {
        this.dataPodService = dataPodService;
        this.gendoxQueryAdapter = gendoxQueryAdapter;
        this.organizationService = organizationService;
        this.auditPermissionOfUseVcService = auditPermissionOfUseVcService;
        this.agentService = agentService;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.agentPurposeOfUsePoliciesConverter = agentPurposeOfUsePoliciesConverter;

    }

    public List<SearchResult> search(String question, UserProfile agentProfile) throws Exception {
        String agentUsername = agentProfile.getUserName();
        Agent agent = agentService.getAgentByUsername(agentUsername);
        List<DataPod> accessibleDataPodsForAgent = dataPodService.getAccessibleDataPodsForAgent(agentUsername);

        Map<String, List<UUID>> dataPodsByHostUrl = dataPodService.getDataPodsByHostUrl(accessibleDataPodsForAgent);
        Organization processorOrganization = organizationService.getOrganizationById(agent.getOrganizationId());
        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(agent.getId());
        List<Policy> policies = new ArrayList<>();
        for (AgentPurposeOfUsePolicies agentPurposeOfUsePolicy : agentPurposeOfUsePolicies) {
            Policy policy = agentPurposeOfUsePoliciesConverter.toPolicy(agentPurposeOfUsePolicy);
            policies.add(policy);
        }

        List<SearchResult> searchResults = new ArrayList<>();

        UUID searchId = UUID.randomUUID();

        for (Map.Entry<String, List<UUID>> entry : dataPodsByHostUrl.entrySet()) {
            String hostUrl = entry.getKey();
            List<UUID> dataPodIds = entry.getValue();

            for (UUID dataPodId : dataPodIds) {
                List<DocumentInstanceSectionDTO> sectionDetails = gendoxQueryAdapter.superAdminSearch(question, dataPodId.toString(), hostUrl, "10");
                DataPod dataPod = dataPodService.getDataPodById(dataPodId);
                Organization ownerOrganization= organizationService.getOrganizationById(dataPod.getOrganizationId());

                // Map section details to SearchResult objects
                for (DocumentInstanceSectionDTO section : sectionDetails) {
                    String documentId = section.getDocumentDTO().getId().toString();
                    String documentSectionIscc = section.getDocumentSectionIsccCode();


                    AuditPermissionOfUseVc auditPermissionOfUseVc = auditPermissionOfUseVcService.createAuditLog(
                            ownerOrganization.getId(),
                            ownerOrganization.getOrganizationDid(),
                            processorOrganization.getId(),
                            processorOrganization.getOrganizationDid(),
                            agent.getId(),
                            searchId,
                            dataPodId,
                            documentSectionIscc,
                            section.getDocumentDTO().getDocumentIsccCode(),
                            section.getTokenCount().intValue(),
                            section.getAiModelName()
                    );

                    W3CVC permissionOfUseVC = auditPermissionOfUseVcService.createPermissionOfUseW3CVC(
                            ownerOrganization.getOrganizationDid(),
                            processorOrganization.getOrganizationDid(),
                            documentSectionIscc,
                            policies);

                    // Generate signed JWT for the Permission of Use VC
                    String signedVCJwt = (String) auditPermissionOfUseVcService.createAgentSignedVcJwt(permissionOfUseVC, processorOrganization.getOrganizationDid());

                    SearchResult searchResult = SearchResult.builder()
                            .documentSectionId(section.getId().toString())
                            .documentId(documentId)
                            .iscc(documentSectionIscc)
                            .text(section.getSectionValue())
                            .ownerName(processorOrganization.getName())
                            .title(section.getDocumentSectionMetadata().getTitle())
                            .tokens(String.valueOf(section.getTokenCount()))
//                            TODO: change url format when documentSectionId is added to the url
                            .documentURL(dataPod.getHostUrl().trim().replace("{documentId}", documentId))
                            .signedPermissionOfUseVc(signedVCJwt)
                            .build();

                    searchResults.add(searchResult);
                }
            }
        }

        return searchResults;
    }


}
