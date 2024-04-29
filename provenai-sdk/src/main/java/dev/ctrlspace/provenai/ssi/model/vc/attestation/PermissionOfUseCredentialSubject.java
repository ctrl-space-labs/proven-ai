package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PermissionOfUseCredentialSubject implements CredentialSubject {

    private String id; // DID of the AI agent
    private String ownerDID; // DID of the data owner
    private List<Policy> policies; // List of usage policies
    private List<String> dataSegments; // List of ISCCs
}
