package dev.ctrlspace.provenai.ssi.model.vc;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to represent the 'credentialSchema' part of the JSON
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
class CredentialSchema {
    private String id;
    private String type;
}
