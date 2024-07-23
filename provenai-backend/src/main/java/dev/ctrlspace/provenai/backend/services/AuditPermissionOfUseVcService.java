package dev.ctrlspace.provenai.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.BlockchainRegistrationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import dev.ctrlspace.provenai.backend.repositories.specifications.AuditPermissionOfUseVcPredicates;
import dev.ctrlspace.provenai.blockchain.MerkleTreeUtil;
import dev.ctrlspace.provenai.blockchain.ProvenAIBlockchainUtil;
import dev.ctrlspace.provenai.ssi.issuer.ProvenAIIssuer;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;
import id.walt.credentials.vc.vcs.W3CVC;
import org.cardanofoundation.merkle.MerkleElement;
import org.cardanofoundation.merkle.ProofItem;
import id.walt.crypto.keys.jwk.JWKKey;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import dev.ctrlspace.provenai.backend.repositories.AuditPermissionOfUseVcRepository;
@Service
public class AuditPermissionOfUseVcService {
    @Value("${proven-ai.ssi.issuer-private-jwk}")
    private String issuerPrivateJwk;
    @Value("${proven-ai.ssi.issuer-did}")
    private String issuerDid;

    @Value("${proven-ai.blockchain.rpc-url}")
    private String rpcUrl;

    @Value("${proven-ai.blockchain.chain-id}")
    private long chainId;

    @Value("${proven-ai.blockchain.private-key}")
    private String privateKey;

    @Value("${proven-ai.blockchain.contract-address}")
    private String contractAddress;

    private AuditPermissionOfUseVcRepository auditPermissionOfUseVcRepository;

    @Autowired
    public AuditPermissionOfUseVcService(AuditPermissionOfUseVcRepository auditPermissionOfUseVcRepository) {
        this.auditPermissionOfUseVcRepository = auditPermissionOfUseVcRepository;
    }

    public AuditPermissionOfUseVc getAuditPermissionOfUseVcById(UUID id) throws ProvenAiException {
        return auditPermissionOfUseVcRepository.findById(id).orElseThrow(() -> new ProvenAiException("AUDIT_LOG_NOT_FOUND", "Audit Log not found with id: " + id, HttpStatus.NOT_FOUND));
    }


    public List<AuditPermissionOfUseDTO> getAuditPermissionOfUseAnalytics(AuditPermissionOfUseVcCriteria criteria) throws ProvenAiException {


        return auditPermissionOfUseVcRepository.findAuditPermissionsAnalytics(criteria);
    }

    /**
     * Get audit logs by criteria. It returns every single interaction between the owner and the processor
     * It is a very heavy query and it is itendend to be used only for the creation of Merkle Tree Proofs
     *
     * @param criteria
     * @param pageable
     * @return
     * @throws ProvenAiException
     */
    public Page<AuditPermissionOfUseVc> getAuditPermissionOfUseByCriteria(AuditPermissionOfUseVcCriteria criteria, Pageable pageable) throws ProvenAiException {
        //This is a very heavy query, so we need to limit the time interval and the number of agents
        if (criteria.getFrom() == null ||
                criteria.getTo() == null ||
                (criteria.getProcessorOrganizationDid().isEmpty() && criteria.getOwnerOrganizationDid().isEmpty())) {
            throw new ProvenAiException("AUDIT_PERMISSION_CRITERIA_MISSING_MANDATORY_VALUES", "Missing mandatory criteria [from, to, (agentIdIn or DataPodIdIn)] ara mandatory", HttpStatus.BAD_REQUEST);
        }
        if (Duration.between(criteria.getFrom(), criteria.getTo()).getSeconds() > 60 * 60 * 24 ) {
            throw new ProvenAiException("TIME_INTERVAL_TOO_LARGE", "Time interval is too large. For a period of more than 24 hours, please use the analytics API that returns aggrigated information", HttpStatus.BAD_REQUEST);
        }
        return auditPermissionOfUseVcRepository.findAll(AuditPermissionOfUseVcPredicates.build(criteria), pageable);
    }


    public AuditPermissionOfUseVc createAuditLog( UUID ownerOrganizationId, String ownerOrganizationDid,
                                                 UUID processorOrganizationId, String processorOrganizationDid,
                                                 UUID agentId, UUID searchId, UUID dataPodId,
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
        auditPermissionOfUseVc.setProcessorAgentId(agentId);

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
        JWKKey jwkKey = new JWKKey(issuerPrivateJwk);
        ProvenAIIssuer provenAIIssuer = new ProvenAIIssuer();
        AdditionalSignVCParams additionalSignVCParams = new AdditionalSignVCParams();

        return provenAIIssuer.generateSignedVCJwt(w3CVC, jwkKey, issuerDid, processorOrganizationDid);

    }

    /**
     *
     *
     * @param organizationDid
     * @param date
     * @return
     * @throws Exception
     */
    public MerkleElement<Serializable> generateDailyUsageMerkleTree(String organizationDid, Instant date) throws Exception {


        ZonedDateTime zdt = date.atZone(ZoneId.of("UTC"));
        Instant startOfDay = date.atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant endOfDay = zdt.toLocalDate().atStartOfDay(ZoneId.of("UTC")).plusDays(1).minusNanos(1).toInstant();


        AuditPermissionOfUseVcCriteria criteria = AuditPermissionOfUseVcCriteria.builder()
                .processorOrganizationDid(organizationDid)
                .from(startOfDay)
                .to(endOfDay)
                .build();
        Page<AuditPermissionOfUseVc> auditPermissionOfUseVcs = this.getAuditPermissionOfUseByCriteria(criteria, Pageable.unpaged());

        MerkleTreeUtil merkleTreeUtil = new MerkleTreeUtil();

        MerkleElement<Serializable> root = merkleTreeUtil.createMerkleTree(auditPermissionOfUseVcs.getContent());

        return root;

    }

    public BlockchainRegistrationDTO registerDailyUsageToBlockchain(String organizationDid, Instant date, MerkleElement<Serializable> root) throws Exception {

        ZonedDateTime zdt = date.atZone(ZoneId.of("UTC"));
        Instant startOfDay = date.atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay(ZoneId.of("UTC")).toInstant();

        byte[] rootHash = root.itemHash();
        ProvenAIBlockchainUtil provenAIBlockchainUtil = new ProvenAIBlockchainUtil(rpcUrl, privateKey, contractAddress, chainId );

        String organizationBlockchainAddress = ProvenAIBlockchainUtil.getEthereumAddressFromDID(organizationDid);

        String transactionHash = provenAIBlockchainUtil.updateDailyUsage(organizationBlockchainAddress, BigInteger.valueOf(startOfDay.toEpochMilli()), rootHash);

        BlockchainRegistrationDTO blockchainRegistrationDTO = BlockchainRegistrationDTO.builder()
                .merkleTreeRootHash(new String(rootHash, java.nio.charset.StandardCharsets.UTF_8))
                .transactionHash(transactionHash)
                .build();
        return blockchainRegistrationDTO;
    }

    public List<AuditProofItem> generateProofs(String organizationDID, Instant date, AuditPermissionOfUseVcCriteria criteria, MerkleElement<Serializable> root) throws Exception {

        if (criteria.getDocumentIscc() == null) {
            throw new ProvenAiException("MISSING_DOCUMENT_ISCC", "Document ISCC is mandatory", HttpStatus.BAD_REQUEST);
        }


        ZonedDateTime zdt = date.atZone(ZoneId.of("UTC"));
        Instant startOfDay = date.atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant endOfDay = zdt.toLocalDate().atStartOfDay(ZoneId.of("UTC")).plusDays(1).minusNanos(1).toInstant();

        // make sure that the criteria is for the same day and the same organization
        criteria.setProcessorOrganizationDid(organizationDID);
        criteria.setFrom(startOfDay);
        criteria.setTo(endOfDay);

        MerkleTreeUtil merkleTreeUtil = new MerkleTreeUtil();

        Page<AuditPermissionOfUseVc> recordsToGetProofs = this.getAuditPermissionOfUseByCriteria(criteria, Pageable.unpaged());

        List<AuditProofItem> proofs = recordsToGetProofs
                .stream()
                .map(auditPermissionOfUseVc -> {
                    io.vavr.collection.List<ProofItem> proof = merkleTreeUtil.getProof(root, auditPermissionOfUseVc.toString()).get();
                    var proofItem = new AuditProofItem(auditPermissionOfUseVc, proof);
                    return proofItem;
                })
                .toList();

        return proofs;
    }

    public BlockchainRegistrationDTO generateAndRegisterDailyUsageToBlockchain(String organizationDid, Instant date) throws Exception {
        MerkleElement<Serializable> root = generateDailyUsageMerkleTree(organizationDid, date);
        return registerDailyUsageToBlockchain(organizationDid, date, root);
    }

    public record AuditProofItem(AuditPermissionOfUseVc auditRecord, io.vavr.collection.List<ProofItem> proof) {}

    public int verifyProofs(byte[] rootHash, List<AuditProofItem> proofs) throws Exception {
        MerkleTreeUtil merkleTreeUtil = new MerkleTreeUtil();
        int validProofs = 0;
        for (AuditProofItem proofItem : proofs) {
            boolean isValid = merkleTreeUtil.verifyProof(rootHash, proofItem.auditRecord(), proofItem.proof());
            if (isValid) {
                validProofs++;
            }
        }
        return validProofs;
    }

    public int generateAndVerifyProofs(String organizationDID, Instant date, AuditPermissionOfUseVcCriteria criteria) throws Exception {
        MerkleElement<Serializable> root = generateDailyUsageMerkleTree(organizationDID, date);
        List<AuditProofItem> proofs = generateProofs(organizationDID, date, criteria, root);
        ZonedDateTime zdt = date.atZone(ZoneId.of("UTC"));
        Instant startOfDay = date.atZone(ZoneId.of("UTC")).toLocalDate().atStartOfDay(ZoneId.of("UTC")).toInstant();



        ProvenAIBlockchainUtil provenAIBlockchainUtil = new ProvenAIBlockchainUtil(rpcUrl, privateKey, contractAddress, chainId);
        String organizationBlockchainAddress = ProvenAIBlockchainUtil.getEthereumAddressFromDID(organizationDID);
        String blockchainRoot = provenAIBlockchainUtil.getDailyUsage(organizationBlockchainAddress, BigInteger.valueOf(startOfDay.toEpochMilli()));
// 0x75318d93da826a962e9cca66067195b83af49be7
//        1718542800000
//        1718496000000

        return verifyProofs(blockchainRoot.getBytes(), proofs);
    }



}
