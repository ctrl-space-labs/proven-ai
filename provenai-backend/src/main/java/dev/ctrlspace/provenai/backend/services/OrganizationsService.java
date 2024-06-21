package dev.ctrlspace.provenai.backend.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.CredentialVerificationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.OrganizationPredicates;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import dev.ctrlspace.provenai.ssi.verifier.CredentialVerificationApi;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationsService {

    private OrganizationRepository organizationRepository;

    private AgentService agentService;

    private CredentialVerificationApi credentialVerificationApi;


    @Autowired
    public OrganizationsService(OrganizationRepository organizationRepository,
                                AgentService agentService,
                                CredentialVerificationApi credentialVerificationApi) {
        this.organizationRepository = organizationRepository;
        this.agentService = agentService;
        this.credentialVerificationApi = credentialVerificationApi;
        WaltIdServiceInitUtils.INSTANCE.initializeWaltIdServices();

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
        Instant now = Instant.now();
        organization.setCreatedAt(now);
        organization.setUpdatedAt(now);

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

        existingOrganization.setNaturalPerson(organization.getNaturalPerson());
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

    public CredentialVerificationDTO verifyOrganizationVP(JsonNode vpRequest) {
        CredentialVerificationDTO credentialVerificationDTO = new CredentialVerificationDTO();
        credentialVerificationDTO.setCredentialVerificationUrl(credentialVerificationApi.verifyCredential(vpRequest));

        return credentialVerificationDTO;
    }


}
