package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dev.ctrlspace.provenai.backend.controller.specs.OrganizationsControllerSpec;
import dev.ctrlspace.provenai.backend.converters.OrganizationConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.authentication.OrganizationUserDTO;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.CredentialVerificationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import dev.ctrlspace.provenai.backend.services.OrganizationsService;
import dev.ctrlspace.provenai.backend.utils.SecurityUtils;
import dev.ctrlspace.provenai.ssi.verifier.CredentialVerificationApi;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@RestController
public class OrganizationsController implements OrganizationsControllerSpec {

    private OrganizationsService organizationsService;
    private OrganizationConverter organizationConverter;

    @Value("${proven-ai.domains.proven-ai-frontend.base-url}")
    private String provenAiFrontendBaseUrl;

    private SecurityUtils securityUtils;



    @Autowired
    public OrganizationsController(OrganizationsService organizationsService,
                                   OrganizationConverter organizationConverter,
                                   CredentialVerificationApi credentialVerificationApi,
                                   SecurityUtils securityUtils) {
        this.organizationsService = organizationsService;
        this.organizationConverter = organizationConverter;
        this.securityUtils = securityUtils;


    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_DOCUMENT', 'getRequestedOrgIdFromPathVariable')")
    @GetMapping("/organizations/{organizationId}")
    public Organization getOrganizationById(@PathVariable UUID organizationId) throws ProvenAiException {

    return organizationsService.getOrganizationById(organizationId);
}

    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_DOCUMENT', 'getRequestedOrgsFromRequestParams')")
    @GetMapping("/organizations")
    public Page<Organization> getAllOrganizations(@Valid OrganizationCriteria criteria, Pageable pageable) throws ProvenAiException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (securityUtils.isUser() &&
                criteria.getOrganizationIdIn().isEmpty()) {
            UserProfile userProfile = (UserProfile) authentication.getPrincipal();

            List<String> organizationIds = userProfile.getOrganizations().stream()
                    .map(OrganizationUserDTO::getId)
                    .collect(Collectors.toList());

            criteria.setOrganizationIdIn(organizationIds);
        }

        return organizationsService.getAllOrganizations(criteria, pageable);
    }


    @PostMapping(value = "/organizations/registration", consumes = {"application/json"})
    public Organization registerOrganization(@RequestBody OrganizationDTO organizationDTO) throws IOException, ExecutionException, InterruptedException, ProvenAiException {
        Organization organization = organizationConverter.toEntity(organizationDTO);

        return organizationsService.registerOrganization(organization);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_UPDATE_ORGANIZATION', 'getRequestedOrgIdFromPathVariable')")
    @PutMapping(value = "/organizations/{organizationId}", consumes = {"application/json"})
    public Organization updateOrganization(@PathVariable UUID organizationId, @RequestBody OrganizationDTO organizationDTO) throws ProvenAiException {


        Organization organization = organizationConverter.toEntity(organizationDTO);
        organization.setId(organizationDTO.getId());

        if (!organizationId.equals(organizationDTO.getId())) {
            throw new ProvenAiException("ORGANIZATION_ID_MISMATCH", "ID in path and ID in body are not the same", HttpStatus.BAD_REQUEST);
        }

        return organizationsService.updateOrganization(organization);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_DELETE_ORGANIZATION', 'getRequestedOrgIdFromPathVariable')")
    @DeleteMapping("/organizations/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable UUID organizationId) throws ProvenAiException {
        organizationsService.deleteOrganization(organizationId);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_VERIFY_PROVEN_AI_ORG_VP', 'getRequestedOrgIdFromPathVariable')")
    @PostMapping("/organizations/{organizationId}/verify-vp")
    public CredentialVerificationDTO verifyOrganizationVP(@RequestBody JsonNode vpRequest, @PathVariable UUID organizationId, @RequestParam(required = false, name = "redirectPath") String base64RedirectPath) throws ProvenAiException {
        String redirectPath = null;
        if (base64RedirectPath != null) {
            redirectPath = new String(Base64.getDecoder().decode(base64RedirectPath));
        }

        if (Strings.isNotBlank(redirectPath) && redirectPath.startsWith("http")) {
            throw new ProvenAiException("INVALID_REDIRECT_URL", "Only relative path is allowed", HttpStatus.BAD_REQUEST);
        }
        CredentialVerificationDTO credentialVerificationDTO = organizationsService.verifyOrganizationVP(vpRequest, organizationId, redirectPath);
        return credentialVerificationDTO;
    }

}
