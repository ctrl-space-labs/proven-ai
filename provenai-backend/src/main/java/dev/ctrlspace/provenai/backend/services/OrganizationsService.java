package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.OrganizationPredicates;
import dev.ctrlspace.provenai.ssi.converters.LegalEntityConverter;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationsService {

    private OrganizationRepository organizationRepository;

    private AgentService agentService;


    @Autowired
    public OrganizationsService(OrganizationRepository organizationRepository,
                                AgentService agentService) {
        this.organizationRepository = organizationRepository;
        this.agentService = agentService;
    }


    public Optional<Organization> getOptionalOrganizationById(UUID id) {
        return organizationRepository.findById(id);
    }

    public Organization getOrganizationById(UUID id) throws ProvenAiException {
        return this.getOptionalOrganizationById(id)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public Page<Organization> getAllOrganizations(OrganizationCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return organizationRepository.findAll(OrganizationPredicates.build(criteria), pageable);
    }

    public Organization registerOrganization(Organization organization) {

        return organizationRepository.save(organization);

    }

    public Optional<Organization> getOrganizationOptionalByAgentId(UUID agentId) throws ProvenAiException {
        Agent agent = agentService.getAgentById(agentId);
        if (agent == null) {
            throw new IllegalArgumentException("Agent not found with ID: " + agentId);
        }
        return organizationRepository.findById(agent.getOrganizationId());
    }

    public Organization getOrganizationByAgentId(UUID agentId) throws ProvenAiException {
        return getOrganizationOptionalByAgentId(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for Agent ID: " + agentId));

    }

    public Organization updateOrganization(Organization organization) throws ProvenAiException {
        UUID organizationId = organization.getId();
        Organization existingOrganization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        existingOrganization.setName(organization.getName());
        existingOrganization.setCountry(organization.getCountry());
        existingOrganization.setVatNumber(organization.getVatNumber());
        existingOrganization.setVerifiablePresentation(organization.getVerifiablePresentation());
        existingOrganization.setUpdatedAt(organization.getUpdatedAt());

        return organizationRepository.save(existingOrganization);


    }

    public void deleteOrganization(UUID organizationId) throws ProvenAiException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        organizationRepository.delete(organization);
    }

        public JsonNode getOrganizationIdVerifiablePresentation(UUID organizationId) throws ProvenAiException, IOException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        return exportOrganizationVerifiableId(organization.getVerifiablePresentation());
    }

    public JsonNode exportOrganizationVerifiableId(String verifiablePresentation) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(verifiablePresentation);
    }

//    public W3CVC getOrganizationIdVerifiablePresentation(UUID organizationId) throws ProvenAiException, IOException, JSONException {
//        Organization organization = organizationRepository.findById(organizationId)
//                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
//        return convertToW3CVC(organization.getVerifiablePresentation());
//    }
//    private W3CVC convertToW3CVC(VerifiableCredential<LegalEntityCredentialSubject> legalEntityVerifiableCredential) throws IOException, JSONException {
//        return legalEntityConverter.convertToW3CVC(legalEntityVerifiableCredential);
//
//    }
}
