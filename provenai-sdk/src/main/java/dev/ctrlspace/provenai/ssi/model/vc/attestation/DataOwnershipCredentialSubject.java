package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Class to represent the 'credentialSubject' part of the JSON
 * This class represents the credential subject of a Data Ownership Credential Subject.
 * <p>
 *     This class is compliant with the W3C and EBSI v.2 Data Model of Verifiable Credentials specifications.
 *     @see <a href="https://code.europa.eu/ebsi/json-schema/-/tree/main/schemas/vcdm2.0/attestation">EBSI v.2.0 Attestation</a>
 * </p>
 */
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
