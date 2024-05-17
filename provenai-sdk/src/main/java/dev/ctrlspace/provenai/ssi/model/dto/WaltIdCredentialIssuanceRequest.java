package dev.ctrlspace.provenai.ssi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private W3CVC vc;
}
