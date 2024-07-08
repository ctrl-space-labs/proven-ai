package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.DataPodControllerSpec;
import dev.ctrlspace.provenai.backend.converters.DataPodConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.authentication.OrganizationUserDTO;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodPublicDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.services.DataPodService;
import dev.ctrlspace.provenai.backend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class DataPodController implements DataPodControllerSpec {


    private DataPodService dataPodService;

    private DataPodConverter dataPodConverter;

    private SecurityUtils securityUtils;

    @Autowired
    public DataPodController(DataPodService dataPodService,
                             DataPodConverter dataPodConverter,
                             SecurityUtils securityUtils) {
        this.dataPodService = dataPodService;
        this.dataPodConverter = dataPodConverter;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/data-pods")
    public Page<DataPod> getAllDataPods(@Valid DataPodCriteria criteria, Pageable pageable, Authentication authentication) throws ProvenAiException {
//        if (securityUtils.isUser()) {
//            UserProfile userProfile = (UserProfile) authentication.getPrincipal();
//            OrganizationUserDTO organizationUserDTO = userProfile.getOrganizations().stream().filter(org -> org.getId().equals(organizationId)).findFirst().orElseThrow(() -> new GendoxException("ORGANIZATION_NOT_FOUND", "Organization not found", HttpStatus.NOT_FOUND));
//
//            organizationUserDTO.getProjects().stream().map(ProjectOrganizationDTO::getId).forEach(projectId -> criteria.getProjectIdIn().add(projectId));
//        }
        return dataPodService.getAllDataPods(criteria, pageable);

    }

    @GetMapping("/data-pods/public")
    public Page<DataPodPublicDTO> getAllPublicDataPods(@Valid DataPodCriteria criteria, Pageable pageable) throws ProvenAiException {
        return dataPodService.getAllPublicDataPods(criteria, pageable);
    }

    @GetMapping("/data-pods/{id}")
    public DataPod getDataPodById(@PathVariable UUID id) throws ProvenAiException {
        return dataPodService.getDataPodById(id);
    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_PROVEN_AI_DATAPOD', 'getRequestedDataPodIdFromPathVariable')")
    @GetMapping("/data-pods/{dataPodId}")
    public DataPod getDataPodById(@PathVariable UUID dataPodId) throws ProvenAiException {
        return dataPodService.getDataPodById(dataPodId);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_PROVEN_AI_DATAPOD', 'getRequestedDataPodIdFromPathVariable')")
    @GetMapping("/data-pods/{dataPodId}/acl-policies")
    public Page<AclPolicies> getAclPoliciesByDataPodId(@PathVariable UUID dataPodId, Pageable pageable) throws ProvenAiException {
        return dataPodService.getAclPoliciesByDataPodId(dataPodId, pageable);
    }


    @PostMapping("/data-pods")
    public DataPod createDataPod(@RequestBody DataPodDTO dataPodDto) throws ProvenAiException {
        DataPod dataPod = dataPodConverter.toEntity(dataPodDto);
        return dataPodService.createDataPod(dataPod, dataPodDto.getAclPolicies());
    }

//    TODO: Create project VC
//    @PostMapping("/data-pods/{id}/credential-offer")
//    public DataPod createDataPodVerifiableId(UUID id) {
//        return DataPod
//                .builder()
//                .id(id)
//                .credentialOfferUrl("openid-credential-offer://issuer.portal.walt.id/?credential_offer_uri=https%3A%2F%2Fissuer.portal.walt.id%2Fopenid4vc%2FcredentialOffer%3Fid%3D99d79908-32e6-4bd7-9873-562d2d262e12")
//                .credentialJwt("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsIjoiam9obkBkb2UuY29tIn0.7J1Gzv")
//                .build();

    @PreAuthorize("@securityUtils.hasAuthority('OP_DELETE_PROVEN_AI_AGENT', 'getRequestedDataPodIdFromPathVariable')")
    @DeleteMapping("/data-pods/{dataPodId}")
    public void deleteDataPod(@PathVariable UUID dataPodId) throws ProvenAiException {
        dataPodService.deleteDataPodById(dataPodId);
    }


    @PreAuthorize("@securityUtils.hasAuthority('OP_EDIT_PROVEN_AI_DATAPOD', 'getRequestedDataPodIdFromPathVariable')")
    @PutMapping("/data-pods/{dataPodId}")
    public DataPod updateDataPod(@PathVariable UUID dataPodId, @RequestBody DataPodDTO dataPodDTO) throws ProvenAiException {
        UUID updatedDataPodId = dataPodDTO.getId();

        DataPod dataPod = dataPodConverter.toEntity(dataPodDTO);
        dataPod.setId(updatedDataPodId);

        if (!dataPodId.equals(dataPodDTO.getId())) {
            throw new ProvenAiException("DATA_POD_ID_MISMATCH", "ID in path and ID in body are not the same", HttpStatus.BAD_REQUEST);
        }

        return dataPodService.updateDataPod(dataPod);
    }


}
