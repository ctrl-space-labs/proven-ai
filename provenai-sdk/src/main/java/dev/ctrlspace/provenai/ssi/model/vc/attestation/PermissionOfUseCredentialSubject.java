package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PermissionOfUseCredentialSubject {

    private String id; // DID of the AI agent
    private String ownerID; // DID of the data owner
    private List<Policy> policies; // List of policies
    private List<String> dataSegments; // List of ISCCs
}
