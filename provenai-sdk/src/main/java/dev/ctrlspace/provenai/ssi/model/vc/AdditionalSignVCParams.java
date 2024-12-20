package dev.ctrlspace.provenai.ssi.model.vc;

import kotlinx.serialization.json.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Class to represent the 'additionalSignVCParams' to sign a Verifiable Credential.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AdditionalSignVCParams {

    private Map<String, String> additionalJwtHeaders;
    private Map<String, JsonElement> additionalJwtOptions;


}
