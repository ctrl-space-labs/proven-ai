package dev.ctrlspace.provenai.backend.model.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import id.walt.credentials.vc.vcs.W3CVC;
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
        private W3CVC w3cVc;

    }

