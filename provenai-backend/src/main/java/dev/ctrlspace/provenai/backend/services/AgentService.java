package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.authentication.KeycloakAuthenticationService;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.repositories.AgentRepository;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.AgentPredicates;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.dto.IssuerKey;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubjectBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import dev.ctrlspace.provenai.ssi.verifier.PresentationVerifier;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PresentationVerificationResponse;
import id.walt.credentials.verification.policies.ExpirationDatePolicy;
import id.walt.credentials.verification.policies.JwtSignaturePolicy;
import id.walt.credentials.verification.policies.NotBeforeDatePolicy;
import id.walt.credentials.verification.policies.vp.HolderBindingPolicy;
import id.walt.crypto.keys.LocalKey;
import jakarta.annotation.Nullable;
import org.json.JSONException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

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


    @Autowired
    public AgentService(AgentRepository agentRepository, CredentialIssuanceApi credentialIssuanceApi, AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService, PolicyTypeRepository policyTypeRepository, OrganizationRepository organizationRepository, KeycloakAuthenticationService keycloakAuthenticationService) {
        this.agentRepository = agentRepository;
        this.credentialIssuanceApi = credentialIssuanceApi;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.policyTypeRepository = policyTypeRepository;
        this.organizationRepository = organizationRepository;
        this.keycloakAuthenticationService = keycloakAuthenticationService;

        WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();


    }


    public Agent getAgentById(UUID id) throws ProvenAiException {
        return agentRepository.findById(id).orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Organization not found with id:" + id, HttpStatus.NOT_FOUND));
    }

    public Page<Agent> getAllAgents(AgentCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return agentRepository.findAll(AgentPredicates.build(criteria), pageable);
    }


    public Agent createAgent(Agent agent, List<Policy> policies) {
        agent.setAgentName(agent.getAgentName());
        // Save the Agent entity first to generate its ID
        Agent savedAgent = agentRepository.save(agent);

        List<AgentPurposeOfUsePolicies> savedPolicies = agentPurposeOfUsePoliciesService.savePoliciesForAgent(savedAgent, policies);


        return savedAgent;
    }


    public W3CVC createAgentW3CVCByID( UUID agentId) throws JsonProcessingException, JSONException, ProvenAiException {
        Agent agent = getAgentById(agentId);
        Organization organization = getOrganizationByAgentId(agentId);
        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(agentId);

//        // Build the list of usage policies
        List<Policy> usagePolicies = agentPurposeOfUsePolicies.stream().map(agentPurposeOfUsePolicy -> new Policy((policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyTypeId())).get().getName(), agentPurposeOfUsePolicy.getValue())).toList();


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
        return getOrganizationOptionalByAgentId(agentId).orElseThrow(() -> new IllegalArgumentException("Organization not found for Agent ID: " + agentId));

    }

    public Object createAgentSignedVcJwt(W3CVC w3CVC, UUID agentId) throws ProvenAiException {
        LocalKey localKey = new LocalKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();
        Organization organization = getOrganizationByAgentId(agentId);

        return provenAIIssuer.generateSignedVCJwt(w3CVC, localKey, issuerDid, organization.getOrganizationDid());

    }

    public String createAgentVCOffer(W3CVC w3CVC) {

        WaltIdCredentialIssuanceRequest request = WaltIdCredentialIssuanceRequest.builder().issuerDid(issuerDid).issuerKey(IssuerKey.builder().jwk(issuerPrivateJwk).type("jwk").build()).vc(w3CVC).build();
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

    public Boolean verifyAgentVP(String vpJwt) throws InterruptedException, ExecutionException {
//      Initialize presentationVerifier
         PresentationVerifier presentationVerifier = new PresentationVerifier();

//        Initialize Policies to be checked
        HolderBindingPolicy holderBindingPolicy = new HolderBindingPolicy();
        JwtSignaturePolicy jwtSignaturePolicy = new JwtSignaturePolicy();
        NotBeforeDatePolicy notBeforeDatePolicy = new NotBeforeDatePolicy();
        ExpirationDatePolicy expirationDatePolicy = new ExpirationDatePolicy();
//        pass null for args
//      Initialize Policy Types
        List<PolicyRequest> vpPolicies = new ArrayList<>();
        List<PolicyRequest> globalVcPolicies = new ArrayList<>();
        HashMap<String, List<PolicyRequest>> specificCredentialPolicies = new HashMap<>();
        HashMap<String, Object> presentationContext = new HashMap<>();

        vpPolicies.add(new PolicyRequest(holderBindingPolicy, null));
        vpPolicies.add(new PolicyRequest(holderBindingPolicy, null));
        globalVcPolicies.add(new PolicyRequest(jwtSignaturePolicy, null));

        globalVcPolicies.add(new PolicyRequest(expirationDatePolicy, null));
        globalVcPolicies.add(new PolicyRequest(notBeforeDatePolicy, null));
        globalVcPolicies.add(new PolicyRequest(jwtSignaturePolicy, null));


        CompletableFuture<PresentationVerificationResponse> verificationFuture = presentationVerifier.verifyPresentationAsync(vpJwt, vpPolicies,
                                                                            globalVcPolicies, specificCredentialPolicies, presentationContext);

        PresentationVerificationResponse response = verificationFuture.get();

        return response.overallSuccess();
    }


    public AccessTokenResponse getAgentJwtToken(String userIdentifier, @Nullable String scope) throws ProvenAiException {

        return keycloakAuthenticationService.impersonateUser(userIdentifier, scope);

    }

    public List<Policy> getAgentUsagePolicies(String jwt) throws JsonProcessingException {
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
            // Decode payload
            String payload = new String(decoder.decode(chunks[1]));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode payloadNode = mapper.readTree(payload);

            // Extract usagePolicies JSON node
            JsonNode usagePoliciesNode = payloadNode
                    .path("vc")
                    .path("credentialSubject")
                    .path("agent")
                    .path("usagePolicies");

            return mapper.readValue(usagePoliciesNode.toString(), new TypeReference<List<Policy>>() {
            });


        }
    }
