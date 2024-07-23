package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DataOwnershipCredentialSubject implements CredentialSubject {
    private String id;
    private String dataPodName;
    private String dataPodId;
    private String ownershipStatus;
    private Instant creationDate;
    private List<Policy> usagePolicies;
    private String isccCollectionMerkleRoot;

}
