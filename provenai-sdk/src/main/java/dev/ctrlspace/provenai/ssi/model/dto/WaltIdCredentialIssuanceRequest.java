package dev.ctrlspace.provenai.ssi.model.dto;

import id.walt.credentials.vc.vcs.W3CVC;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WaltIdCredentialIssuanceRequest {
    private IssuerKey issuerKey;
    private String issuerDid;
    private String credentialConfigurationId = "";
    private W3CVC credentialData;
}
