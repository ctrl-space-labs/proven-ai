package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.converters.DataPodConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.*;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodPublicDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.repositories.DataPodRepository;
import dev.ctrlspace.provenai.backend.repositories.OrganizationRepository;
import dev.ctrlspace.provenai.backend.repositories.PolicyTypeRepository;
import dev.ctrlspace.provenai.backend.repositories.specifications.DataPodPredicates;
import dev.ctrlspace.provenai.ssi.issuer.CredentialIssuanceApi;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.dto.IssuerKey;
import dev.ctrlspace.provenai.ssi.model.dto.WaltIdCredentialIssuanceRequest;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.DataOwnershipCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.jwk.JWKKey;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataPodService {

    private DataPodRepository dataPodRepository;

    private AclPoliciesService aclPoliciesService;

    private DataPodConverter dataPodConverter;

    private CredentialIssuanceApi credentialIssuanceApi;

    private OrganizationRepository organizationRepository;

    @Value("${proven-ai.ssi.issuer-did}")
    private String issuerDid;

    @Value("${proven-ai.ssi.issuer-private-jwk}")
    private String issuerPrivateJwk;

    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    public DataPodService(DataPodRepository dataPodRepository,
                          AclPoliciesService aclPoliciesService,
                          AgentService agentService,
                          DataPodConverter dataPodConverter,
                          CredentialIssuanceApi credentialIssuanceApi,
                          OrganizationRepository organizationRepository,
                          PolicyTypeRepository policyTypeRepository) {
        this.dataPodRepository = dataPodRepository;
        this.aclPoliciesService = aclPoliciesService;
        this.dataPodConverter = dataPodConverter;
        this.credentialIssuanceApi = credentialIssuanceApi;
        this.organizationRepository = organizationRepository;
        this.policyTypeRepository = policyTypeRepository;


    }


    public Optional<DataPod> getOptionalDataPodById(UUID id) {
        return dataPodRepository.findById(id);
    }

    public DataPod getDataPodById(UUID id) throws ProvenAiException {
        return this.getOptionalDataPodById(id)
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + id, HttpStatus.NOT_FOUND));
    }


    public Page<DataPod> getAllDataPods(DataPodCriteria criteria, Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return dataPodRepository.findAll(DataPodPredicates.build(criteria), pageable);
    }

    public Page<DataPodPublicDTO> getAllPublicDataPods(DataPodCriteria criteria , Pageable pageable) throws ProvenAiException {
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }

        Page<DataPod> dataPods = dataPodRepository.findAll(DataPodPredicates.build(criteria), pageable);

        return dataPods.map(dataPodConverter::toPublicDTO);
    }

    public Page<AclPolicies> getAclPoliciesByDataPodId(UUID dataPodId, Pageable pageable) throws  ProvenAiException{
        if (pageable == null) {
            throw new ProvenAiException("Pageable cannot be null", "pageable.null", HttpStatus.BAD_REQUEST);
        }
        return aclPoliciesService.getAclPoliciesByDataPodId(dataPodId, pageable);
    }


    public void deleteDataPodById(UUID id) throws ProvenAiException {
        DataPod dataPod = dataPodRepository.findById(id)
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + id, HttpStatus.NOT_FOUND));
        aclPoliciesService.deletePoliciesByDataPodId(id);
        dataPodRepository.delete(dataPod);

    }


    public DataPod createDataPod(DataPod dataPod, List<Policy> policies) throws ProvenAiException {

        if (dataPodRepository.existsByPodUniqueName(dataPod.getPodUniqueName())) {
            throw new ProvenAiException("DataPod with the same name already exists", "dataPod.name.duplicate", HttpStatus.BAD_REQUEST);
        }

        Instant now = Instant.now();
        dataPod.setCreatedAt(now);
        dataPod.setUpdatedAt(now);

        DataPod savedDataPod = dataPodRepository.save(dataPod);

        List<AclPolicies> savedPolicies = aclPoliciesService.savePoliciesForDataPod(savedDataPod, policies);

        return savedDataPod;
    }

    public DataPod updateDataPod(DataPod dataPod) throws ProvenAiException {
        UUID dataPodId = dataPod.getId();
        DataPod existingDataPod = dataPodRepository.findById(dataPod.getId())
                .orElseThrow(() -> new ProvenAiException("DATA_POD_NOT_FOUND", "DataPod not found with id: " + dataPod.getId(), HttpStatus.NOT_FOUND));
        existingDataPod.setPodUniqueName(dataPod.getPodUniqueName());
        existingDataPod.setHostUrl(dataPod.getHostUrl());
        existingDataPod.setUpdatedAt(Instant.now());

        return dataPodRepository.save(existingDataPod);


    }

        public List<DataPod> getAccessibleDataPodsForAgent(UUID agentId) throws ProvenAiException, JsonProcessingException {

            return dataPodRepository.findAccessibleDataPodsForAgent(agentId);

        }


    public Map<String, List<UUID>> getDataPodsByHostUrl(List<DataPod> dataPods) {
        return dataPods.stream()
                .collect(Collectors.groupingBy(DataPod::getHostUrl,
                        Collectors.mapping(DataPod::getId, Collectors.toList())));
    }


    public W3CVC createDataPodW3CVCByID(UUID dataPodId) throws JsonProcessingException, JSONException, ProvenAiException {
        DataPod dataPod = getDataPodById(dataPodId);
        Organization organization = getOrganizationByDataPodId(dataPodId);

        List<AclPolicies> aclPolicies = aclPoliciesService.getAclPoliciesByDataPodId(dataPodId);

//        // Build the list of usage policies
        List<Policy> usagePolicies = aclPolicies.stream().map(aclPolicy -> new Policy((policyTypeRepository.findById(aclPolicy.getPolicyType().getId())).get().getName(), aclPolicy.getValue())).toList();



        VerifiableCredential<DataOwnershipCredentialSubject> verifiableCredential = new VerifiableCredential<>();
        verifiableCredential.setCredentialSubject(DataOwnershipCredentialSubject.builder()
                .id(organization.getOrganizationDid())
                .dataPodName(dataPod.getPodUniqueName())
                .dataPodId(dataPod.getId().toString())
                .usagePolicies(usagePolicies)
                .isccCollectionMerkleRoot("")
                .ownershipStatus("active")
                .creationDate(Instant.now())
                .build());

        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();

        return provenAIIssuer.generateUnsignedVC(verifiableCredential);
    }


    public Object createDataPodSignedVcJwt(W3CVC w3CVC, UUID dataPodId) throws ProvenAiException {
        JWKKey jwkKey = new JWKKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();
        Organization organization = getOrganizationByDataPodId(dataPodId);

        return provenAIIssuer.generateSignedVCJwt(w3CVC, jwkKey, issuerDid, organization.getOrganizationDid());


    }

    public String createDataPodVCOffer(W3CVC w3CVC) {

        WaltIdCredentialIssuanceRequest request = WaltIdCredentialIssuanceRequest.builder().issuerDid(issuerDid).issuerKey(IssuerKey.builder().jwk(issuerPrivateJwk).type("jwk").build()).credentialData(w3CVC).build();
        return credentialIssuanceApi.issueCredential(request);
    }


    public Organization getOrganizationByDataPodId(UUID dataPodId) throws ProvenAiException {
        return getOrganizationOptionalByDataPodId(dataPodId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for Data pod ID: " + dataPodId));

    }


    public Optional<Organization> getOrganizationOptionalByDataPodId(UUID dataPodId) throws ProvenAiException {
        DataPod dataPod = getDataPodById(dataPodId);
        if (dataPod == null) {
            throw new IllegalArgumentException("DataPod not found with ID: " + dataPodId);
        }
        return organizationRepository.findById(dataPod.getOrganizationId());
    }

//    public void updateDataPodVerifiableId(UUID dataPodId, String verifiableId) {
//        dataPodRepository.updateAgentVerifiableId(dataPodId, verifiableId);
//    }



}

