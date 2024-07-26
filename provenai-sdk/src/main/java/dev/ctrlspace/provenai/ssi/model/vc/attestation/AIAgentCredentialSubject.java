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
 * This class represents the credential subject of an AI Agent Credential.
 * <p>
 *     This class is compliant with the W3C and EBSI v.2 Data Model of Verifiable Credentials specifications.
 *     @see <a href="https://code.europa.eu/ebsi/json-schema/-/tree/main/schemas/vcdm2.0/attestation">EBSI v.2.0 Attestation</a>
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AIAgentCredentialSubject implements CredentialSubject {
    private String id;  // organization did id
    private String organizationName;
    private String agentName;
    private String agentId;
    private Instant creationDate;  // Assuming the use of java.util.Date; consider java.time.LocalDateTime for more precise control
    private List<Policy> usagePolicies;


}
