package dev.ctrlspace.provenai.backend.model.dtos;

import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchResult {

    private String id;
    private String iscc;
    private String title;
    private String text;
    private String documentURL;
    private String tokens;
    private String ownerName;
    // TODO investigate if we need to change this with the actual VC
    private PermissionOfUseCredentialSubject permissionOfUseCredentialSubject;

}
