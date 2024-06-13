package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import dev.ctrlspace.provenai.backend.repositories.specifications.AuditPermissionOfUseVcPredicates;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
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
import java.util.List;
import java.util.UUID;
import dev.ctrlspace.provenai.backend.repositories.AuditPermissionOfUseVcRepository;
@Service
public class AuditPermissionOfUseVcService {
    @Value("${proven-ai.ssi.issuer-private-jwk}")
    private String issuerPrivateJwk;
    @Value("${proven-ai.ssi.issuer-did}")
    private String issuerDid;
    private AuditPermissionOfUseVcRepository auditPermissionOfUseVcRepository;

    @Autowired
    public AuditPermissionOfUseVcService(AuditPermissionOfUseVcRepository auditPermissionOfUseVcRepository) {
        this.auditPermissionOfUseVcRepository = auditPermissionOfUseVcRepository;
    }

    public AuditPermissionOfUseVc getAuditPermissionOfUseVcById(UUID id) throws ProvenAiException {
        return auditPermissionOfUseVcRepository.findById(id).orElseThrow(() -> new ProvenAiException("AUDIT_LOG_NOT_FOUND", "Audit Log not found with id: " + id, HttpStatus.NOT_FOUND));
    }


    public List<AuditPermissionOfUseDTO> getAllAuditPermissionOfUseVc(AuditPermissionOfUseVcCriteria criteria) throws ProvenAiException {


        return auditPermissionOfUseVcRepository.findAuditPermissionsAnalytics(criteria);
    }


    public AuditPermissionOfUseVc createAuditLog( UUID ownerOrganizationId, String ownerOrganizationDid,
                                                 UUID processorOrganizationId, String processorOrganizationDid,
                                                 UUID searchId, UUID dataPodId,
                                                 String sectionIscc, String documentIscc,
                                                 Integer tokens, String embeddingModel
                                                ) {
        AuditPermissionOfUseVc auditPermissionOfUseVc = new AuditPermissionOfUseVc();

        auditPermissionOfUseVc.setOwnerOrganizationId(ownerOrganizationId);
        auditPermissionOfUseVc.setOwnerOrganizationDid(ownerOrganizationDid);
        auditPermissionOfUseVc.setProcessorOrganizationId(processorOrganizationId);
        auditPermissionOfUseVc.setProcessorOrganizationDid(processorOrganizationDid);
        auditPermissionOfUseVc.setSearchId(searchId);
        auditPermissionOfUseVc.setOwnerDataPodId(dataPodId);
        auditPermissionOfUseVc.setSectionIscc(sectionIscc);
        auditPermissionOfUseVc.setDocumentIscc(documentIscc);
        auditPermissionOfUseVc.setEmbeddingModel(embeddingModel);
        auditPermissionOfUseVc.setTokens(tokens);

        auditPermissionOfUseVc.setCreatedAt(Instant.now());
        auditPermissionOfUseVc.setUpdatedAt(Instant.now());

        auditPermissionOfUseVc = auditPermissionOfUseVcRepository.save(auditPermissionOfUseVc);

        return auditPermissionOfUseVc;


    }


    public W3CVC createPermissionOfUseW3CVC(String ownerOrganizationDid, String processorOrganizationDid,
                                            String sectionIscc, List<Policy> permissionOfUse) throws JsonProcessingException, JSONException {



        VerifiableCredential<PermissionOfUseCredentialSubject> verifiableCredential = new VerifiableCredential<>();
        verifiableCredential.setCredentialSubject(PermissionOfUseCredentialSubject.builder()
                .id(processorOrganizationDid)
                .ownerDID(ownerOrganizationDid)
                .policies(permissionOfUse)
                .dataSegments(sectionIscc)
                .build());

        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        return provenAIIssuer.generateUnsignedVC(verifiableCredential);
    }


    public Object createAgentSignedVcJwt(W3CVC w3CVC, String processorOrganizationDid) throws ProvenAiException {
        LocalKey localKey = new LocalKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();

        return provenAIIssuer.generateSignedVCJwt(w3CVC, localKey, issuerDid, processorOrganizationDid);

    }






}
