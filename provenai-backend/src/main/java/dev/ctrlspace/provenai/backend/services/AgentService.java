package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.repositories.*;
import dev.ctrlspace.provenai.backend.repositories.specifications.AgentPredicates;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.dto.IssuerKey;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.LocalKey;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class AgentService {

    private AgentRepository agentRepository;

    private OrganizationRepository organizationRepository;

    private CredentialIssuanceApi credentialIssuanceApi;

    @Value("${issuer-did}")
    private String issuerDid;

    @Value("${issuer-private-jwk}")
    private String issuerPrivateJwk;


    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;

    private PolicyTypeRepository policyTypeRepository;

    private OrganizationsService organizationsService;

    @Autowired
    public AgentService(AgentRepository agentRepository,
                        CredentialIssuanceApi credentialIssuanceApi,
                        AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                        PolicyTypeRepository policyTypeRepository) {
        this.agentRepository = agentRepository;
        this.credentialIssuanceApi = credentialIssuanceApi;
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
        agent.setAgentName(agent.getAgentName() + "Agent");
        // Save the Agent entity first to generate its ID
        Agent savedAgent = agentRepository.save(agent);

        List<AgentPurposeOfUsePolicies> savedPolicies = agentPurposeOfUsePoliciesService.savePoliciesForAgent(savedAgent, policies);


        return savedAgent;
    }


    public W3CVC createAgentW3CVCByID(UUID agentId) throws JsonProcessingException, JSONException, ProvenAiException {

        Agent agent = getAgentById(agentId);
        Organization organization = organizationsService.getOrganizationByAgentId(agentId);
        ObjectMapper objectMapper = new ObjectMapper();
        List<AgentPurposeOfUsePolicies> agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesService.getAgentPurposeOfUsePolicies(agentId);

        // Build the list of usage policies
        List<Policy> usagePolicies = agentPurposeOfUsePolicies.stream()
                .map(agentPurposeOfUsePolicy -> new Policy((policyTypeRepository.findById(agentPurposeOfUsePolicy.getPolicyTypeId())).get().getName()
                        , agentPurposeOfUsePolicy.getValue()))
                .toList();

        AIAgentCredentialSubject credentialSubject = AIAgentCredentialSubject.builder()
                .id(organization.getOrganizationDid())
                .organizationName(organization.getName())
                .agentName(agent.getAgentName())
                .creationDate(Instant.now())
                .usagePolicies(usagePolicies)
                .build();

        VerifiableCredential<AIAgentCredentialSubject> verifiableCredential = new VerifiableCredential<>();
        verifiableCredential.setCredentialSubject(credentialSubject);

        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();

        return provenAIIssuer.generateUnsignedVC(verifiableCredential);
    }


    public Object createAgentSignedVcJwt(W3CVC w3CVC, UUID agentId) throws ProvenAiException {
        LocalKey localKey = new LocalKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();
        Organization organization = organizationsService.getOrganizationByAgentId(agentId);

        return provenAIIssuer.generateSignedVCJwt(w3CVC, localKey, issuerDid, organization.getOrganizationDid());

    }

    public String createAgentVCOffer(W3CVC w3CVC) {

        WaltIdCredentialIssuanceRequest request = WaltIdCredentialIssuanceRequest.builder()
                .issuerDid(issuerDid)
                .issuerKey(IssuerKey.builder().jwk(issuerPrivateJwk).type("jwk").build())
                .vc(w3CVC)
                .build();
        return credentialIssuanceApi.issueCredential(request);
    }


    public void deleteAgent(UUID agentId) throws ProvenAiException {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Agent not found with id: " + agentId, HttpStatus.NOT_FOUND));
        agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePoliciesByAgentId(agentId);
        agentRepository.delete(agent);
    }


    public Agent updateAgent(Agent agent) throws ProvenAiException {
        Agent existingAgent = getAgentById(agent.getId());
        existingAgent.setAgentVcJwt(agent.getAgentVcJwt());
        existingAgent.setUpdatedAt(agent.getUpdatedAt());

        return agentRepository.save(existingAgent);
    }

    public void updateAgentVerifiableId(UUID agentId, String verifiableId) {
        agentRepository.updateAgentVerifiableId(agentId, verifiableId);
    }

}
