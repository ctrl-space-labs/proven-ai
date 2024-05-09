package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.repositories.*;
import dev.ctrlspace.provenai.backend.repositories.specifications.AgentPredicates;
import dev.ctrlspace.provenai.backend.repositories.specifications.OrganizationPredicates;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.dto.IssuerKey;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.LocalKey;
import kotlinx.serialization.json.JsonElement;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentService {

    private AgentRepository agentRepository;

    private OrganizationRepository organizationRepository;

    private PolicyOptionRepository policyOptionRepository;

    private AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository;


    private CredentialIssuanceApi credentialIssuanceApi;

    private String provenAIIssuerPrivateJwkStr;

    @Value("${issuer-did}")
    private String issuerDid;

    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;

    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public AgentService(AgentRepository agentRepository,
                        CredentialIssuanceApi credentialIssuanceApi,
                        String provenAIIssuerPrivateJwkStr,
                        OrganizationRepository organizationRepository,
                        PolicyOptionRepository policyOptionRepository,
                        AgentPurposeOfUsePoliciesRepository agentPurposeOfUsePoliciesRepository,
                        AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                        PolicyTypeRepository policyTypeRepository) {
        this.agentRepository = agentRepository;
        this.organizationRepository = organizationRepository;
        this.credentialIssuanceApi = credentialIssuanceApi;
        this.provenAIIssuerPrivateJwkStr = provenAIIssuerPrivateJwkStr;
        this.policyOptionRepository = policyOptionRepository;
        this.agentPurposeOfUsePoliciesRepository = agentPurposeOfUsePoliciesRepository;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.policyTypeRepository = policyTypeRepository;
    }


    public Agent getAgentById(UUID id) throws ProvenAiException {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Organization not found with id:" + id, HttpStatus.NOT_FOUND));
    }

    public Page<Agent> getAllAgents(AgentCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return agentRepository.findAll(AgentPredicates.build(criteria), pageable);
    }


    public Agent createAgent(Agent agent, List<Policy> policies) {
        // Save the Agent entity first to generate its ID
        Agent savedAgent = agentRepository.save(agent);

        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePoliciesList = policies.stream()
                .map(policy -> {
                    // Retrieve PolicyOption based on policy type name
                    PolicyOption policyOption = policyOptionRepository.findByName(policy.getPolicyValue());

                    AgentPurposeOfUsePolicies agentPurposeOfUsePolicy = new AgentPurposeOfUsePolicies();
                    agentPurposeOfUsePolicy.setAgentId(savedAgent.getId());
                    agentPurposeOfUsePolicy.setPolicyOptionId(policyOption.getId());
                    agentPurposeOfUsePolicy.setValue(policy.getPolicyValue());
                    agentPurposeOfUsePolicy.setPolicyTypeId(policyOption.getPolicyTypeId());
                    agentPurposeOfUsePolicy.setCreatedAt(Instant.now());
                    agentPurposeOfUsePolicy.setUpdatedAt(Instant.now());

                    return agentPurposeOfUsePolicy;
                })
                .collect(Collectors.toList());

        agentPurposeOfUsePoliciesRepository.saveAll(agentPurposeOfUsePoliciesList);

        return savedAgent;
    }


    public Optional<Organization> getOrganizationByAgentId(UUID agentId) {
        Optional<Agent> agentOptional = agentRepository.findById(agentId);
        if (agentOptional.isEmpty()) {
            throw new IllegalArgumentException("Agent not found with ID: " + agentId);
        }
        Agent agent = agentOptional.get();
        return organizationRepository.findById(agent.getOrganizationId());
    }



    public W3CVC createAgentW3CVCByID(UUID agentId) throws JsonProcessingException, JSONException {

        Optional<Agent> agent = agentRepository.findById(agentId);
        Optional<Organization> organization = getOrganizationByAgentId(agentId);
        ObjectMapper objectMapper = new ObjectMapper();
        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(agentId);

        // Build the list of usage policies
        List<Policy> usagePolicies = agentPurposeOfUsePolicies.stream()
                .map(agentPurposeOfUsePolicy -> new Policy((policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyTypeId())).get().getName()
                                                            , agentPurposeOfUsePolicy.getValue()))
                .toList();

        AIAgentCredentialSubject credentialSubject = AIAgentCredentialSubject.builder()
//                TODO organization DID from the VP
                .id("did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQ0ZRLU5yYTV5bnlCc2Z4d3k3YU5mOGR1QUVVQ01sTUlyUklyRGc2REl5NCIsIngiOiJoNW5idzZYOUptSTBCdnVRNU0wSlhmek84czJlRWJQZFYyOXdzSFRMOXBrIn0")
                .organizationName(organization.get().getName())
                .creationDate(Instant.now())
                .usagePolicies(usagePolicies)
                .build();

        VerifiableCredential<AIAgentCredentialSubject> verifiableCredential = new VerifiableCredential<>();
        verifiableCredential.setCredentialSubject(credentialSubject);

        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();

        return  provenAIIssuer.generateUnsignedVC(verifiableCredential);
    }

    public Object createAgentSignedVcJwt (W3CVC w3CVC) {
        LocalKey localKey = new LocalKey(provenAIIssuerPrivateJwkStr);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();

        return provenAIIssuer.generateSignedVCJwt(w3CVC, localKey,issuerDid,
                "did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQ0ZRLU5yYTV5bnlCc2Z4d3k3YU5mOGR1QUVVQ01sTUlyUklyRGc2REl5NCIsIngiOiJoNW5idzZYOUptSTBCdnVRNU0wSlhmek84czJlRWJQZFYyOXdzSFRMOXBrIn0");

    }

    public String createAgentVCOffer(W3CVC w3CVC) {

        WaltIdCredentialIssuanceRequest request = WaltIdCredentialIssuanceRequest.builder()
                .issuerDid(issuerDid)
                .issuerKey(IssuerKey.builder().jwk(provenAIIssuerPrivateJwkStr).type("jwk").build())
                .vc(w3CVC)
                .build();
        return credentialIssuanceApi.issueCredential(request);
    }


    //    delete agent
    public void deleteAgent(UUID agentId) throws ProvenAiException {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Agent not found with id: " + agentId, HttpStatus.NOT_FOUND));
        agentRepository.delete(agent);
    }

//    update agent

    public Agent updateAgent(Agent agent) throws ProvenAiException {
        UUID agentId = agent.getId();
        Agent existingAgent = getAgentById(agentId);
        existingAgent.setAgentVcId(agent.getAgentVcId());
        existingAgent.setUpdatedAt(agent.getUpdatedAt());

        return agentRepository.save(existingAgent);
    }

    public void updateAgentVerifiableId(UUID agentId, String verifiableId) {
        agentRepository.updateAgentVerifiableId(agentId, verifiableId);
    }

}
