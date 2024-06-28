package dev.ctrlspace.provenai.backend.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.backend.adapters.GendoxWebHookAdapter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.CredentialVerificationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.EventPayloadDTO;
import dev.ctrlspace.provenai.backend.model.dtos.WebHookEventResponse;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.OrganizationPredicates;
import dev.ctrlspace.provenai.backend.utils.JWTUtils;
import dev.ctrlspace.provenai.backend.utils.ValidatorUtils;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.id.LegalEntityCredentialSubject;
import dev.ctrlspace.provenai.ssi.verifier.CredentialVerificationApi;
import dev.ctrlspace.provenai.ssi.verifier.ProvenAIVerifier;
import dev.ctrlspace.provenai.utils.SSIJWTUtils;
import dev.ctrlspace.provenai.utils.WaltIdServiceInitUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.LocalKey;
import jakarta.annotation.Nullable;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrganizationsService {

    private OrganizationRepository organizationRepository;

    private AgentService agentService;

    private CredentialVerificationApi credentialVerificationApi;

    @Value("${proven-ai.domains.proven-ai-frontend.base-url}")
    private String baseUrl;

    @Value("${proven-ai.domains.proven-ai-frontend.context-path}")
    private String contextPath;



    private JWTUtils jwtUtils;

    private SSIJWTUtils ssiJwtUtils;

    private ValidatorUtils validatorUtils;

    private GendoxWebHookAdapter gendoxWebHookAdapter;


    @Autowired
    public OrganizationsService(OrganizationRepository organizationRepository,
                                AgentService agentService,
                                CredentialVerificationApi credentialVerificationApi,
                                JWTUtils jwtUtils,
                                SSIJWTUtils ssiJwtUtils,
                                ValidatorUtils validatorUtils,
                                GendoxWebHookAdapter gendoxWebHookAdapter) {
        this.organizationRepository = organizationRepository;
        this.agentService = agentService;
        this.credentialVerificationApi = credentialVerificationApi;
        this.jwtUtils = jwtUtils;
        this.ssiJwtUtils = ssiJwtUtils;
        this.validatorUtils = validatorUtils;
        this.gendoxWebHookAdapter = gendoxWebHookAdapter;
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

    public Organization registerOrganization(Organization organization) throws ExecutionException, InterruptedException, ProvenAiException, IOException {
        EventPayloadDTO eventPayload = new EventPayloadDTO();

        if (organization.getOrganizationVpJwt() == null) {

            eventPayload.setOrganizationId(organization.getId().toString());
            ResponseEntity<WebHookEventResponse> responseEntity =
                    gendoxWebHookAdapter.gendoxWebHookEvent("PROVEN_AI_REQUEST_ORGANIZATION_DID", eventPayload);

            organization.setOrganizationDid(responseEntity.getBody().getData());


        } else if (organization.getOrganizationVpJwt() != null) {
            ProvenAIVerifier provenAIVerifier = new ProvenAIVerifier();

            Boolean verificationResult = provenAIVerifier.verifyVPJwt(organization.getOrganizationVpJwt());
            if (!verificationResult) {
                throw new ProvenAiException("INVALID_VP_JWT", "Invalid VP JWT", HttpStatus.BAD_REQUEST);
            }

            String OrganizationVcJwt = ssiJwtUtils.getVCJwtFromVPJwt(organization.getOrganizationVpJwt());


//            Boolean credentialSubjectValidation = validatorUtils.validateCredentialSubjectFields(OrganizationVcJwt, organization);
//            if (!credentialSubjectValidation) {
//                throw new ProvenAiException("CREDENTIAL_FIELDS_MISMATCH", "Credential fields do not match the organization fields", HttpStatus.BAD_REQUEST);
//            }

            organization.setOrganizationDid(jwtUtils.getPayloadFromJwt(organization.getOrganizationVpJwt()).get("sub").toString());

            eventPayload.setOrganizationDid(organization.getOrganizationDid());
            eventPayload.setOrganizationId(organization.getId().toString());
            ResponseEntity<WebHookEventResponse> responseEntity =
                    gendoxWebHookAdapter.gendoxWebHookEvent("PROVEN_AI_ORGANIZATION_REGISTRATION", eventPayload);
        }


        organization.setCreatedAt(Instant.now());
        organization.setUpdatedAt(Instant.now());

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

    /**
     * Verify organization VP
     * @param vpRequest
     * @param organizationId the organization ID to use for the default redirect if the `redirectPath` is not provided.
     * @param redirectPath the URL to redirect to after verification
     * @return
     */
    public CredentialVerificationDTO verifyOrganizationVP(JsonNode vpRequest, UUID organizationId, String redirectPath) {
        CredentialVerificationDTO credentialVerificationDTO = new CredentialVerificationDTO();
        String successRedirect = getSuccessRedirectUrl(organizationId, redirectPath);

        String VERIFIER_ERROR_URL = "http://localhost:3001/ssi/verify/fail/?id=$id";
        credentialVerificationDTO.setCredentialVerificationUrl(credentialVerificationApi.verifyCredential(vpRequest, successRedirect, VERIFIER_ERROR_URL));

        return credentialVerificationDTO;
    }

    private String getSuccessRedirectUrl(UUID organizationId, @Nullable String redirectPath) {


        if (redirectPath != null && !redirectPath.isEmpty()) {
            String url = removeParamFromUrl(baseUrl + redirectPath, "vcOfferSessionId");
            return url + "&vcOfferSessionId=$id";
        }

        return baseUrl + "/provenAI/data-pods-control/?vcOfferSessionId=$id&organizationId=" + organizationId.toString();
    }

    public static String removeParamFromUrl(String url, String paramToRemove) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);

        uriBuilder.replaceQueryParam(paramToRemove);

        URI uri = uriBuilder.build().toUri();

        return uri.toString();
    }


}
