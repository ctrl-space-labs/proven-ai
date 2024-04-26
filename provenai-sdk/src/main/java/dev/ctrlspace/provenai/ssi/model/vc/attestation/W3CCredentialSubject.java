package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import kotlin.Pair;
import kotlinx.serialization.json.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class W3CCredentialSubject implements CredentialSubject{


    private String id;
    private List<String> context;
    private List<String> type;
    private String credentialId;
    private String issuerDid;
    private Instant validFrom;
    private Duration validFor;
    private Instant validUntil;
    private Pair<String, JsonElement> useData;
    private JsonElement credentialSubject;
    private String subjectDid;
}
