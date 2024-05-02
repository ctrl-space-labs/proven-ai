package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.repositories.AgentRepository;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.ssi.issuer.VerifiableCredentialBuilder;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import org.springframework.beans.factory.annotation.Autowired;
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


    public Optional<Agent> getAgentById (UUID id) {
        return agentRepository.findById(id);
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

}
