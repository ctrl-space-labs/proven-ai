package dev.ctrlspace.provenai.backend.model.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VerifiablePresentationResponse {
        private String credentialOfferUrl;
        private JsonNode verifiablePresentation;

    }

