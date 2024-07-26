package dev.ctrlspace.provenai.backend.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VCOfferDTO {
    private String id;
    private String credentialOfferUrl;
    private Object credentialJwt;
}
