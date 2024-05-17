package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to represent the 'policy' part of the JSON
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Policy {
    private String policyType;
    private String policyValue;
}
