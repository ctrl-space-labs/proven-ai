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

    private String documentSectionId;
    private String documentId;
    private String iscc;
    private String title;
    private String text;
    private String documentURL; // get document or document section by ID
    private String tokens;
    private String ownerName; // get public info of organization by ID
    // TODO investigate if we need to change this with the actual VC
    private PermissionOfUseCredentialSubject permissionOfUseCredentialSubject;

}
