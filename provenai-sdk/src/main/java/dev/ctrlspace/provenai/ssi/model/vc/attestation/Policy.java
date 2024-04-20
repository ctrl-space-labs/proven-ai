package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
class Policy {
    private String policyType;
    private String policyValue;
}
