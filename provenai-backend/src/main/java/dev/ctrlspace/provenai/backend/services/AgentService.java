package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.adapters.GendoxWebHookAdapter;
import dev.ctrlspace.provenai.backend.authentication.KeycloakAuthenticationService;
import dev.ctrlspace.provenai.backend.converters.AgentConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.AgentPublicDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentDTO;
import dev.ctrlspace.provenai.backend.model.dtos.EventPayloadDTO;
import dev.ctrlspace.provenai.backend.model.dtos.WebHookEventResponse;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.repositories.AgentRepository;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.AgentPredicates;
import dev.ctrlspace.provenai.backend.utils.JWTUtils;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.dto.IssuerKey;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import dev.ctrlspace.provenai.utils.SSIJWTUtils;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.jwk.JWKKey;
import jakarta.annotation.Nullable;
import org.json.JSONException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class AgentService {

    private AgentRepository agentRepository;

    private OrganizationRepository organizationRepository;

    private CredentialIssuanceApi credentialIssuanceApi;

    private KeycloakAuthenticationService keycloakAuthenticationService;

    @Value("${proven-ai.ssi.issuer-did}")
    private String issuerDid;

    @Value("${proven-ai.ssi.issuer-private-jwk}")
    private String issuerPrivateJwk;


    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;

    private PolicyTypeRepository policyTypeRepository;
    private AgentConverter agentConverter;

    private SSIJWTUtils ssiJwtUtils;

    private GendoxWebHookAdapter gendoxWebHookAdapter;

    private JWTUtils jwtUtils;


    @Autowired
    public AgentService(AgentRepository agentRepository,
                        CredentialIssuanceApi credentialIssuanceApi,
                        AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                        PolicyTypeRepository policyTypeRepository,
                        OrganizationRepository organizationRepository,
                        AgentConverter agentConverter,
                        KeycloakAuthenticationService keycloakAuthenticationService,
                        SSIJWTUtils ssiJwtUtils,
                        JWTUtils jwtUtils,
                        GendoxWebHookAdapter gendoxWebHookAdapter) {
        this.agentRepository = agentRepository;
        this.credentialIssuanceApi = credentialIssuanceApi;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.policyTypeRepository = policyTypeRepository;
        this.organizationRepository = organizationRepository;
        this.agentConverter = agentConverter;
        this.keycloakAuthenticationService = keycloakAuthenticationService;
        this.ssiJwtUtils = ssiJwtUtils;
        this.jwtUtils = jwtUtils;
        this.gendoxWebHookAdapter = gendoxWebHookAdapter;

        WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();


    }


    public Agent getAgentById(UUID id) throws ProvenAiException {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Agent not found with id:" + id, HttpStatus.NOT_FOUND));
    }


    public Agent getAgentByUsername(String agentUsername) throws ProvenAiException {
        return agentRepository.findByAgentUsername(agentUsername).orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Agent not found with username:" + agentUsername, HttpStatus.NOT_FOUND));
    }

    public Agent getAgentByName(String agentName) throws ProvenAiException {
        return agentRepository.findByAgentName(agentName);
    }

    public Page<Agent> getAllAgents(AgentCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return agentRepository.findAll(AgentPredicates.build(criteria), pageable);
    }



    public Page<AgentPublicDTO> getPublicAgentByCriteria(AgentCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }

        Page<Agent> agents = agentRepository.findAll(AgentPredicates.build(criteria), pageable);
        return agents.map(agentConverter::toPublicDTO);
    }

    public Page<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(UUID id, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(id, pageable);
    }


    public Agent createAgent(Agent agent, List<Policy> policies) throws ProvenAiException {

        if (agentRepository.existsByAgentUsername(agent.getAgentUsername())) {
            throw new ProvenAiException("AGENT_USERNAME_EXISTS", "Agent with the same username already exists", HttpStatus.BAD_REQUEST);
        }

        Instant now = Instant.now();
        agent.setCreatedAt(now);
        agent.setUpdatedAt(now);

        Agent savedAgent = agentRepository.save(agent);

        List<AgentPurposeOfUsePolicies> savedPolicies = agentPurposeOfUsePoliciesService.savePoliciesForAgent(savedAgent, policies);


        return savedAgent;
    }


    public W3CVC createAgentW3CVCByID(UUID agentId) throws JsonProcessingException, JSONException, ProvenAiException {
        Agent agent = getAgentById(agentId);
        Organization organization = getOrganizationByAgentId(agentId);
        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(agentId);

//        // Build the list of usage policies
        List<Policy> usagePolicies = agentPurposeOfUsePolicies.stream().map(agentPurposeOfUsePolicy -> new Policy((policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyType().getId())).get().getName(), agentPurposeOfUsePolicy.getValue())).toList();


        VerifiableCredential<AIAgentCredentialSubject> verifiableCredential = new VerifiableCredential<>();
        verifiableCredential.setCredentialSubject(AIAgentCredentialSubject.builder()
                .id(organization.getOrganizationDid())
                .organizationName(organization.getName())
                .agentName(agent.getAgentName())
                .agentId(agentId.toString())
                .creationDate(Instant.now())
                .usagePolicies(usagePolicies)
                .build());

        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();

        return provenAIIssuer.generateUnsignedVC(verifiableCredential);
    }


    public Optional<Organization> getOrganizationOptionalByAgentId(UUID agentId) throws ProvenAiException {
        Agent agent = getAgentById(agentId);
        if (agent == null) {
            throw new IllegalArgumentException("Agent not found with ID: " + agentId);
        }
        return organizationRepository.findById(agent.getOrganizationId());
    }

    public Organization getOrganizationByAgentId(UUID agentId) throws ProvenAiException {
        return getOrganizationOptionalByAgentId(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for Agent ID: " + agentId));

    }

    public Object createAgentSignedVcJwt(W3CVC w3CVC, UUID agentId) throws ProvenAiException {
        JWKKey jwkKey = new JWKKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();
        Organization organization = getOrganizationByAgentId(agentId);

        EventPayloadDTO eventPayload = new EventPayloadDTO();
        eventPayload.setOrganizationDid(organization.getOrganizationDid());
        eventPayload.setProjectAgentId(String.valueOf(agentId));
        eventPayload.setOrganizationId(organization.getId().toString());

        ResponseEntity<WebHookEventResponse> responseEntity =
                gendoxWebHookAdapter.gendoxWebHookEvent("PROVEN_AI_AGENT_REGISTRATION", eventPayload);

        return provenAIIssuer.generateSignedVCJwt(w3CVC, jwkKey, issuerDid, organization.getOrganizationDid());


    }

    public String createAgentVCOffer(W3CVC w3CVC) {

        WaltIdCredentialIssuanceRequest request = WaltIdCredentialIssuanceRequest.builder().issuerDid(issuerDid).issuerKey(IssuerKey.builder().jwk(issuerPrivateJwk).type("jwk").build()).credentialData(w3CVC).build();
        return credentialIssuanceApi.issueCredential(request);
    }


    public void deleteAgent(UUID agentId) throws ProvenAiException {
        Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Agent not found with id: " + agentId, HttpStatus.NOT_FOUND));
        agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePoliciesByAgentId(agentId);
        agentRepository.delete(agent);
    }


    public Agent updateAgent(Agent agent) throws ProvenAiException {
        Agent existingAgent = getAgentById(agent.getId());
        existingAgent.setAgentVcJwt(agent.getAgentVcJwt());
        existingAgent.setAgentName(agent.getAgentName());
        existingAgent.setUpdatedAt(Instant.now());

        return agentRepository.save(existingAgent);
    }

    public void updateAgentVerifiableId(UUID agentId, String verifiableId) {
        agentRepository.updateAgentVerifiableId(agentId, verifiableId);
    }


    public AccessTokenResponse getAgentAccessToken(String agentUsername, @Nullable String scope) throws ProvenAiException {

        return keycloakAuthenticationService.impersonateUser(agentUsername, scope);

    }

    public List<Policy> getAgentUsagePolicies(String jwt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode usagePoliciesNode = jwtUtils.getPayloadFromJwt(jwt)
                .path("vc")
                .path("credentialSubject")
                .path("agent")
                .path("usagePolicies");

        return mapper.readValue(usagePoliciesNode.toString(), new TypeReference<List<Policy>>() {
        });


    }

    public String getAgentVcJwt(String vpToken) throws IOException, JsonProcessingException {

       return ssiJwtUtils.getVCJwtFromVPJwt(vpToken);
    }
}
