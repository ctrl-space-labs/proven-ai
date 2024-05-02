package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.backend.controller.specs.OrganizationsControllerSpec;
import dev.ctrlspace.provenai.backend.converters.OrganizationConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.VerifiablePresentationResponse;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.services.OrganizationsService;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@RestController
public class OrganizationsController implements OrganizationsControllerSpec {

    private OrganizationsService organizationsService;
    private OrganizationConverter organizationConverter;

    private CredentialIssuanceApi credentialIssuanceApi;


    @Autowired
    public OrganizationsController(OrganizationsService organizationsService,
                                   OrganizationConverter organizationConverter) {
        this.organizationsService = organizationsService;
        this.organizationConverter = organizationConverter;
        this.credentialIssuanceApi = new CredentialIssuanceApi();


    }

    @GetMapping("/organizations/{organizationId}")
    public Optional<Organization> getOrganizationById(@PathVariable UUID organizationId) throws ProvenAiException {

        Optional<Organization> organization = organizationsService.getOrganizationById(organizationId);
        return organization;
    }


    @GetMapping("/organizations")
    public Page<Organization> getAllOrganizations(@Valid OrganizationCriteria criteria, Pageable pageable) throws ProvenAiException {

        return organizationsService.getAllOrganizations(criteria, pageable);
    }




    @PostMapping(value = "/organizations/registration", consumes = {"application/json"})
    public Organization registerOrganization(@RequestBody OrganizationDTO organizationDTO) throws JsonProcessingException {
        Organization organization = organizationConverter.toEntity(organizationDTO);

        return organizationsService.registerOrganization(organization);
    }

    @PutMapping(value = "/organizations/{organizationId}", consumes = {"application/json"})
    public Organization updateOrganization(@PathVariable UUID organizationId, @RequestBody OrganizationDTO organizationDTO) throws Exception {
        UUID updatedOrganizationId = organizationDTO.getId();
        Organization organization = organizationConverter.toEntity(organizationDTO);
        organization.setId(updatedOrganizationId);

        if (!organizationId.equals(organizationDTO.getId())) {
            throw new ProvenAiException("ORGANIZATION_ID_MISMATCH", "ID in path and ID in body are not the same", HttpStatus.BAD_REQUEST);
        }

        return organizationsService.updateOrganization(organization);
    }

    @DeleteMapping("/organizations/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable UUID organizationId) throws ProvenAiException {
        organizationsService.deleteOrganization(organizationId);
    }


//    @GetMapping("/organizations/{organizationId}/verifiable-presentation")
//    public ResponseEntity<VerifiablePresentationResponse> getOrganizationVerifiablePresentation(@PathVariable UUID organizationId) throws ProvenAiException, IOException {
//        JsonNode verifiablePresentation = organizationsService.getOrganizationIdVerifiablePresentation(organizationId);
//        String credentialOfferUrl = credentialIssuanceApi.issueCredential(verifiablePresentation);
//
//        VerifiablePresentationResponse response = new VerifiablePresentationResponse();
//        response.setCredentialOfferUrl(credentialOfferUrl);
//        response.setVerifiablePresentation(verifiablePresentation);
//
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/organizations/{organizationId}/verifiable-presentation")
    public VerifiablePresentationResponse getOrganizationVerifiablePresentation(@PathVariable UUID organizationId) throws ProvenAiException, IOException {
        JsonNode verifiablePresentation = organizationsService.getOrganizationIdVerifiablePresentation(organizationId);
        String credentialOfferUrl = credentialIssuanceApi.issueCredential(verifiablePresentation);

        VerifiablePresentationResponse verifiablePresentationResponse = new VerifiablePresentationResponse();
        verifiablePresentationResponse.setCredentialOfferUrl(credentialOfferUrl);
        verifiablePresentationResponse.setVerifiablePresentation(verifiablePresentation);

        return verifiablePresentationResponse;
    }
}
