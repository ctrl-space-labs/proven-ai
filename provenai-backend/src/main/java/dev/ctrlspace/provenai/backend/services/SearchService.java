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
import io.micrometer.tracing.Tracer;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SearchService {


    private DataPodService dataPodService;

    private GendoxQueryAdapter gendoxQueryAdapter;

    private OrganizationsService organizationService;

    private AuditPermissionOfUseVcService auditPermissionOfUseVcService;

    private AgentService agentService;

    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;

    private AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter;

    private Tracer tracer;


    @Autowired
    public SearchService(DataPodService dataPodService,
                         GendoxQueryAdapter gendoxQueryAdapter,
                         OrganizationsService organizationService,
                         AuditPermissionOfUseVcService auditPermissionOfUseVcService,
                         AgentService agentService,
                         AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                         Tracer tracer,
                         AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter) {
        this.dataPodService = dataPodService;
        this.gendoxQueryAdapter = gendoxQueryAdapter;
        this.organizationService = organizationService;
        this.auditPermissionOfUseVcService = auditPermissionOfUseVcService;
        this.agentService = agentService;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.agentPurposeOfUsePoliciesConverter = agentPurposeOfUsePoliciesConverter;
        this.tracer = tracer;

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

//        String searchId = tracer.currentSpan().context().traceId();
        String searchId = UUID.randomUUID().toString();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<CompletableFuture<DataPodSearchResult>> futures = new ArrayList<>();

        for (Map.Entry<String, List<UUID>> entry : dataPodsByHostUrl.entrySet()) {
            String hostUrl = entry.getKey();
            List<UUID> dataPodIds = entry.getValue();

            for (UUID dataPodId : dataPodIds) {
                CompletableFuture<DataPodSearchResult> future = CompletableFuture.supplyAsync(() -> {
                    List<DocumentInstanceSectionDTO> sections = gendoxQueryAdapter.superAdminSearch(question, dataPodId.toString(), hostUrl, 5);
                    return new DataPodSearchResult(dataPodId, sections);
                }, executorService);
                futures.add(future);
            }
        }

        // Wait for all futures to complete and fail if any future fails
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

// Collect results after all futures are done, or handle exceptions
        List<DataPodSearchResult> allSections = allOf.handle((result, ex) ->
                        futures.stream()
                                .map(CompletableFuture::join)
                                .toList())
                .join();

        List<DocumentInstanceSectionWithPodId> sortedSections = allSections.stream()
                .flatMap(dataPodSearchResult -> dataPodSearchResult.sections().stream()
                        .map(section -> new DocumentInstanceSectionWithPodId(dataPodSearchResult.dataPodId(), section)))
                .sorted(Comparator.comparing(d -> d.section().getDistanceFromQuestion()))
                .toList();

        List<DocumentInstanceSectionWithPodId> topSections = sortedSections.stream().limit(5).toList();
        List<DocumentInstanceSectionWithPodId> remainingSections = sortedSections.stream().skip(5).toList();


        for (DocumentInstanceSectionWithPodId sectionWithPodId : topSections) {
            DocumentInstanceSectionDTO section = sectionWithPodId.section();
            UUID dataPodId = sectionWithPodId.dataPodId();
            DataPod dataPod = dataPodService.getDataPodById(dataPodId);
            Organization ownerOrganization = organizationService.getOrganizationById(dataPod.getOrganizationId());

            SearchResult searchResult = createSearchResult(section, dataPod, ownerOrganization, processorOrganization, agent, searchId, policies);
            searchResults.add(searchResult);
        }


        executorService.shutdown();
        return searchResults;
    }

    private SearchResult createSearchResult(DocumentInstanceSectionDTO section,
                                            DataPod dataPod,
                                            Organization ownerOrganization,
                                            Organization processorOrganization,
                                            Agent agent,
                                            String searchId,
                                            List<Policy> policies) throws JSONException, JsonProcessingException, ProvenAiException {
        String documentId = section.getDocumentDTO().getId().toString();
        String documentSectionIscc = section.getDocumentSectionIsccCode();

        AuditPermissionOfUseVc auditPermissionOfUseVc = auditPermissionOfUseVcService.createAuditLog(
                ownerOrganization.getId(), ownerOrganization.getOrganizationDid(),
                processorOrganization.getId(),
                            processorOrganization.getOrganizationDid(),
                            agent.getId(),
                UUID.fromString(searchId),
                            dataPod.getId(),
                documentSectionIscc, section.getDocumentDTO().getDocumentIsccCode(),
                section.getTokenCount().intValue(), section.getAiModelName()
        );

        W3CVC permissionOfUseVC = auditPermissionOfUseVcService.createPermissionOfUseW3CVC(
                ownerOrganization.getOrganizationDid(),
                processorOrganization.getOrganizationDid(),
                documentSectionIscc,
                policies
        );

        String signedVCJwt = (String) auditPermissionOfUseVcService.createAgentSignedVcJwt(permissionOfUseVC, processorOrganization.getOrganizationDid());

        return SearchResult.builder()
                .documentSectionId(section.getId().toString())
                .documentId(documentId)
                .iscc(documentSectionIscc)
                .text(section.getSectionValue())
                .ownerName(processorOrganization.getName())
                .title(section.getDocumentSectionMetadata().getTitle())
                .tokens(String.valueOf(section.getTokenCount()))
                .documentURL(dataPod.getHostUrl().trim().replace("{documentId}", documentId))
                .signedPermissionOfUseVc(signedVCJwt)
                .build();
    }

    private record DataPodSearchResult(UUID dataPodId, List<DocumentInstanceSectionDTO> sections) {
    }

    private record DocumentInstanceSectionWithPodId(UUID dataPodId, DocumentInstanceSectionDTO section) {
    }


}
