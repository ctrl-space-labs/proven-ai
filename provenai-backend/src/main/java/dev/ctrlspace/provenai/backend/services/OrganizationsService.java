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

        Organization existingOrganization = organizationRepository.findById(organization.getId())
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organization.getId(), HttpStatus.NOT_FOUND));
        if (organization.getName() != null && organization.getName() != "") {
            existingOrganization.setName(organization.getName());
        }
        if (organization.getCountry() != null && organization.getCountry() != "") {
            existingOrganization.setCountry(organization.getCountry());
        }

        existingOrganization.setIsNaturalPerson(organization.getIsNaturalPerson());
        existingOrganization.setLegalPersonIdentifier(organization.getLegalPersonIdentifier());
        existingOrganization.setLegalName(organization.getLegalName());
        existingOrganization.setLegalAddress(organization.getLegalAddress());
        existingOrganization.setTaxReference(organization.getTaxReference());
        existingOrganization.setFamilyName(organization.getFamilyName());
        existingOrganization.setFirstName(organization.getFirstName());
        existingOrganization.setDateOfBirth(organization.getDateOfBirth());
        existingOrganization.setGender(organization.getGender());
        existingOrganization.setNationality(organization.getNationality());
        existingOrganization.setPersonalIdentifier(organization.getPersonalIdentifier());
        existingOrganization.setVatNumber(organization.getVatNumber());
        if (organization.getOrganizationVpJwt() != null && organization.getOrganizationVpJwt() != "") {
            existingOrganization.setOrganizationVpJwt(organization.getOrganizationVpJwt());
        }
        if (organization.getOrganizationDid() != null && organization.getOrganizationDid() != "") {
            existingOrganization.setOrganizationDid(organization.getOrganizationDid());
        }
        existingOrganization.setUpdatedAt(Instant.now());

        return organizationRepository.save(existingOrganization);


    }

    public void deleteOrganization(UUID organizationId) throws ProvenAiException {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ProvenAiException("ORGANIZATION_NOT_FOUND", "Organization not found with id: " + organizationId, HttpStatus.NOT_FOUND));
        organizationRepository.delete(organization);
    }

}
