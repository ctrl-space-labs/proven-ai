package dev.ctrlspace.provenai.ssi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class IssuerKey {
    private String jwk;
    private String type;

    // getters and setters
}
