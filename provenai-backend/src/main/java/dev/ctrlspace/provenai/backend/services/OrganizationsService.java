package dev.ctrlspace.provenai.backend.services;


import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.OrganizationPredicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
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


    public Organization updateOrganization(Organization organization) throws ProvenAiException {
        UUID organizationId = organization.getId();
        Organization existingOrganization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        existingOrganization.setName(organization.getName());
        existingOrganization.setCountry(organization.getCountry());
        existingOrganization.setVatNumber(organization.getVatNumber());
        existingOrganization.setOrganizationVpJwt(organization.getOrganizationVpJwt());
        existingOrganization.setUpdatedAt(Instant.now());

        return organizationRepository.save(existingOrganization);


    }

    public void deleteOrganization(UUID organizationId) throws ProvenAiException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        organizationRepository.delete(organization);
    }

}
