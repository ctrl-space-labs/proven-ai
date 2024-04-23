package dev.ctrlspace.provenai.ssi.model.vc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

// Main class representing the root of the JSON object
// java do follows
/**
 * Represents a Verifiable Credential.
 * <p>
 *     A Verifiable Credential is a tamper-evident credential that has authorship that can be cryptographically verified.
 *     Verifiable Credentials are a key component of the W3C Verifiable Credentials Data Model.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VerifiableCredential<T extends CredentialSubject> {
    public static Set<String> supportedTypes = Set.of("VerifiableCredential",
            "VerifiableAttestation",
            "VerifiablePID",
            "LegalEntityVerifiableID");


    private List<String> context;
    private String id;
    private List<String> type;
    private String issuer;
    private Date validFrom;
    private Date validUntil;
    private T credentialSubject;
    private CredentialSchema credentialSchema;

}
