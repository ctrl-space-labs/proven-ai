package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiRuntimeException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.repositories.AgentRepository;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentService {

    private AgentRepository agentRepository;

    private OrganizationRepository organizationRepository;

    @Autowired
    public AgentService(AgentRepository agentRepository,
                        OrganizationRepository organizationRepository) {
        this.agentRepository = agentRepository;
        this.organizationRepository = organizationRepository;
    }


public Agent getAgentById(UUID id) throws ProvenAiException {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ProvenAiException("AGENT_NOT_FOUND", "Organization not found with id:" + id, HttpStatus.NOT_FOUND));
    }

    public Agent registerAgent(Agent agent) {

        return agentRepository.save(agent);

    }

    public Optional<Organization> getOrganizationByAgentId(UUID agentId) {
        Optional<Agent> agentOptional = agentRepository.findById(agentId);
        if (agentOptional.isEmpty()) {
            throw new IllegalArgumentException("Agent not found with ID: " + agentId);
        }
        Agent agent = agentOptional.get();
        return organizationRepository.findById(agent.getOrganizationId());
    }

//    CRUD to create an agent



    public W3CVC createAgentVerifiableCredentialID(UUID agentId) throws JsonProcessingException {
        Optional<Agent> agent = agentRepository.findById(agentId);
        Optional<Organization> organization = getOrganizationByAgentId(agentId);
        ObjectMapper objectMapper = new ObjectMapper();

        // Build the credential subject
        AIAgentCredentialSubject credentialSubject = AIAgentCredentialSubject.builder()
                .id(agent.get().getId().toString())
                .organizationName(organization.get().getName())
                .creationDate(Instant.now())
                .purpose("AI Agent Credential")
                .build();

//        // Build the verifiable credential
//        VerifiableCredentialBuilder credentialBuilder = new VerifiableCredentialBuilder();
//        credentialBuilder.addContext("https://www.w3.org/2018/credentials/v1");
//        credentialBuilder.addType("VerifiableAIAgent");
//        credentialBuilder.setCredentialId("urn:uuid: " + UUID.randomUUID().toString());
//        credentialBuilder.setIssuerDid("did:key:_proven-ai-issuer_");
//        credentialBuilder.setSubjectDid("orgdid");
//        credentialBuilder.credentialSubject(objectMapper.writeValueAsString(credentialSubject));
//        credentialBuilder.validFor(Duration.ofDays(30)); // Example: 30 days validity period
//
//        // Build and return the verifiable credential
//        return credentialBuilder.buildCredential();
        return null;
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

}
