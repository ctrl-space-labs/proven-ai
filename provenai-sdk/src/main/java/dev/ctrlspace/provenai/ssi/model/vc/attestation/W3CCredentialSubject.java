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

/**
 * Class to represent the 'credentialSubject' and useData part of the JSON for W3CVC format Verifiable Credentials.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class W3CCredentialSubject implements CredentialSubject{


    private String id;
    private Pair<String, JsonElement> useData;
    private JsonElement credentialSubject;
}
